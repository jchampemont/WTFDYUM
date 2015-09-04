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
import com.jeanchampemont.wtfdyum.service.FollowersService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;

/**
 * The Class TweetUnfollowFeatureService.
 */
@Service
public class TweetUnfollowFeatureService extends AbstractFeatureService {

    /**
     * Instantiates a new tweet unfollow feature service.
     *
     * @param principalService
     *            the principal service
     * @param followersService
     *            the user service
     * @param twitterService
     *            the twitter service
     * @param unfollowTweetText
     *            the unfollow tweet text
     */
    @Autowired
    public TweetUnfollowFeatureService(final PrincipalService principalService,
            final FollowersService followersService,
            final TwitterService twitterService,
            @Value("${wtfdyum.unfollow.tweet-text}") final String unfollowTweetText) {
        super(Feature.TWEET_UNFOLLOW);
        this.principalService = principalService;
        this.followersService = followersService;
        this.twitterService = twitterService;
        this.unfollowTweetText = unfollowTweetText;
    }

    /** The principal service. */
    private final PrincipalService principalService;

    /** The user service. */
    private final FollowersService followersService;

    /** The twitter service. */
    private final TwitterService twitterService;

    /** The unfollow dm text. */
    private final String unfollowTweetText;

    /*
     * (non-Javadoc)
     *
     * @see com.jeanchampemont.wtfdyum.service.feature.AbstractFeatureService#
     * completeCron(java.lang.Long)
     */
    @Override
    public void completeCron(final Long userId) throws WTFDYUMException {
        final Principal principal = principalService.get(userId);
        final Set<Long> followers = twitterService.getFollowers(userId, Optional.ofNullable(principal));
        followersService.saveFollowers(userId, followers);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.feature.AbstractFeatureService#cron(
     * java.lang.Long)
     */
    @Override
    public Set<Event> cron(final Long userId) throws WTFDYUMException {
        final Set<Event> result = new HashSet<>();
        final Principal principal = principalService.get(userId);
        final Set<Long> followers = twitterService.getFollowers(userId, Optional.ofNullable(principal));

        final Set<Long> unfollowersId = followersService.getUnfollowers(userId, followers);

        for (final Long unfollowerId : unfollowersId) {
            final User unfollower = twitterService.getUser(principal, unfollowerId);
            result.add(new Event(EventType.UNFOLLOW, unfollower.getScreenName()));
            twitterService.tweet(principal,
                    String.format(unfollowTweetText, unfollower.getScreenName()));
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jeanchampemont.wtfdyum.service.feature.FeatureService#hasCron()
     */
    @Override
    public boolean hasCron() {
        return true;
    }
}
