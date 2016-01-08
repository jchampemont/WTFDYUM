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
package com.jeanchampemont.wtfdyum.web;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.service.UserService;
import com.jeanchampemont.wtfdyum.service.FeatureService;
import com.jeanchampemont.wtfdyum.utils.SessionManager;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMExceptionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(value = MockitoJUnitRunner.class)
public class UserControllerTest extends AbstractControllerTest {

    @Mock
    private TwitterService twitterService;

    @Mock
    private PrincipalService principalService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private FeatureService featureService;

    @InjectMocks
    private UserController userController;

    @Test
    public void disableFeatureTest() throws Exception {
        when(authenticationService.getCurrentUserId()).thenReturn(12340L);
        when(featureService.disableFeature(12340L, Feature.NOTIFY_UNFOLLOW)).thenReturn(true);

        mockMvc
        .perform(get("/user/feature/disable/NOTIFY_UNFOLLOW"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));

        verify(featureService, times(1)).disableFeature(12340L, Feature.NOTIFY_UNFOLLOW);
        verify(userService, times(1)).addEvent(12340L,
                new Event(EventType.FEATURE_DISABLED, Feature.NOTIFY_UNFOLLOW.getShortName()));
    }

    @Test
    public void enableFeatureTest() throws Exception {
        when(authenticationService.getCurrentUserId()).thenReturn(12340L);
        when(featureService.enableFeature(12340L, Feature.NOTIFY_UNFOLLOW)).thenReturn(true);

        mockMvc
        .perform(get("/user/feature/enable/NOTIFY_UNFOLLOW"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));

        verify(featureService, times(1)).enableFeature(12340L, Feature.NOTIFY_UNFOLLOW);
        verify(userService, times(1)).addEvent(12340L,
                new Event(EventType.FEATURE_ENABLED, Feature.NOTIFY_UNFOLLOW.getShortName()));
    }

    @Test
    public void indexTest() throws Exception {
        final Principal principal = new Principal(1L, "tok", "toksec");
        SessionManager.setPrincipal(principal);
        final User u = new User();

        final List<Event> events = Arrays.asList(new Event(), new Event(EventType.REGISTRATION, ""));

        when(authenticationService.getCurrentUserId()).thenReturn(12340L);
        when(twitterService.getUser(principal, 12340L)).thenReturn(u);
        when(userService.getRecentEvents(12340L, 10)).thenReturn(events);
        when(featureService.isEnabled(12340L, Feature.NOTIFY_UNFOLLOW)).thenReturn(true);

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

    @Test
    public void logoutTest() throws Exception {
        mockMvc
        .perform(get("/user/logout"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

        verify(authenticationService, times(1)).logOut();

    }

    @Override
    protected Object getTestedController() {
        return userController;
    }
}
