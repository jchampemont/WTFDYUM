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
package com.jeanchampemont.wtfdyum.service;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.feature.FeatureStrategy;
import com.jeanchampemont.wtfdyum.service.impl.FeatureServiceImpl;
import com.jeanchampemont.wtfdyum.service.feature.impl.NotifyUnfollowFeatureStrategy;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class FeatureServiceTest {

	@Mock
    NotifyUnfollowFeatureStrategy notifyUnfollowFeatureService;

	private FeatureService sut;

	@Before
	public void _init() {
		initMocks(this);
		final Map<Feature, FeatureStrategy> featureServices = new HashMap<>();
		featureServices.put(Feature.NOTIFY_UNFOLLOW, notifyUnfollowFeatureService);
		sut = new FeatureServiceImpl(featureServices);
	}

	@Test
	public void completeCronTest() throws WTFDYUMException {
		sut.completeCron(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).completeCron(123L);
	}

	@Test
	public void cronTest() throws WTFDYUMException {
		final Set<Event> expectedResult = new HashSet<>();
		when(notifyUnfollowFeatureService.cron(123L)).thenReturn(expectedResult);

		final Set<Event> result = sut.cron(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).cron(123L);

		assertThat(result).isSameAs(expectedResult);
	}

	@Test
	public void disableFeatureTest() {
		when(notifyUnfollowFeatureService.disableFeature(123L)).thenReturn(false);

		final boolean result = sut.disableFeature(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).disableFeature(123L);

		assertThat(result).isFalse();
	}

	@Test
	public void enableFeatureTest() {
		when(notifyUnfollowFeatureService.enableFeature(123L)).thenReturn(true);

		final boolean result = sut.enableFeature(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).enableFeature(123L);

		assertThat(result).isTrue();
	}

	@Test
	public void isEnabledTest() {
		when(notifyUnfollowFeatureService.isEnabled(123L)).thenReturn(true);

		final boolean result = sut.isEnabled(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).isEnabled(123L);

		assertThat(result).isTrue();
	}
}
