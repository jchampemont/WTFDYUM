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

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;

/**
 * This class should be extended by all test class testing controllers.
 *
 * @author Jean
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
@WebAppConfiguration
public abstract class AbstractControllerTest {

    /** The context. */
    @Autowired
    private WebApplicationContext context;

    /** The mock mvc. */
    protected MockMvc mockMvc;

    /**
     * Setup : - Init mockMvc.
     */
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
