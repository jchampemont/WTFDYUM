/*
 * Copyright (C) 2018 WTFDYUM
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

import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.AdminService;
import com.jeanchampemont.wtfdyum.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    public AdminServiceImpl(FeatureService featureService) {
        this.featureService = featureService;
    }

    private final FeatureService featureService;

    @Override
    public Map<Feature, Integer> countEnabledFeature(Set<Long> members) {
        Map<Feature, Integer> result = new HashMap<>();
        for (Feature f : Feature.values()) {
            int count = 0;
            for (Long member : members) {
                if (featureService.isEnabled(member, f)) {
                    count++;
                }
            }
            result.put(f, count);
        }
        return result;
    }
}
