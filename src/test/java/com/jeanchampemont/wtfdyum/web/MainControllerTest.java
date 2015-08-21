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
package com.jeanchampemont.wtfdyum.web;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.service.UserService;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * The Class MainControllerTest.
 */
@RunWith(value = MockitoJUnitRunner.class)
public class MainControllerTest extends AbstractControllerTest {

    /** The twitter service mock. */
    @Mock
    private TwitterService twitterService;

    /** The user service. */
    @Mock
    private UserService userService;

    /** The authentication service. */
    @Mock
    private AuthenticationService authenticationService;

    /** The main controller. */
    @InjectMocks
    private MainController mainController;

    /**
     * Index test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void indexTest() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }

    /**
     * Signin already authenticated test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void signinAlreadyAuthenticatedTest() throws Exception {
        when(authenticationService.isAuthenticated()).thenReturn(true);

        mockMvc.perform(get("/signin")).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));
    }

    /**
     * Signin callback test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void signinCallbackTest() throws Exception {
        final RequestToken returnedRequestToken = new RequestToken("my_super_token", "");

        when(twitterService.signin(anyString())).thenReturn(returnedRequestToken);

        final HttpSession session = mockMvc.perform(get("/signin")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http*://**/**my_super_token")).andReturn().getRequest().getSession();

        assertThat(session).isNotNull();
        assertThat(session.getAttribute(MainController.SESSION_REQUEST_TOKEN)).isNotNull();
        assertThat(session.getAttribute(MainController.SESSION_REQUEST_TOKEN)).isEqualTo(returnedRequestToken);

        final AccessToken returnedAccessToken = new AccessToken("TOken", "secret");
        ReflectionTestUtils.setField(returnedAccessToken, "userId", 1203L);

        when(twitterService.completeSignin(returnedRequestToken, "42")).thenReturn(returnedAccessToken);

        mockMvc.perform(get("/signin/callback?oauth_verifier=42").session((MockHttpSession) session))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user"));

        final Principal builtUser = new Principal(1203L, "TOken", "secret");

        verify(userService, times(1)).saveUpdate(builtUser);
        verify(authenticationService, times(1)).authenticate(builtUser);
    }

    /**
     * Signin test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void signinTest() throws Exception {
        final RequestToken returnedRequestToken = new RequestToken("my_super_token", "");

        when(twitterService.signin(anyString())).thenReturn(returnedRequestToken);

        mockMvc.perform(get("/signin")).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("http*://**/**my_super_token"));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.web.AbstractControllerTest#getTestedController
     * ()
     */
    @Override
    protected Object getTestedController() {
        return mainController;
    }
}
