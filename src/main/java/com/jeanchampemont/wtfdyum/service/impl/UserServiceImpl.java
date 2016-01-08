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
package com.jeanchampemont.wtfdyum.service.impl;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.dto.type.UserLimitType;
import com.jeanchampemont.wtfdyum.service.FeatureService;
import com.jeanchampemont.wtfdyum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final String EVENTS_KEY_PREFIX = "EVENTS_";

    private static final String FEATURES_KEY_PREFIX = "FEATURES_";

    @Autowired
    public UserServiceImpl(final RedisTemplate<String, Event> eventRedisTemplate,
            final RedisTemplate<String, Feature> featureRedisTemplate,
            final RedisTemplate<String, Long> longRedisTemplate,
            final FeatureService featureService,
            final Clock clock) {
        this.eventRedisTemplate = eventRedisTemplate;
        this.featureRedisTemplate = featureRedisTemplate;
        this.longRedisTemplate = longRedisTemplate;
        this.featureService = featureService;
        this.clock = clock;
    }

    private final RedisTemplate<String, Event> eventRedisTemplate;

    private final RedisTemplate<String, Feature> featureRedisTemplate;

    private final RedisTemplate<String, Long> longRedisTemplate;

    private final FeatureService featureService;

    private final Clock clock;

    @Override
    public void addEvent(final Long userId, final Event event) {
        event.setCreationDateTime(LocalDateTime.now(clock));
        eventRedisTemplate.opsForList().leftPush(eventsKey(userId), event);
    }

    @Override
    public boolean applyLimit(final Long userId, final UserLimitType type) {
        final boolean reached = longRedisTemplate.opsForValue().increment(limitKey(userId, type), 1L) >= type
                .getLimitValue();
        if (reached) {
            for (final Feature f : Feature.values()) {
                featureService.disableFeature(userId, f);
            }
            addEvent(userId, new Event(EventType.CREDENTIALS_INVALID_LIMIT_REACHED, ""));
        }
        return reached;
    }

    @Override
    public Set<Feature> getEnabledFeatures(final Long userId) {
        return featureRedisTemplate.opsForSet().members(featuresKey(userId));
    }

    @Override
    public List<Event> getRecentEvents(final Long userId, final int count) {
        return eventRedisTemplate.opsForList().range(eventsKey(userId), 0, count);
    }

    @Override
    public void resetLimit(final Long userId, final UserLimitType type) {
        longRedisTemplate.delete(limitKey(userId, type));
    }

    private String eventsKey(final Long userId) {
        return new StringBuilder(EVENTS_KEY_PREFIX).append(userId.toString()).toString();
    }

    private String featuresKey(final Long userId) {
        return new StringBuilder(FEATURES_KEY_PREFIX).append(userId.toString()).toString();
    }

    private String limitKey(final Long userId, final UserLimitType type) {
        return new StringBuilder(type.name()).append("_").append(userId.toString()).toString();
    }
}
