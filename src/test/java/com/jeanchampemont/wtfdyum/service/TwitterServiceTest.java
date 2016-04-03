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
package com.jeanchampemont.wtfdyum.service;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.service.impl.TwitterServiceImpl;
import com.jeanchampemont.wtfdyum.utils.ResponseListMockForTest;
import com.jeanchampemont.wtfdyum.utils.TwitterFactoryHolder;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMExceptionType;
import org.assertj.core.api.Assertions;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import twitter4j.*;
import twitter4j.api.UsersResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class TwitterServiceTest {

    private static final String DEFAULT_BASE_URL = "http://url/toto/";

    private static final String DEFAULT_PATH = "default/path";

    @Autowired
    private Mapper mapper;

    private TwitterService sut;

    @Mock
    private Twitter twitter;

    @Mock
    private UsersResources usersResources;

    @Mock
    private TwitterFactoryHolder twitterFactory;

    @Before
    public void ainit() {
        initMocks(this);
        when(twitterFactory.getInstance()).thenReturn(twitter);
        sut = new TwitterServiceImpl(twitterFactory, mapper, DEFAULT_BASE_URL, "appId", "appSecret");
    }

    @Test
    public void completeSigninTestNominal() throws Exception {
        final AccessToken returnedToken = new AccessToken("TOKTOK", "TOK_secret");

        final RequestToken paramToken = new RequestToken("TOK", "SECRET_tok");
        final String verifier = "VERiFy";

        when(twitter.getOAuthAccessToken(paramToken, verifier)).thenReturn(returnedToken);

        final AccessToken accessToken = sut.completeSignin(paramToken, verifier);

        verify(twitter, times(1)).getOAuthAccessToken(paramToken, verifier);

        assertThat(accessToken).isNotNull();
        assertThat(accessToken).isEqualTo(returnedToken);
    }

    @Test(expected = WTFDYUMException.class)
    public void completeSigninTestTwitterException() throws Exception {
        final RequestToken paramToken = new RequestToken("TOK", "SECRET_tok");
        final String verifier = "VERiFy";

        when(twitter.getOAuthAccessToken(paramToken, verifier)).thenThrow(new TwitterException("dummy"));

        sut.completeSignin(paramToken, verifier);

        Assertions.fail("Exception not throwned");
    }

    @Test
    public void getFollowersTest() throws Exception {
        final Optional<Principal> principal = Optional.of(new Principal(123L, "toktok", "secsecret"));
        final IDs idsMock = mock(IDs.class);
        when(twitter.getFollowersIDs(444L, -1)).thenReturn(idsMock);

        final RateLimitStatus rateLimitStatusMock = mock(RateLimitStatus.class);
        when(idsMock.getRateLimitStatus()).thenReturn(rateLimitStatusMock);

        when(rateLimitStatusMock.getRemaining()).thenReturn(1);

        when(idsMock.getIDs()).thenReturn(new long[]{12L, 34L, 44L, 42L, 42L, 999L});

        final Set<Long> followers = sut.getFollowers(444L, principal);

        assertThat(followers).isNotNull();
        assertThat(followers.contains(12L));
        assertThat(followers.contains(34L));
        assertThat(followers.contains(44L));
        assertThat(followers.contains(42L));
        assertThat(followers.contains(999L));

        verify(twitter, times(1)).setOAuthAccessToken(new AccessToken("toktok", "secsecret"));
    }

    @Test
    public void getFollowersMultiplePageTest() throws Exception {
        final Optional<Principal> principal = Optional.of(new Principal(123L, "toktok", "secsecret"));
        final IDs idsMock = mock(IDs.class);
        when(twitter.getFollowersIDs(444L, -1)).thenReturn(idsMock);

        final RateLimitStatus rateLimitStatusMock = mock(RateLimitStatus.class);
        when(idsMock.getRateLimitStatus()).thenReturn(rateLimitStatusMock);
        when(idsMock.hasNext()).thenReturn(true);

        when(rateLimitStatusMock.getRemaining()).thenReturn(1);

        when(idsMock.getIDs()).thenReturn(new long[]{12L, 34L, 44L, 42L, 42L, 999L});

        when(idsMock.hasNext()).thenReturn(false);

        when(rateLimitStatusMock.getRemaining()).thenReturn(0);

        when(idsMock.getIDs()).thenReturn(new long[]{1001L, 1002L, 1003L});

        final Set<Long> followers = sut.getFollowers(444L, principal);

        assertThat(followers).isNotNull();
        assertThat(followers.contains(12L));
        assertThat(followers.contains(34L));
        assertThat(followers.contains(44L));
        assertThat(followers.contains(42L));
        assertThat(followers.contains(999L));
        assertThat(followers.contains(1001L));
        assertThat(followers.contains(1002L));
        assertThat(followers.contains(1003L));

        verify(twitter, times(1)).setOAuthAccessToken(new AccessToken("toktok", "secsecret"));
    }

    @Test
    public void getFollowersTestWithoutPrincipal() throws Exception {
        final IDs idsMock = mock(IDs.class);
        when(twitter.getFollowersIDs(444L, -1)).thenReturn(idsMock);

        final RateLimitStatus rateLimitStatusMock = mock(RateLimitStatus.class);
        when(idsMock.getRateLimitStatus()).thenReturn(rateLimitStatusMock);

        when(rateLimitStatusMock.getRemaining()).thenReturn(1);

        when(idsMock.getIDs()).thenReturn(new long[]{12L, 34L, 44L, 42L, 42L, 999L});

        final Set<Long> followers = sut.getFollowers(444L, Optional.<Principal> empty());

        assertThat(followers).isNotNull();
        assertThat(followers.contains(12L));
        assertThat(followers.contains(34L));
        assertThat(followers.contains(44L));
        assertThat(followers.contains(42L));
        assertThat(followers.contains(999L));
    }

    @Test
    public void getUsersEmptyTest() throws Exception {
        final List<com.jeanchampemont.wtfdyum.dto.User> result = sut.getUsers(new Principal(1L, "", ""), new long[0]);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void getUsersMultiplePageTest() throws Exception {
        final User userMock = mock(User.class);
        when(userMock.getName()).thenReturn("name");
        when(userMock.getScreenName()).thenReturn("screenName");
        when(userMock.getProfileImageURL()).thenReturn("profile img url");
        when(userMock.getURL()).thenReturn("user url");

        final ResponseList<User> users = new ResponseListMockForTest<User>();
        final long[] ids = new long[150];

        final long[] first100 = new long[100];
        final long[] next50 = new long[50];

        final Random rand = new Random();
        for (int i = 0; i < 150; i++) {
            users.add(userMock);
            final long id = rand.nextLong();

            when(userMock.getId()).thenReturn(id);

            ids[i] = id;
            if (i < 100) {
                first100[i] = id;
            } else {
                next50[i - 100] = id;
            }
        }

        final ResponseList<User> first100Users = new ResponseListMockForTest<>();
        first100Users.addAll(users.subList(0, 100));

        final ResponseList<User> next50Users = new ResponseListMockForTest<>();
        next50Users.addAll(users.subList(100, 150));

        when(twitter.users()).thenReturn(usersResources);
        when(usersResources.lookupUsers(first100)).thenReturn(first100Users);
        when(usersResources.lookupUsers(next50)).thenReturn(next50Users);

        final List<com.jeanchampemont.wtfdyum.dto.User> result = sut.getUsers(new Principal(1L, "", ""), ids);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(150);
    }

    @Test
    public void getUsersTest() throws Exception {
        final User userMock = mock(User.class);
        when(userMock.getName()).thenReturn("name");
        when(userMock.getScreenName()).thenReturn("screenName");
        when(userMock.getProfileImageURL()).thenReturn("profile img url");
        when(userMock.getURL()).thenReturn("user url");

        final ResponseList<User> users = new ResponseListMockForTest<User>();
        final long[] ids = new long[100];

        final Random rand = new Random();
        for(int i = 0; i < 100; i++) {
            users.add(userMock);
            final long id = rand.nextLong();

            when(userMock.getId()).thenReturn(id);
            ids[i] = id;
        }

        when(twitter.users()).thenReturn(usersResources);
        when(usersResources.lookupUsers(ids)).thenReturn(users);

        final List<com.jeanchampemont.wtfdyum.dto.User> result = sut.getUsers(new Principal(1L, "", ""), ids);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(100);
    }

    @Test
    public void getUsersTwitterExceptionTest() throws Exception {
        final User userMock = mock(User.class);
        when(userMock.getName()).thenReturn("name");
        when(userMock.getScreenName()).thenReturn("screenName");
        when(userMock.getProfileImageURL()).thenReturn("profile img url");
        when(userMock.getURL()).thenReturn("user url");

        final ResponseList<User> users = new ResponseListMockForTest<User>();
        final long[] ids = new long[100];

        final Random rand = new Random();
        for(int i = 0; i < 100; i++) {
            users.add(userMock);
            final long id = rand.nextLong();

            when(userMock.getId()).thenReturn(id);
            ids[i] = id;
        }

        when(twitter.users()).thenReturn(usersResources);
        when(usersResources.lookupUsers(ids)).thenThrow(TwitterException.class);

        try {
            sut.getUsers(new Principal(1L, "", ""), ids);
            Assertions.failBecauseExceptionWasNotThrown(WTFDYUMException.class);
        } catch (WTFDYUMException e) {
            assertThat(e.getType()).isEqualTo(WTFDYUMExceptionType.TWITTER_ERROR);
        }
    }

    @Test
    public void getFollowersTestWithoutPrincipalRateLimit() throws Exception {
        final IDs idsMock = mock(IDs.class);
        when(twitter.getFollowersIDs(444L, -1)).thenReturn(idsMock);

        final RateLimitStatus rateLimitStatusMock = mock(RateLimitStatus.class);
        when(idsMock.hasNext()).thenReturn(true);
        when(idsMock.getRateLimitStatus()).thenReturn(rateLimitStatusMock);

        when(rateLimitStatusMock.getRemaining()).thenReturn(0);

        try {
            sut.getFollowers(444L, Optional.<Principal> empty());
            Assertions.failBecauseExceptionWasNotThrown(WTFDYUMException.class);
        } catch (final WTFDYUMException e) {
            assertThat(e.getType()).isEqualTo(WTFDYUMExceptionType.GET_FOLLOWERS_RATE_LIMIT_EXCEEDED);
        }
    }

    @Test
    public void getFollowersTestWithoutPrincipalTwitterException() throws Exception {
        when(twitter.getFollowersIDs(444L, -1)).thenThrow(new TwitterException("msg"));

        try {
            sut.getFollowers(444L, Optional.<Principal> empty());
            Assertions.failBecauseExceptionWasNotThrown(WTFDYUMException.class);
        } catch (final WTFDYUMException e) {
            assertThat(e.getType()).isEqualTo(WTFDYUMExceptionType.TWITTER_ERROR);
        }

    }

    @Test
    public void getUserTest() throws Exception {
        final User userMock = mock(User.class);

        when(twitter.users()).thenReturn(usersResources);
        when(usersResources.showUser(123L)).thenReturn(userMock);
        when(userMock.getId()).thenReturn(123L);
        when(userMock.getName()).thenReturn("name");
        when(userMock.getScreenName()).thenReturn("screenName");
        when(userMock.getProfileImageURL()).thenReturn("profile img url");
        when(userMock.getURL()).thenReturn("user url");

        final com.jeanchampemont.wtfdyum.dto.User result = sut.getUser(new Principal(1L, "", ""), 123L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(123L);
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getScreenName()).isEqualTo("screenName");
        assertThat(result.getProfileImageURL()).isEqualTo("profile img url");
        assertThat(result.getURL()).isEqualTo("user url");
    }

    @Test(expected = WTFDYUMException.class)
    public void getUserTestException() throws Exception {
        when(twitter.users()).thenReturn(usersResources);
        when(usersResources.showUser(123L)).thenThrow(new TwitterException("msg"));

        sut.getUser(new Principal(1L, "", ""), 123L);
    }

    @Test
    public void sendDirectMessageTest() throws Exception {
        final Principal principal = new Principal(123L, "toktok", "secsecret");
        sut.sendDirectMessage(principal, 555L, "text");

        verify(twitter, times(1)).sendDirectMessage(555L, "text");
    }

    @Test
    public void sendDirectMessageTestException() throws Exception {
        when(twitter.sendDirectMessage(444L, "text")).thenThrow(new TwitterException("msg"));

        try {
            sut.sendDirectMessage(new Principal(412L, "", ""), 444L, "text");
            Assertions.failBecauseExceptionWasNotThrown(WTFDYUMException.class);
        } catch (final WTFDYUMException e) {
            assertThat(e.getType()).isEqualTo(WTFDYUMExceptionType.TWITTER_ERROR);
        }
    }

    @Test
    public void signinTestNominal() throws Exception {
        final RequestToken returnedToken = new RequestToken("TOK", "SECRET_tok");

        when(twitter.getOAuthRequestToken(DEFAULT_BASE_URL + DEFAULT_PATH)).thenReturn(returnedToken);

        final RequestToken requestToken = sut.signin(DEFAULT_PATH);

        verify(twitter, times(1)).getOAuthRequestToken(DEFAULT_BASE_URL + DEFAULT_PATH);

        assertThat(requestToken).isNotNull();
        assertThat(requestToken).isEqualTo(returnedToken);
    }

    @Test(expected = WTFDYUMException.class)
    public void signinTestTwitterException() throws Exception {
        when(twitter.getOAuthRequestToken(DEFAULT_BASE_URL + DEFAULT_PATH)).thenThrow(new TwitterException("dummy"));

        sut.signin(DEFAULT_PATH);

        Assertions.fail("Exception not throwned");
    }

    @Test
    public void tweetTest() throws Exception {
        sut.tweet(new Principal(144L, "tok", "toksec"), "my brand new tweet");
        verify(twitter, times(1)).updateStatus("my brand new tweet");
    }

    @Test(expected = WTFDYUMException.class)
    public void tweetTestException() throws Exception {
        when(twitter.updateStatus("my brand new tweet")).thenThrow(new TwitterException(""));
        sut.tweet(new Principal(144L, "tok", "toksec"), "my brand new tweet");
    }

    @Test
    public void verifyCredentialsTestFalse() throws TwitterException {
        when(twitter.verifyCredentials()).thenThrow(new TwitterException(""));
        final boolean result = sut.verifyCredentials(new Principal(12L, "tre", "tr"));
        assertThat(result).isFalse();
    }

    @Test
    public void verifyCredentialsTestTrue() throws TwitterException {
        when(twitter.verifyCredentials()).thenReturn(null);
        final boolean result = sut.verifyCredentials(new Principal(12L, "tre", "tr"));
        assertThat(result).isTrue();
    }
}
