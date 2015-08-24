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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
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

    private static final String TWEET_TEXT = "@%s tweet";

    private static final String DM_TEXT = "@%s DM";

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
        sut = new CronServiceImpl(principalService, userService, twitterService, DM_TEXT, TWEET_TEXT);
    }

    /**
     * Check credentials test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void checkCredentialsTest() throws Exception {
        final Principal principal = principal(1L);
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
        final Principal principal = principal(1L);
        when(twitterService.verifyCredentials(principal)).thenReturn(false);

        sut.checkCredentials();

        for (final Feature feature : Feature.values()) {
            verify(userService, times(1)).disableFeature(1L, feature);
        }
        verify(userService, times(1)).addEvent(1L, new Event(EventType.INVALID_TWITTER_CREDENTIALS, ""));
    }

    /**
     * Find unfollowers test disabled.
     *
     * @throws Exception the exception
     */
    @Test
    public void findUnfollowersTestDisabled() throws Exception {
        principal(5L);
        featureEnabled(5L, Feature.NOTIFY_UNFOLLOW, false);

        sut.findUnfollowers();
    }

    /**
     * Find unfollowers test nominal.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void findUnfollowersTestNominal() throws Exception {
        final Principal principal = principal(1L);
        featureEnabled(1L, Feature.NOTIFY_UNFOLLOW, true);

        final Set<Long> followers = followers(principal, (s, f) -> s.thenReturn(f));

        final List<User> unfollowers = unfollowers(principal, followers, (s, u) -> s.thenReturn(u));

        sut.findUnfollowers();

        verifyUnfollowDM(principal, unfollowers.get(0));
        verifyUnfollowDM(principal, unfollowers.get(1));

        // New followers list should be saved
        verify(userService, times(1)).saveFollowers(1L, followers);
    }

    /**
     * Find unfollowers test npe error.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void findUnfollowersTestNPEError() throws Exception {
        final Principal principal = principal(4L);
        featureEnabled(4L, Feature.NOTIFY_UNFOLLOW, true);

        final Set<Long> followers = followers(principal, (s, f) -> s.thenReturn(f));

        // NPE thrown while computing unfollowers
        unfollowers(principal, followers, (s, u) -> s.thenThrow(new NullPointerException()));

        sut.findUnfollowers();

        // should have an event UNKNOWN_ERROR
        verify(userService, times(1)).addEvent(4L, new Event(EventType.UNKNOWN_ERROR, null));
    }

    /**
     * Find unfollowers test rate limit error.
     *
     * @throws Exception the exception
     */
    @Test
    public void findUnfollowersTestRateLimitError() throws Exception {
        final Principal principal = principal(3L);
        featureEnabled(3L, Feature.NOTIFY_UNFOLLOW, true);

        followers(principal,
                (s, f) -> s.thenThrow(new WTFDYUMException(WTFDYUMExceptionType.GET_FOLLOWERS_RATE_LIMIT_EXCEEDED)));

        sut.findUnfollowers();

        // should have an event RATE_LIMIT_EXCEEDED
        verify(userService, times(1)).addEvent(3L, new Event(EventType.RATE_LIMIT_EXCEEDED, null));
    }

    /**
     * Find unfollowers test tweet.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void findUnfollowersTestTweet() throws Exception {
        final Principal principal = principal(6L);
        featureEnabled(6L, Feature.TWEET_UNFOLLOW, true);

        final Set<Long> followers = followers(principal, (s, f) -> s.thenReturn(f));

        final List<User> unfollowers = unfollowers(principal, followers, (s, u) -> s.thenReturn(u));

        sut.findUnfollowers();

        verifyUnfollowTweet(principal, unfollowers.get(0));
        verifyUnfollowTweet(principal, unfollowers.get(1));

        // New followers list should be saved
        verify(userService, times(1)).saveFollowers(6L, followers);
    }

    /**
     * Find unfollowers test twitter error.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void findUnfollowersTestTwitterError() throws Exception {
        final Principal principal = principal(2L);
        featureEnabled(2L, Feature.NOTIFY_UNFOLLOW, true);

        followers(principal, (s, f) -> s.thenThrow(new WTFDYUMException(WTFDYUMExceptionType.TWITTER_ERROR)));

        sut.findUnfollowers();

        // should have an event TWITTER_ERROR
        verify(userService, times(1)).addEvent(2L, new Event(EventType.TWITTER_ERROR, null));
    }

    /**
     * Feature enabled.
     *
     * @param userId
     *            the user id
     * @param feature
     *            the feature
     * @param value
     *            the value
     */
    private void featureEnabled(final long userId, final Feature feature, final boolean value) {
        when(userService.isFeatureEnabled(userId, feature)).thenReturn(value);
    }

    /**
     * Followers.
     *
     * @param principal
     *            the principal
     * @param l
     *            the lambda expression
     * @return the sets of followers ids
     * @throws WTFDYUMException
     *             the WTFDYUM exception
     */
    private Set<Long> followers(final Principal principal, final BiConsumer<OngoingStubbing<Set<Long>>, Set<Long>> l)
            throws WTFDYUMException {
        final Set<Long> followers = new HashSet<>(Arrays.asList(10L, 11L, 12L));
        l.accept(when(twitterService.getFollowers(principal.getUserId(), Optional.ofNullable(principal))), followers);
        return followers;
    }

    /**
     * Principal.
     *
     * @param id
     *            the id
     * @return the principal
     */
    private Principal principal(final long id) {
        when(principalService.getMembers()).thenReturn(new HashSet<>(Arrays.asList(id)));
        final Principal principal = new Principal(id, "Principal 1 Token", "Principal 1 Token Secret");
        when(principalService.get(id)).thenReturn(principal);
        return principal;
    }

    /**
     * Unfollowers.
     *
     * @param principal
     *            the principal
     * @param followersIds
     *            the followers ids
     * @param l
     *            the l
     * @return the sets the
     * @throws WTFDYUMException
     *             the WTFDYUM exception
     */
    private List<User> unfollowers(final Principal principal, final Set<Long> followersIds, final BiConsumer<OngoingStubbing<Set<Long>>, Set<Long>> l)
            throws WTFDYUMException {
        final Set<Long> unfollowers = new HashSet<>(Arrays.asList(10L, 11L));
        l.accept(when(userService.getUnfollowers(principal.getUserId(), followersIds)), unfollowers);

        // unfollowers 10 and 11 details :
        final User user10 = new User();
        user10.setId(10L);
        user10.setScreenName("user10");

        final User user11 = new User();
        user11.setId(11L);
        user11.setScreenName("user11");

        when(twitterService.getUser(principal, 10L)).thenReturn(user10);
        when(twitterService.getUser(principal, 11L)).thenReturn(user11);
        return Arrays.asList(user10, user11);
    }

    /**
     * Verify unfollow dm.
     *
     * @param principal
     *            the principal
     * @param unfollower
     *            the unfollower
     * @throws WTFDYUMException
     *             the WTFDYUM exception
     */
    private void verifyUnfollowDM(final Principal principal, final User unfollower) throws WTFDYUMException {
        verify(userService, times(1)).addEvent(principal.getUserId(), new Event(EventType.UNFOLLOW, unfollower.getScreenName()));
        verify(twitterService, times(1)).sendDirectMessage(principal, principal.getUserId(), String.format(
                DM_TEXT, unfollower.getScreenName()));
    }

    /**
     * Verify unfollow tweet.
     *
     * @param principal
     *            the principal
     * @param unfollower
     *            the unfollower
     * @throws WTFDYUMException
     *             the WTFDYUM exception
     */
    private void verifyUnfollowTweet(final Principal principal, final User unfollower) throws WTFDYUMException {
        verify(userService, times(1)).addEvent(principal.getUserId(),
                new Event(EventType.UNFOLLOW, unfollower.getScreenName()));
        verify(twitterService, times(1)).tweet(principal, String.format(TWEET_TEXT, unfollower.getScreenName()));
    }
}
