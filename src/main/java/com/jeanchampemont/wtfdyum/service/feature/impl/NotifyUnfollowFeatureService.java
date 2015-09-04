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
package com.jeanchampemont.wtfdyum.service.feature.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.service.UserService;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;

/**
 * The Class NotifyUnfollowFeatureService.
 */
@Service
public class NotifyUnfollowFeatureService extends AbstractFeatureService {

	/**
	 * Instantiates a new notify unfollow feature service.
	 *
	 * @param principalService the principal service
	 * @param userService the user service
	 * @param twitterService the twitter service
	 * @param unfollowDMText the unfollow dm text
	 */
	@Autowired
	public NotifyUnfollowFeatureService(final PrincipalService principalService,
            final UserService userService,
            final TwitterService twitterService,
            @Value("${wtfdyum.unfollow.dm-text}") final String unfollowDMText) {
		super(Feature.NOTIFY_UNFOLLOW);
		this.principalService = principalService;
		this.userService = userService;
		this.twitterService = twitterService;
		this.unfollowDMText = unfollowDMText;
	}
	
	 /** The principal service. */
    private final PrincipalService principalService;

    /** The user service. */
    private final UserService userService;

    /** The twitter service. */
    private final TwitterService twitterService;

    /** The unfollow dm text. */
    private final String unfollowDMText;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jeanchampemont.wtfdyum.service.feature.FeatureService#hasCron()
	 */
	@Override
	public boolean hasCron() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.AbstractFeatureService#cron(
	 * java.lang.Long)
	 */
	@Override
	public Set<Event> cron(Long userId) throws WTFDYUMException {
		final Set<Event> result = new HashSet<>();
		final Principal principal = principalService.get(userId);
        final Set<Long> followers = twitterService.getFollowers(userId, Optional.ofNullable(principal));

        final Set<Long> unfollowersId = userService.getUnfollowers(userId, followers);

        for (final Long unfollowerId : unfollowersId) {
            final User unfollower = twitterService.getUser(principal, unfollowerId);
            result.add(new Event(EventType.UNFOLLOW, unfollower.getScreenName()));
            twitterService.sendDirectMessage(principal, userId,
                    String.format(unfollowDMText, unfollower.getScreenName()));
        }
        return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jeanchampemont.wtfdyum.service.feature.AbstractFeatureService#
	 * completeCron(java.lang.Long)
	 */
	@Override
	public void completeCron(Long userId) throws WTFDYUMException {
		final Principal principal = principalService.get(userId);
		final Set<Long> followers = twitterService.getFollowers(userId, Optional.ofNullable(principal));
		userService.saveFollowers(userId, followers);
	}
}
