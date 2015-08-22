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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.EventType;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.impl.UserServiceImpl;

/**
 * The Class UserServiceTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class UserServiceTest {

    /** The system under test. */
    private UserService sut;

    /** The long redis template mock. */
    @Mock
    private RedisTemplate<String, Long> longRedisTemplate;

    /** The Event redis template mock. */
    @Mock
    private RedisTemplate<String, Event> eventRedisTemplate;

    /** The Feature redis template mock. */
    @Mock
    private RedisTemplate<String, Feature> featureRedisTemplate;

    /** The Long Set operations. */
    @Mock
    private SetOperations<String, Long> longSetOperations;

    /** The Feature Set operations. */
    @Mock
    private SetOperations<String, Feature> featureSetOperations;

    /** The event list operations. */
    @Mock
    private ListOperations<String, Event> eventListOperations;

    /**
     * Inits the test.
     */
    @Before
    public void _init() {
        initMocks(this);
        sut = new UserServiceImpl(eventRedisTemplate, featureRedisTemplate, longRedisTemplate);
    }

    /**
     * Adds the event test.
     */
    @Test
    public void addEventTest() {
        when(eventRedisTemplate.opsForList()).thenReturn(eventListOperations);

        final Event event = new Event(EventType.REGISTRATION, "data");

        sut.addEvent(31L, event);

        verify(eventListOperations, times(1)).leftPush("EVENTS_31", event);
    }

    /**
     * Disable feature test disabled feature.
     */
    @Test
    public void disableFeatureTestDisabledFeature() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.remove("FEATURES_1334", Feature.NOTIFY_UNFOLLOW)).thenReturn(0L);

        final boolean result = sut.disableFeature(1334L, Feature.NOTIFY_UNFOLLOW);

        verify(featureSetOperations, times(1)).remove("FEATURES_1334", Feature.NOTIFY_UNFOLLOW);

        assertThat(result).isFalse();
    }

    /**
     * Disable feature test enabled feature.
     */
    @Test
    public void disableFeatureTestEnabledFeature() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.remove("FEATURES_1334", Feature.NOTIFY_UNFOLLOW)).thenReturn(1L);

        final boolean result = sut.disableFeature(1334L, Feature.NOTIFY_UNFOLLOW);

        verify(featureSetOperations, times(1)).remove("FEATURES_1334", Feature.NOTIFY_UNFOLLOW);

        assertThat(result).isTrue();
    }

    /**
     * Enable feature test disabled feature.
     */
    @Test
    public void enableFeatureTestDisabledFeature() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.add("FEATURES_1334", Feature.NOTIFY_UNFOLLOW)).thenReturn(1L);

        final boolean result = sut.enableFeature(1334L, Feature.NOTIFY_UNFOLLOW);

        verify(featureSetOperations, times(1)).add("FEATURES_1334", Feature.NOTIFY_UNFOLLOW);

        assertThat(result).isTrue();
    }

    /**
     * Enable feature test enabled feature.
     */
    @Test
    public void enableFeatureTestEnabledFeature() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.add("FEATURES_1334", Feature.NOTIFY_UNFOLLOW)).thenReturn(0L);

        final boolean result = sut.enableFeature(1334L, Feature.NOTIFY_UNFOLLOW);

        verify(featureSetOperations, times(1)).add("FEATURES_1334", Feature.NOTIFY_UNFOLLOW);

        assertThat(result).isFalse();
    }

    /**
     * Gets the recent events test.
     *
     * @return the recent events test
     */
    @Test
    public void getRecentEventsTest() {
        final List<Event> result = Arrays.asList(new Event(EventType.REGISTRATION, "reg"),
                new Event(EventType.UNFOLLOW, "unfoll"));

        when(eventRedisTemplate.opsForList()).thenReturn(eventListOperations);
        when(eventListOperations.range("EVENTS_1249", 0, 12)).thenReturn(result);

        final List<Event> returnedResult = sut.getRecentEvents(1249L, 12);

        assertThat(returnedResult).isNotNull();
        assertThat(returnedResult.size()).isEqualTo(2);
        assertThat(returnedResult).isEqualTo(result);
    }

    /**
     * Gets the unfollowers test.
     *
     * @return the unfollowers test
     */
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

    /**
     * Checks if is feature enabled test.
     */
    @Test
    public void isFeatureEnabledTest() {
        when(featureRedisTemplate.opsForSet()).thenReturn(featureSetOperations);
        when(featureSetOperations.isMember("FEATURES_1899", Feature.NOTIFY_UNFOLLOW)).thenReturn(true);

        final boolean featureEnabled = sut.isFeatureEnabled(1899L, Feature.NOTIFY_UNFOLLOW);

        assertThat(featureEnabled).isTrue();
    }

    /**
     * Save followers test.
     */
    @Test
    public void saveFollowersTest() {
        when(longRedisTemplate.opsForSet()).thenReturn(longSetOperations);

        sut.saveFollowers(1788L, new HashSet<Long>(Arrays.asList(888L, 89L, 19L)));

        verify(longRedisTemplate, times(1)).delete("FOLLOWERS_1788");
        verify(longSetOperations, times(1)).add("FOLLOWERS_1788", 19L, 888L, 89L);
    }
}
