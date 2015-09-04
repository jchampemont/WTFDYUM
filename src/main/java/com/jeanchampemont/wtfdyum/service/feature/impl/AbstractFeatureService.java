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
package com.jeanchampemont.wtfdyum.service.feature.impl;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.feature.FeatureService;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;

/**
 * The Class AbstractFeatureService .
 */
public abstract class AbstractFeatureService implements FeatureService {

	/** The Constant FEATURES_KEY_PREFIX. */
	private static final String FEATURES_KEY_PREFIX = "FEATURES_";

	/** The feature redis template. */
	@Autowired
	private RedisTemplate<String, Feature> featureRedisTemplate;

	/** The feature. */
	private final Feature feature;

	/**
	 * Instantiates a new abstract feature service.
	 *
	 * @param feature
	 *            the feature
	 */
	public AbstractFeatureService(Feature feature) {
		this.feature = feature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeatureService#enableFeature(
	 * java.lang.Long)
	 */
	@Override
	public boolean enableFeature(Long userId) {
		return featureRedisTemplate.opsForSet().add(featuresKey(userId), feature) == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeatureService#disableFeature(
	 * java.lang.Long)
	 */
	@Override
	public boolean disableFeature(Long userId) {
		return featureRedisTemplate.opsForSet().remove(featuresKey(userId), feature) == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeatureService#cron(java.lang.
	 * Long)
	 */
	@Override
	public Set<Event> cron(Long userId) throws WTFDYUMException {
		// Explicitly doing nothing
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeatureService#completeCron(
	 * java.lang.Long)
	 */
	@Override
	public void completeCron(Long userId) throws WTFDYUMException {
		// Explicitly doing nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeatureService#isEnabled(java.
	 * lang.Long)
	 */
	@Override
	public boolean isEnabled(Long userId) {
		return featureRedisTemplate.opsForSet().isMember(featuresKey(userId), feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeatureService#getFeature()
	 */
	@Override
	public Feature getFeature() {
		return feature;
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
