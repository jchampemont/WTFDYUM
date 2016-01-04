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

import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.service.UserService;
import com.jeanchampemont.wtfdyum.service.feature.FeaturesService;
import com.jeanchampemont.wtfdyum.utils.SessionManager;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMExceptionType;

/**
 * The Class UserControllerTest.
 */
@RunWith(value = MockitoJUnitRunner.class)
public class UserControllerTest extends AbstractControllerTest {

    /** The twitter service mock. */
    @Mock
    private TwitterService twitterService;

    /** The user service. */
    @Mock
    private PrincipalService principalService;

    /** The authentication service. */
    @Mock
    private AuthenticationService authenticationService;

    /** The user service. */
    @Mock
    private UserService userService;
    
    /** The features service. */
    @Mock
    private FeaturesService featuresService;

    /** The main controller. */
    @InjectMocks
    private UserController userController;

    /**
     * Disable feature test.
     *
     * @throws Exception the exception
     */
    @Test
    public void disableFeatureTest() throws Exception {
        when(authenticationService.getCurrentUserId()).thenReturn(12340L);
        when(featuresService.disableFeature(12340L, Feature.NOTIFY_UNFOLLOW)).thenReturn(true);

        mockMvc
        .perform(get("/user/feature/disable/NOTIFY_UNFOLLOW"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));

        verify(featuresService, times(1)).disableFeature(12340L, Feature.NOTIFY_UNFOLLOW);
        verify(userService, times(1)).addEvent(12340L,
                new Event(EventType.FEATURE_DISABLED, Feature.NOTIFY_UNFOLLOW.getShortName()));
    }

    /**
     * Enable feature test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void enableFeatureTest() throws Exception {
        when(authenticationService.getCurrentUserId()).thenReturn(12340L);
        when(featuresService.enableFeature(12340L, Feature.NOTIFY_UNFOLLOW)).thenReturn(true);

        mockMvc
        .perform(get("/user/feature/enable/NOTIFY_UNFOLLOW"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));

        verify(featuresService, times(1)).enableFeature(12340L, Feature.NOTIFY_UNFOLLOW);
        verify(userService, times(1)).addEvent(12340L,
                new Event(EventType.FEATURE_ENABLED, Feature.NOTIFY_UNFOLLOW.getShortName()));
    }

    /**
     * Index test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void indexTest() throws Exception {
        final Principal principal = new Principal(1L, "tok", "toksec");
        SessionManager.setPrincipal(principal);
        final User u = new User();

        final List<Event> events = Arrays.asList(new Event(), new Event(EventType.REGISTRATION, ""));

        when(authenticationService.getCurrentUserId()).thenReturn(12340L);
        when(twitterService.getUser(principal, 12340L)).thenReturn(u);
        when(userService.getRecentEvents(12340L, 10)).thenReturn(events);
        when(featuresService.isEnabled(12340L, Feature.NOTIFY_UNFOLLOW)).thenReturn(true);

        mockMvc
        .perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/index"))
        .andExpect(model().attribute("user", u))
        .andExpect(model().attribute("events", events))
        .andExpect(model().attribute("availableFeatures", Feature.values()))
        .andExpect(
                model().attribute("featuresStatus", hasEntry(Feature.NOTIFY_UNFOLLOW.name(), true)));
    }

    /**
     * Index test twitter error exception.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void indexTestTwitterErrorException() throws Exception {
        final Principal principal = new Principal(1L, "tok", "toksec");
        SessionManager.setPrincipal(principal);

        when(authenticationService.getCurrentUserId()).thenReturn(12340L);
        when(twitterService.getUser(principal, 12340L))
        .thenThrow(new WTFDYUMException(WTFDYUMExceptionType.TWITTER_ERROR));

        mockMvc
        .perform(get("/user"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

        verify(authenticationService, times(1)).logOut();
    }

    /**
     * Logout test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void logoutTest() throws Exception {
        mockMvc
        .perform(get("/user/logout"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

        verify(authenticationService, times(1)).logOut();

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
        return userController;
    }
}
