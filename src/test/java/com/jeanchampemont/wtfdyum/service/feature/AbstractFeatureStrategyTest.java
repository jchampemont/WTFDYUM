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
package com.jeanchampemont.wtfdyum.service.feature;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.service.FollowersService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.service.feature.impl.AbstractFeatureStrategy;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public abstract class AbstractFeatureStrategyTest {

    protected static final String TWEET_TEXT = "@%s tweet";

    protected static final String DM_TEXT = "@%s DM";

    protected PrincipalService principalService;

    protected FollowersService followersService;

    protected TwitterService twitterService;

    protected AbstractFeatureStrategy sut;

    protected RedisTemplate<String, Feature> featureRedisTemplate;

    protected SetOperations<String, Feature> featureSetOperations;

    @Test
    public void disableFeatureTestDisabledFeature() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.remove("FEATURES_1334", sut.getFeature())).thenReturn(0L);

        final boolean result = sut.disableFeature(1334L);

        verify(featureSetOperations, times(1)).remove("FEATURES_1334", sut.getFeature());

        assertThat(result).isFalse();
    }

    @Test
    public void disableFeatureTestEnabledFeature() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.remove("FEATURES_1334", sut.getFeature())).thenReturn(1L);

        final boolean result = sut.disableFeature(1334L);

        verify(featureSetOperations, times(1)).remove("FEATURES_1334", sut.getFeature());

        assertThat(result).isTrue();
    }

    @Test
    public void enableFeatureTestDisabledFeature() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.add("FEATURES_1334", sut.getFeature())).thenReturn(1L);

        final boolean result = sut.enableFeature(1334L);

        verify(featureSetOperations, times(1)).add("FEATURES_1334", sut.getFeature());

        assertThat(result).isTrue();
    }

    @Test
    public void enableFeatureTestEnabledFeature() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.add("FEATURES_1334", sut.getFeature())).thenReturn(0L);

        final boolean result = sut.enableFeature(1334L);

        verify(featureSetOperations, times(1)).add("FEATURES_1334", sut.getFeature());

        assertThat(result).isFalse();
    }

    @Test
    public void isFeatureEnabledTest() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.isMember("FEATURES_1899", sut.getFeature())).thenReturn(true);

        final boolean featureEnabled = sut.isEnabled(1899L);

        assertThat(featureEnabled).isTrue();
    }

    @SuppressWarnings("unchecked")
    protected void _init() {
        principalService = mock(PrincipalService.class);
        followersService = mock(FollowersService.class);
        twitterService = mock(TwitterService.class);
        featureRedisTemplate = mock(RedisTemplate.class);
        featureSetOperations = mock(SetOperations.class);
    }

    protected Set<Long> followers(final Principal principal, final BiConsumer<OngoingStubbing<Set<Long>>, Set<Long>> l)
            throws WTFDYUMException {
        final Set<Long> followers = new HashSet<>(Arrays.asList(10L, 11L, 12L));
        l.accept(when(twitterService.getFollowers(principal.getUserId(), Optional.ofNullable(principal))), followers);
        return followers;
    }

    protected Principal principal(final long id) {
        when(principalService.getMembers()).thenReturn(new HashSet<>(Arrays.asList(id)));
        final Principal principal = new Principal(id, "Principal 1 Token", "Principal 1 Token Secret");
        when(principalService.get(id)).thenReturn(principal);
        return principal;
    }

    protected List<User> unfollowers(final Principal principal, final Set<Long> followersIds,
            final BiConsumer<OngoingStubbing<Set<Long>>, Set<Long>> l) throws WTFDYUMException {
        final Set<Long> unfollowers = new HashSet<>(Arrays.asList(10L, 11L));
        l.accept(when(followersService.getUnfollowers(principal.getUserId(), followersIds)), unfollowers);

        // unfollowers 10 and 11 details :
        final User user10 = new User();
        user10.setId(10L);
        user10.setScreenName("user10");

        final User user11 = new User();
        user11.setId(11L);
        user11.setScreenName("user11");

        when(twitterService.getUsers(principal, 10L, 11L)).thenReturn(Arrays.asList(user10, user11));
        return Arrays.asList(user10, user11);
    }

    protected void verifyUnfollowDM(final Principal principal, final User unfollower) throws WTFDYUMException {
        verify(twitterService, times(1)).sendDirectMessage(principal, principal.getUserId(),
                String.format(DM_TEXT, unfollower.getScreenName()));
    }

    protected void verifyUnfollowTweet(final Principal principal, final User unfollower) throws WTFDYUMException {
        verify(twitterService, times(1)).tweet(principal, String.format(TWEET_TEXT, unfollower.getScreenName()));
    }
}
