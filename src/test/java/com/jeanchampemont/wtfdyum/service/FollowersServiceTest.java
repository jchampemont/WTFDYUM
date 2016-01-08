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
import com.jeanchampemont.wtfdyum.service.impl.FollowersServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class FollowersServiceTest {

    private FollowersServiceImpl sut;

    @Mock
    private RedisTemplate<String, Long> longRedisTemplate;

    @Mock
    private SetOperations<String, Long> longSetOperations;

    @Before
    public void _init() {
        initMocks(this);
        sut = new FollowersServiceImpl(longRedisTemplate);
    }

    @Test
    public void getUnfollowersTest() {
        final Set<Long> result = new HashSet<>(Arrays.asList(124L, 901L, 44L));

        when(longRedisTemplate.opsForSet()).thenReturn(longSetOperations);
        when(longSetOperations.difference("FOLLOWERS_12", "TEMP_FOLLOWERS_12")).thenReturn(result);

        final Set<Long> currentFollowersId = new HashSet<>(Arrays.asList(999L, 998L, 997L, 978L));

        final Set<Long> returnedResult = sut.getUnfollowers(12L, currentFollowersId);

        verify(longSetOperations).add("TEMP_FOLLOWERS_12", 978L, 997L, 998L, 999L);
        verify(longRedisTemplate).delete("TEMP_FOLLOWERS_12");

        assertThat(returnedResult).isNotNull();
        assertThat(returnedResult).isEqualTo(result);
    }

    @Test
    public void saveFollowersTest() {
        when(longRedisTemplate.opsForSet()).thenReturn(longSetOperations);

        sut.saveFollowers(1788L, new HashSet<Long>(Arrays.asList(888L, 89L, 19L)));

        verify(longRedisTemplate, times(1)).delete("FOLLOWERS_1788");
        verify(longSetOperations, times(1)).add("FOLLOWERS_1788", 19L, 888L, 89L);
    }
}
