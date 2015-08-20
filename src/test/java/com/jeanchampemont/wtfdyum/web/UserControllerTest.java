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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.common.base.Optional;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.service.UserService;

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
    private UserService userService;

    /** The authentication service. */
    @Mock
    private AuthenticationService authenticationService;

    /** The main controller. */
    @InjectMocks
    private UserController userController;

    /**
     * Index test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void indexTest() throws Exception {
        when(authenticationService.getCurrentUserId()).thenReturn(Optional.of(12340L));

        mockMvc.perform(MockMvcRequestBuilders.get("/user")).andExpect(status().isOk())
        .andExpect(view().name("user/index")).andExpect(model().attribute("userId", 12340L));
    }

    /**
     * Logout test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void logoutTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/logout")).andExpect(status().is3xxRedirection())
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
