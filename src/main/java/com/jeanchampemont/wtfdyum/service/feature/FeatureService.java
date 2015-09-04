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

import java.util.Set;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;

/**
 * This interface should be implemented by all feature's services.
 */
public interface FeatureService {

    /**
     * Complete cron.
     *
     * This method is called after all cron for this user have been executed
     *
     * @param userId
     *            the user id
     * @throws WTFDYUMException
     */
    void completeCron(Long userId) throws WTFDYUMException;

    /**
     * Method that should be executed periodically for this feature.
     *
     * @param userId
     *            the user id
     * @return the resulting events set
     * @throws WTFDYUMException
     *             the WTFDYUM exception
     */
    Set<Event> cron(Long userId) throws WTFDYUMException;

    /**
     * Disable the feature for this userId.
     *
     * @param userId
     *            the user id
     * @return true if the feature was enabled and has been disabled, false
     *         otherwise
     */
    boolean disableFeature(Long userId);

    /**
     * Enable the feature for this userId.
     *
     * @param userId
     *            the user id
     * @return true if the feature was disabled and has been enabled, false
     *         otherwise
     */
    boolean enableFeature(Long userId);

    /**
     * Gets the feature.
     *
     * @return the feature
     */
    Feature getFeature();

    /**
     * Checks for cron.
     *
     * @return whether or not this feature has a cron that should be executed
     *         periodically
     */
    boolean hasCron();

    /**
     * Checks if is enabled.
     *
     * @param userId
     *            the user id
     * @return whether or not this feature is enabled
     */
    boolean isEnabled(Long userId);
}
