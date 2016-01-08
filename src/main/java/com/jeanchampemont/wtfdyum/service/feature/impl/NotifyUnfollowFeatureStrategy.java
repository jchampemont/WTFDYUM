/*
 * Copyright (C) 2015, 2016 WTFDYUM
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

import com.google.common.primitives.Longs;
import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.service.FollowersService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class NotifyUnfollowFeatureStrategy extends AbstractFeatureStrategy {

    @Autowired
    public NotifyUnfollowFeatureStrategy(final PrincipalService principalService,
                                         final FollowersService followersService,
                                         final TwitterService twitterService,
                                         @Value("${wtfdyum.unfollow.dm-text}") final String unfollowDMText) {
        super(Feature.NOTIFY_UNFOLLOW);
        this.principalService = principalService;
        this.followersService = followersService;
        this.twitterService = twitterService;
        this.unfollowDMText = unfollowDMText;
    }

    private final PrincipalService principalService;

    private final FollowersService followersService;

    private final TwitterService twitterService;

    private final String unfollowDMText;

    @Override
    public void completeCron(final Long userId) throws WTFDYUMException {
        final Principal principal = principalService.get(userId);
        final Set<Long> followers = twitterService.getFollowers(userId, Optional.ofNullable(principal));
        followersService.saveFollowers(userId, followers);
    }

    @Override
    public Set<Event> cron(final Long userId) throws WTFDYUMException {
        final Set<Event> result = new HashSet<>();
        final Principal principal = principalService.get(userId);
        final Set<Long> followers = twitterService.getFollowers(userId, Optional.ofNullable(principal));

        final Set<Long> unfollowersId = followersService.getUnfollowers(userId, followers);

        final List<User> unfollowers = twitterService.getUsers(principal, Longs.toArray(unfollowersId));
        for (final User unfollower : unfollowers) {
            result.add(new Event(EventType.UNFOLLOW, unfollower.getScreenName()));
            twitterService.sendDirectMessage(principal, userId,
                    String.format(unfollowDMText, unfollower.getScreenName()));
        }
        return result;
    }

    @Override
    public boolean hasCron() {
        return true;
    }
}
