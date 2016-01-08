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
import com.jeanchampemont.wtfdyum.service.impl.SessionAuthenticationServiceImpl;
import com.jeanchampemont.wtfdyum.utils.SessionProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class AuthenticationServiceTest {

    @Mock
    private HttpSession session;

    @Mock
    private SessionProvider sessionProvider;

    private AuthenticationService sut;

    @Before
    public void ainit() {
        initMocks(this);
        when(sessionProvider.getSession()).thenReturn(session);
        sut = new SessionAuthenticationServiceImpl(sessionProvider);
    }

    @Test(expected = NullPointerException.class)
    public void authenticateNullUserIdTest() {
        sut.authenticate(new Principal(null, "toke", "secrt"));
    }

    @Test(expected = NullPointerException.class)
    public void authenticateNullUserTest() {
        sut.authenticate(null);
    }

    @Test
    public void authenticateTest() {
        sut.authenticate(new Principal(120L, "tok", "secret"));

        verify(session, times(1)).setAttribute(anyString(), eq(120L));
    }

    @Test
    public void getCurrentUserIdAuthenticatedTest() {
        when(session.getAttribute(anyString())).thenReturn(144L);
        final Long currentUserId = sut.getCurrentUserId();

        assertThat(currentUserId).isNotNull();
        assertThat(currentUserId).isEqualTo(144L);
    }

    @Test
    public void getCurrentUserIdNotAuthenticatedTest() {
        when(session.getAttribute(anyString())).thenReturn(null);
        final Long currentUserId = sut.getCurrentUserId();

        assertThat(currentUserId).isNull();
    }

    @Test
    public void isAuthenticatedAuthenticatedTest() {
        when(session.getAttribute(anyString())).thenReturn(144L);
        final Boolean isAuthenticated = sut.isAuthenticated();

        assertThat(isAuthenticated).isNotNull();
        assertThat(isAuthenticated).isTrue();
    }

    @Test
    public void isAuthenticatedNotAuthenticatedTest() {
        when(session.getAttribute(anyString())).thenReturn(null);
        final Boolean isAuthenticated = sut.isAuthenticated();

        assertThat(isAuthenticated).isNotNull();
        assertThat(isAuthenticated).isFalse();
    }

    @Test
    public void logOutTest() {
        sut.logOut();
        verify(session, times(1)).removeAttribute(anyString());
    }
}
