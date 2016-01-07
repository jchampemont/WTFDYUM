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

/**
 * The Class AbstractFeatureStrategy .
 */
public abstract class AbstractFeatureStrategy implements FeatureStrategy {

    /** The Constant FEATURES_KEY_PREFIX. */
    private static final String FEATURES_KEY_PREFIX = "FEATURES_";

    /**
     * Instantiates a new abstract feature strategy.
     *
     * @param feature
     *            the feature
     */
    public AbstractFeatureStrategy(final Feature feature) {
        this.feature = feature;
    }

    /** The feature redis template. */
    @Autowired
    private RedisTemplate<String, Feature> featureRedisTemplate;

    /** The feature. */
    private final Feature feature;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy#completeCron(
     * java.lang.Long)
     */
    @Override
    public void completeCron(final Long userId) throws WTFDYUMException {
        // Explicitly doing nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy#cron(java.lang.
     * Long)
     */
    @Override
    public Set<Event> cron(final Long userId) throws WTFDYUMException {
        // Explicitly doing nothing
        return Collections.emptySet();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy#disableFeature(
     * java.lang.Long)
     */
    @Override
    public boolean disableFeature(final Long userId) {
        return featureRedisTemplate.opsForSet().remove(featuresKey(userId), feature) == 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy#enableFeature(
     * java.lang.Long)
     */
    @Override
    public boolean enableFeature(final Long userId) {
        return featureRedisTemplate.opsForSet().add(featuresKey(userId), feature) == 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy#getFeature()
     */
    @Override
    public Feature getFeature() {
        return feature;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy#isEnabled(java.
     * lang.Long)
     */
    @Override
    public boolean isEnabled(final Long userId) {
        return featureRedisTemplate.opsForSet().isMember(featuresKey(userId), feature);
    }

    /**
     * Build the features key.
     *
     * @param userId
     *            the user id
     * @return the string
     */
    private String featuresKey(final Long userId) {
        return new StringBuilder(FEATURES_KEY_PREFIX).append(userId.toString()).toString();
    }
}
