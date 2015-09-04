/*
 * Copyright (C) 2015 WTFDYUM
 *
 * This file is part of the WTFDYUM project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeanchampemont.wtfdyum.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.dto.type.UserLimitType;
import com.jeanchampemont.wtfdyum.service.CronService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.service.UserService;
import com.jeanchampemont.wtfdyum.service.feature.FeaturesService;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMExceptionType;

/**
 * The Class CronServiceImpl.
 */
@Service
public class CronServiceImpl implements CronService {

    /**
     * Instantiates a new cron service impl.
     *
     * @param principalService
     *            the principal service
     * @param userService
     *            the user service
     * @param twitterService
     *            the twitter service
     * @param unfollowDMText
     *            the unfollow dm text
     * @param unfollowTweetText
     *            the unfollow tweet text
     */
    @Autowired
    public CronServiceImpl(final PrincipalService principalService,
            final UserService userService,
            final TwitterService twitterService,
            final FeaturesService featuresService) {
        this.principalService = principalService;
        this.userService = userService;
        this.twitterService = twitterService;
        this.featuresService = featuresService;
    }

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /** The principal service. */
    private final PrincipalService principalService;

    /** The user service. */
    private final UserService userService;

    /** The twitter service. */
    private final TwitterService twitterService;
    
    /** The feature services. */
    private final FeaturesService featuresService;

    /*
     * (non-Javadoc)
     *
     * @see com.jeanchampemont.wtfdyum.service.CronService#checkCredentials()
     */
    @Override
    @Scheduled(fixedDelayString = "${wtfdyum.credentials-check-delay}", initialDelay = 120000L)
    public void checkCredentials() {
        log.debug("Checking credentials...");
        final StopWatch watch = new StopWatch();
        watch.start();
        final Set<Long> members = principalService.getMembers();

        for (final Long userId : members) {
            final Principal principal = principalService.get(userId);

            if (!twitterService.verifyCredentials(principal)) {
                userService.applyLimit(userId, UserLimitType.CREDENTIALS_INVALID);
                userService.addEvent(userId, new Event(EventType.INVALID_TWITTER_CREDENTIALS, ""));
            } else {
                userService.resetLimit(userId, UserLimitType.CREDENTIALS_INVALID);
            }
        }
        watch.stop();
        log.debug("Finished checking credentials in {} ms", watch.getTotalTimeMillis());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jeanchampemont.wtfdyum.service.CronService#findUnfollowers()
     */
    @Override
    @Scheduled(fixedDelayString = "${wtfdyum.unfollow-check-delay}", initialDelay = 120000L)
    public void cron() {
        log.debug("Starting cron method...");
        final StopWatch watch = new StopWatch();
        watch.start();
        final Set<Long> members = principalService.getMembers();

        for (final Long userId : members) {
        	try {
        		Set<Feature> enabledFeatures = userService.getEnabledFeatures(userId);
        		for(Feature enabledFeature : enabledFeatures) {
        			featuresService.cron(userId, enabledFeature);
        		}
        		for(Feature enabledFeature : enabledFeatures) {
        			featuresService.completeCron(userId, enabledFeature);
        		}
	        } catch (final WTFDYUMException e) {
	            if (WTFDYUMExceptionType.GET_FOLLOWERS_RATE_LIMIT_EXCEEDED.equals(e.getType())) {
	                userService.addEvent(userId, new Event(EventType.RATE_LIMIT_EXCEEDED, null));
	            } else {
	                userService.addEvent(userId, new Event(EventType.TWITTER_ERROR, null));
	            }
	        } catch (final Throwable t) {
	            userService.addEvent(userId, new Event(EventType.UNKNOWN_ERROR, null));
	        }
        }
        watch.stop();
        log.debug("Finished cron in {} ms", watch.getTotalTimeMillis());
    }

}
