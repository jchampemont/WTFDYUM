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

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.service.impl.PrincipalServiceImpl;

/**
 * The Class PrincipalServiceTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class PrincipalServiceTest {

    /** The system under test. */
    private PrincipalService sut;

    /** The redis template mock. */
    @Mock
    private RedisTemplate<String, Serializable> redisTemplate;

    /** The value operations. */
    @Mock
    private ValueOperations<String, Serializable> valueOperations;

    /**
     * Inits the test.
     */
    @Before
    public void ainit() {
        initMocks(this);
        sut = new PrincipalServiceImpl(redisTemplate);
    }

    /**
     * Save update null user id test.
     */
    @Test(expected = NullPointerException.class)
    public void saveUpdateNullUserIdTest() {
        sut.saveUpdate(new Principal(null, "lkdf", "sec"));
    }

    /**
     * Save update null user test.
     */
    @Test(expected = NullPointerException.class)
    public void saveUpdateNullUserTest() {
        sut.saveUpdate(null);
    }

    /**
     * Save update test.
     */
    @Test
    public void saveUpdateTest() {
        final Principal u = new Principal(12L, "tokdf", "secrrr");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        sut.saveUpdate(u);

        verify(valueOperations, times(1)).set("12", u);
    }
}
