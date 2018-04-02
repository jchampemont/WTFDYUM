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
package com.jeanchampemont.wtfdyum.service;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.impl.AdminServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class AdminServiceTest {
    @Mock
    private FeatureService featureService;

    private AdminService sut;

    @Before
    public void init() {
        initMocks(this);
        sut = new AdminServiceImpl(featureService);
    }

    @Test
    public void countEnabledFeatureNominalTest() {
        Set<Long> ids = new HashSet<>(Arrays.asList(42L, 45L));
        when(featureService.isEnabled(42L, Feature.NOTIFY_UNFOLLOW)).thenReturn(true);
        when(featureService.isEnabled(45L, Feature.NOTIFY_UNFOLLOW)).thenReturn(true);
        when(featureService.isEnabled(42L, Feature.TWEET_UNFOLLOW)).thenReturn(true);
        when(featureService.isEnabled(45L, Feature.TWEET_UNFOLLOW)).thenReturn(false);

        Map<Feature, Integer> result = sut.countEnabledFeature(ids);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsKeys(Feature.NOTIFY_UNFOLLOW, Feature.TWEET_UNFOLLOW);
        assertThat(result.get(Feature.NOTIFY_UNFOLLOW)).isEqualTo(2);
        assertThat(result.get(Feature.TWEET_UNFOLLOW)).isEqualTo(1);
    }
}
