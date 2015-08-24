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
package com.jeanchampemont.wtfdyum.service;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.EventType;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.service.impl.CronServiceImpl;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMExceptionType;

/**
 * The Class CronServiceTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class CronServiceTest {

    /** The principal service. */
    @Mock
    private PrincipalService principalService;

    /** The user service. */
    @Mock
    private UserService userService;

    /** The twitter service. */
    @Mock
    private TwitterService twitterService;

    /** The system under test. */
    private CronService sut;

    /**
     * Inits the test.
     */
    @Before
    public void _init() {
        initMocks(this);
        sut = new CronServiceImpl(principalService, userService, twitterService, "@%s DM", "@%s tweet");
    }

    /**
     * Check credentials test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void checkCredentialsTest() throws Exception {
        when(principalService.getMembers()).thenReturn(new HashSet<>(Arrays.asList(1L)));
        final Principal principal = new Principal(1L, "tok", "ttoktok");
        when(principalService.get(1L)).thenReturn(principal);
        when(twitterService.verifyCredentials(principal)).thenReturn(true);

        sut.checkCredentials();
    }

    /**
     * Check credentials test invalid.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void checkCredentialsTestInvalid() throws Exception {
        when(principalService.getMembers()).thenReturn(new HashSet<>(Arrays.asList(1L)));
        final Principal principal = new Principal(1L, "tok", "ttoktok");
        when(principalService.get(1L)).thenReturn(principal);
        when(twitterService.verifyCredentials(principal)).thenReturn(false);

        sut.checkCredentials();

        for (final Feature feature : Feature.values()) {
            verify(userService, times(1)).disableFeature(1L, feature);
        }
        verify(userService, times(1)).addEvent(1L, new Event(EventType.INVALID_TWITTER_CREDENTIALS, ""));
    }

    /**
     * Find unfollowers test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void findUnfollowersTest() throws Exception {
        /*
         * 5 members
         *
         * 1: nominal test case
         *
         * 2: an error is thrown while contacting twitter
         *
         * 3: account has exceeded its rate limits
         *
         * 4: a NPE is thrown whil computing unfollowers
         *
         * 5: feature disabled
         *
         * 6: send a tweet when an unfollower is found
         */
        when(principalService.getMembers()).thenReturn(new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L)));

        //Feature enabled for all members
        when(userService.isFeatureEnabled(anyLong(), eq(Feature.NOTIFY_UNFOLLOW))).thenReturn(true);
        //except member 5L
        when(userService.isFeatureEnabled(5L, Feature.NOTIFY_UNFOLLOW)).thenReturn(false);

        // Feature disabled for all members
        when(userService.isFeatureEnabled(anyLong(), eq(Feature.TWEET_UNFOLLOW))).thenReturn(false);
        // except member 6L
        when(userService.isFeatureEnabled(6L, Feature.TWEET_UNFOLLOW)).thenReturn(true);

        // principals for members 1, 2, 3, 4, 6
        final List<Principal> principals = Arrays.asList(new Principal(1L, "", ""), new Principal(2L, "", ""),
                new Principal(3L, "", ""), new Principal(4L, "", ""), null, new Principal(6L, "", ""));
        when(principalService.get(anyLong())).thenReturn(principals.get(0), principals.get(1), principals.get(2),
                principals.get(3), principals.get(5));

        // default followers list
        final Set<Long> followers = new HashSet<>(Arrays.asList(10L, 11L, 12L));
        // 1L: default followers
        when(twitterService.getFollowers(1L, Optional.ofNullable(principals.get(0)))).thenReturn(followers);
        // 2L: throw a twitter error
        when(twitterService.getFollowers(2L, Optional.ofNullable(principals.get(1))))
        .thenThrow(new WTFDYUMException(WTFDYUMExceptionType.TWITTER_ERROR));
        // 3L: throw a rate limit error
        when(twitterService.getFollowers(3L, Optional.ofNullable(principals.get(2))))
        .thenThrow(new WTFDYUMException(WTFDYUMExceptionType.GET_FOLLOWERS_RATE_LIMIT_EXCEEDED));
        // 4L: default followers
        when(twitterService.getFollowers(4L, Optional.ofNullable(principals.get(3)))).thenReturn(followers);
        // 6L: default followers
        when(twitterService.getFollowers(6L, Optional.ofNullable(principals.get(5)))).thenReturn(followers);

        // default unfollowers list
        final Set<Long> unfollowers = new HashSet<>(Arrays.asList(10L, 11L));
        // 1L: default unfollowers
        when(userService.getUnfollowers(1L, followers)).thenReturn(unfollowers);
        // 4L: NPE thrown while computing unfollowers
        when(userService.getUnfollowers(4L, followers)).thenThrow(new NullPointerException());
        // 6L : default unfollowers
        when(userService.getUnfollowers(6L, followers)).thenReturn(unfollowers);

        // unfollowers 10 and 11 details :
        final User user10 = new User();
        user10.setId(10L);
        user10.setScreenName("user10");

        final User user11 = new User();
        user11.setId(11L);
        user11.setScreenName("user11");

        when(twitterService.getUser(principals.get(0), 10L)).thenReturn(user10);
        when(twitterService.getUser(principals.get(0), 11L)).thenReturn(user11);
        when(twitterService.getUser(principals.get(5), 10L)).thenReturn(user10);
        when(twitterService.getUser(principals.get(5), 11L)).thenReturn(user11);



        sut.findUnfollowers();



        // 1L :
        // A unfollow event and a direct message should have been sent for
        // unfollower 10
        verify(userService, times(1)).addEvent(1L, new Event(EventType.UNFOLLOW, user10.getScreenName()));
        verify(twitterService, times(1)).sendDirectMessage(principals.get(0), 1L, String.format(
                "@%s DM", user10.getScreenName()));

        // A unfollow event and a direct message should have been sent for
        // unfollower 11
        verify(userService, times(1)).addEvent(1L, new Event(EventType.UNFOLLOW, user11.getScreenName()));
        verify(twitterService, times(1)).sendDirectMessage(principals.get(0), 1L, String.format(
                "@%s DM", user11.getScreenName()));

        // 2L should have an event TWITTER_ERROR
        verify(userService, times(1)).addEvent(2L, new Event(EventType.TWITTER_ERROR, null));

        // 3L should have an event RATE_LIMIT_EXCEEDED
        verify(userService, times(1)).addEvent(3L, new Event(EventType.RATE_LIMIT_EXCEEDED, null));
        // 4L should have an event UNKNOWN_ERROR
        verify(userService, times(1)).addEvent(4L, new Event(EventType.UNKNOWN_ERROR, null));

        // 6L :
        // A unfollow event, a direct message and a tweet should have been sent
        // for
        // unfollower 10
        verify(userService, times(1)).addEvent(6L, new Event(EventType.UNFOLLOW, user10.getScreenName()));
        verify(twitterService, times(1)).sendDirectMessage(principals.get(5), 6L, String.format(
                "@%s DM", user10.getScreenName()));
        verify(twitterService, times(1)).tweet(principals.get(5),
                String.format("@%s tweet", user10.getScreenName()));

        // A unfollow event, a direct message and a tweet should have been sent
        // for
        // unfollower 11
        verify(userService, times(1)).addEvent(6L, new Event(EventType.UNFOLLOW, user11.getScreenName()));
        verify(twitterService, times(1)).sendDirectMessage(principals.get(5), 6L, String.format(
                "@%s DM", user11.getScreenName()));
        verify(twitterService, times(1)).tweet(principals.get(5),
                String.format("@%s tweet", user11.getScreenName()));

        // 1L: New followers list should be saved
        verify(userService, times(1)).saveFollowers(1L, followers);

        // 6L: New followers list should be saved
        verify(userService, times(1)).saveFollowers(6L, followers);
    }

}
