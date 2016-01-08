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
package com.jeanchampemont.wtfdyum.service.impl;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.FeatureService;
import com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Service
public class FeatureServiceImpl implements FeatureService {

    public FeatureServiceImpl() {
        // left deliberately empty
    }

    public FeatureServiceImpl(final Map<Feature, FeatureStrategy> featureStrategies) {
        this.featureStrategies = featureStrategies;
    }

    @Resource
    private Map<Feature, FeatureStrategy> featureStrategies;

    @Override
    public void completeCron(final Long userId, final Feature feature) throws WTFDYUMException {
        featureStrategies.get(feature).completeCron(userId);
    }

    @Override
    public Set<Event> cron(final Long userId, final Feature feature) throws WTFDYUMException {
        return featureStrategies.get(feature).cron(userId);
    }

    @Override
    public boolean disableFeature(final Long userId, final Feature feature) {
        return featureStrategies.get(feature).disableFeature(userId);
    }

    @Override
    public boolean enableFeature(final Long userId, final Feature feature) {
        return featureStrategies.get(feature).enableFeature(userId);
    }

    @Override
    public boolean isEnabled(final Long userId, final Feature feature) {
        return featureStrategies.get(feature).isEnabled(userId);
    }
}
