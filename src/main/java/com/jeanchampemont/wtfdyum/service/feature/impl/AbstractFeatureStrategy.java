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
package com.jeanchampemont.wtfdyum.service.feature.impl;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractFeatureStrategy implements FeatureStrategy {

    private static final String FEATURES_KEY_PREFIX = "FEATURES_";

    public AbstractFeatureStrategy(final Feature feature) {
        this.feature = feature;
    }

    @Autowired
    private RedisTemplate<String, Feature> featureRedisTemplate;

    private final Feature feature;

    @Override
    public void completeCron(final Long userId) throws WTFDYUMException {
        // Explicitly doing nothing
    }

    @Override
    public Set<Event> cron(final Long userId) throws WTFDYUMException {
        // Explicitly doing nothing
        return Collections.emptySet();
    }

    @Override
    public boolean disableFeature(final Long userId) {
        return featureRedisTemplate.opsForSet().remove(featuresKey(userId), feature) == 1;
    }

    @Override
    public boolean enableFeature(final Long userId) {
        return featureRedisTemplate.opsForSet().add(featuresKey(userId), feature) == 1;
    }

    @Override
    public Feature getFeature() {
        return feature;
    }

    @Override
    public boolean isEnabled(final Long userId) {
        return featureRedisTemplate.opsForSet().isMember(featuresKey(userId), feature);
    }

    private String featuresKey(final Long userId) {
        return new StringBuilder(FEATURES_KEY_PREFIX).append(userId.toString()).toString();
    }
}
