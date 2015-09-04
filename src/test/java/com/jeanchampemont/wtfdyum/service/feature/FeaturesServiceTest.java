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
package com.jeanchampemont.wtfdyum.service.feature;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.feature.impl.FeaturesServiceImpl;
import com.jeanchampemont.wtfdyum.service.feature.impl.NotifyUnfollowFeatureService;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;

/**
 * The Class FeaturesServiceTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class FeaturesServiceTest {

	/** The notify unfollow feature service. */
	@Mock
	NotifyUnfollowFeatureService notifyUnfollowFeatureService;

	/** The sut. */
	private FeaturesService sut;

	/**
	 * _init.
	 */
	@Before
	public void _init() {
		initMocks(this);
		final Map<Feature, FeatureService> featureServices = new HashMap<>();
		featureServices.put(Feature.NOTIFY_UNFOLLOW, notifyUnfollowFeatureService);
		sut = new FeaturesServiceImpl(featureServices);
	}

	/**
	 * Complete cron test.
	 *
	 * @throws WTFDYUMException
	 *             the WTFDYUM exception
	 */
	@Test
	public void completeCronTest() throws WTFDYUMException {
		sut.completeCron(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).completeCron(123L);
	}

	/**
	 * Cron test.
	 *
	 * @throws WTFDYUMException
	 *             the WTFDYUM exception
	 */
	@Test
	public void cronTest() throws WTFDYUMException {
		final Set<Event> expectedResult = new HashSet<>();
		when(notifyUnfollowFeatureService.cron(123L)).thenReturn(expectedResult);

		final Set<Event> result = sut.cron(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).cron(123L);

		assertThat(result).isSameAs(expectedResult);
	}

	/**
	 * Disable feature test.
	 */
	@Test
	public void disableFeatureTest() {
		when(notifyUnfollowFeatureService.disableFeature(123L)).thenReturn(false);

		final boolean result = sut.disableFeature(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).disableFeature(123L);

		assertThat(result).isFalse();
	}

	/**
	 * Enable feature test.
	 */
	@Test
	public void enableFeatureTest() {
		when(notifyUnfollowFeatureService.enableFeature(123L)).thenReturn(true);

		final boolean result = sut.enableFeature(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).enableFeature(123L);

		assertThat(result).isTrue();
	}

	/**
	 * Checks if is enabled test.
	 */
	@Test
	public void isEnabledTest() {
		when(notifyUnfollowFeatureService.isEnabled(123L)).thenReturn(true);

		final boolean result = sut.isEnabled(123L, Feature.NOTIFY_UNFOLLOW);

		verify(notifyUnfollowFeatureService, times(1)).isEnabled(123L);

		assertThat(result).isTrue();
	}
}
