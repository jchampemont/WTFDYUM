/*
 * Copyright (C) 2016 WTFDYUM
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
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(value = MockitoJUnitRunner.class)
public class AjaxControllerTest extends AbstractControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AjaxController ajaxController;

    @Test
    public void recentEventsTest() throws Exception {
        List<Event> events = Arrays.asList(new Event(EventType.FEATURE_ENABLED, "test"), new Event(EventType.FEATURE_ENABLED, "test2"));

        when(authenticationService.getCurrentUserId()).thenReturn(128L);
        when(userService.getRecentEvents(128L, 10, 14)).thenReturn(events);

        mockMvc.perform(get("/ajax/recentEvents/14"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("events", events))
               .andExpect(view().name("user/fragment/events"));
    }

    @Override
    protected Object getTestedController() {
        return ajaxController;
    }
}
