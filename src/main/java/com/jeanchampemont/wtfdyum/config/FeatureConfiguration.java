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
package com.jeanchampemont.wtfdyum.config;

import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring configuration for feature strategies beans
 */
@Configuration
public class FeatureConfiguration {

    @Bean(name = "featureStrategies")
    public Map<Feature, FeatureStrategy> featureStrategies(final List<FeatureStrategy> featureStrategies) {
        final Map<Feature, FeatureStrategy> result = new HashMap<>();
        for (final FeatureStrategy featureStrategy : featureStrategies) {
            result.put(featureStrategy.getFeature(), featureStrategy);
        }
        return result;
    }
}
