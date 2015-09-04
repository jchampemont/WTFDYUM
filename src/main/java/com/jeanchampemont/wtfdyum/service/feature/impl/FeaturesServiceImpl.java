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

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.feature.FeatureService;
import com.jeanchampemont.wtfdyum.service.feature.FeaturesService;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;

/**
 * The Class FeaturesServiceImpl.
 */
@Service
public class FeaturesServiceImpl implements FeaturesService {

	/**
	 * Instantiates a new features service impl.
	 *
	 * @param featureServices
	 *            the feature services
	 */
	public FeaturesServiceImpl(Map<Feature, FeatureService> featureServices) {
		this.featureServices = featureServices;
	}
	
	/**
	 * Instantiates a new features service impl.
	 */
	public FeaturesServiceImpl() {
		//left deliberately empty
	}

	/** The feature services. */
	@Resource
	private Map<Feature, FeatureService> featureServices;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeaturesService#enableFeature(
	 * java.lang.Long, com.jeanchampemont.wtfdyum.dto.Feature)
	 */
	@Override
	public boolean enableFeature(Long userId, Feature feature) {
		return featureServices.get(feature).enableFeature(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeaturesService#disableFeature
	 * (java.lang.Long, com.jeanchampemont.wtfdyum.dto.Feature)
	 */
	@Override
	public boolean disableFeature(Long userId, Feature feature) {
		return featureServices.get(feature).disableFeature(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeaturesService#isEnabled(java
	 * .lang.Long, com.jeanchampemont.wtfdyum.dto.Feature)
	 */
	@Override
	public boolean isEnabled(Long userId, Feature feature) {
		return featureServices.get(feature).isEnabled(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeaturesService#cron(java.lang
	 * .Long, com.jeanchampemont.wtfdyum.dto.Feature)
	 */
	@Override
	public Set<Event> cron(Long userId, Feature feature) throws WTFDYUMException {
		return featureServices.get(feature).cron(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeanchampemont.wtfdyum.service.feature.FeaturesService#completeCron(
	 * java.lang.Long, com.jeanchampemont.wtfdyum.dto.Feature)
	 */
	@Override
	public void completeCron(Long userId, Feature feature) throws WTFDYUMException {
		featureServices.get(feature).completeCron(userId);
	}

}
