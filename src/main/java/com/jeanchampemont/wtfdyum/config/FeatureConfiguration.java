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
package com.jeanchampemont.wtfdyum.config;

import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.feature.FeatureService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class FeatureConfiguration.
 */
@Configuration
public class FeatureConfiguration {

    /**
     * Feature services.
     *
     * @param featureServices
     *            the feature services
     * @return the map
     */
    @Bean(name = "featureServices")
    public Map<Feature, FeatureService> featureServices(final List<FeatureService> featureServices) {
        final Map<Feature, FeatureService> result = new HashMap<>();
        for (final FeatureService featureService : featureServices) {
            result.put(featureService.getFeature(), featureService);
        }
        return result;
    }
}
