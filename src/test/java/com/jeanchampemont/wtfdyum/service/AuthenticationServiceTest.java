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

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Optional;
import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.service.impl.SessionAuthenticationServiceImpl;
import com.jeanchampemont.wtfdyum.utils.SessionProvider;

/**
 * The Class AuthenticationServiceTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class AuthenticationServiceTest {

    /** The session. */
    @Mock
    private HttpSession session;

    /** The session provider. */
    @Mock
    private SessionProvider sessionProvider;

    /** The system under test. */
    private AuthenticationService sut;

    /**
     * Inits the test.
     */
    @Before
    public void ainit() {
        initMocks(this);
        when(sessionProvider.getSession()).thenReturn(session);
        sut = new SessionAuthenticationServiceImpl(sessionProvider);
    }

    /**
     * Authenticate null user id test.
     */
    @Test(expected = NullPointerException.class)
    public void authenticateNullUserIdTest() {
        sut.authenticate(new User(null, "toke", "secrt"));
    }

    /**
     * Authenticate null user test.
     */
    @Test(expected = NullPointerException.class)
    public void authenticateNullUserTest() {
        sut.authenticate(null);
    }

    /**
     * Authenticate test.
     */
    @Test
    public void authenticateTest() {
        sut.authenticate(new User(120L, "tok", "secret"));

        verify(session, times(1)).setAttribute(anyString(), eq(120L));
    }

    /**
     * Gets the current user id authenticated test.
     *
     * @return the current user id authenticated test
     */
    @Test
    public void getCurrentUserIdAuthenticatedTest() {
        when(session.getAttribute(anyString())).thenReturn(144L);
        final Optional<Long> currentUserId = sut.getCurrentUserId();

        assertThat(currentUserId).isNotNull();
        assertThat(currentUserId.isPresent()).isTrue();
        assertThat(currentUserId.get()).isEqualTo(144L);
    }

    /**
     * Gets the current user id not authenticated test.
     *
     * @return the current user id not authenticated test
     */
    @Test
    public void getCurrentUserIdNotAuthenticatedTest() {
        when(session.getAttribute(anyString())).thenReturn(null);
        final Optional<Long> currentUserId = sut.getCurrentUserId();

        assertThat(currentUserId).isNotNull();
        assertThat(currentUserId.isPresent()).isFalse();
    }

    /**
     * Checks if is authenticated authenticated test.
     */
    @Test
    public void isAuthenticatedAuthenticatedTest() {
        when(session.getAttribute(anyString())).thenReturn(144L);
        final Boolean isAuthenticated = sut.isAuthenticated();

        assertThat(isAuthenticated).isNotNull();
        assertThat(isAuthenticated).isTrue();
    }

    /**
     * Checks if is authenticated not authenticated test.
     */
    @Test
    public void isAuthenticatedNotAuthenticatedTest() {
        when(session.getAttribute(anyString())).thenReturn(null);
        final Boolean isAuthenticated = sut.isAuthenticated();

        assertThat(isAuthenticated).isNotNull();
        assertThat(isAuthenticated).isFalse();
    }

    /**
     * Log out test.
     */
    @Test
    public void logOutTest() {
        sut.logOut();
        verify(session, times(1)).removeAttribute(anyString());
    }
}
