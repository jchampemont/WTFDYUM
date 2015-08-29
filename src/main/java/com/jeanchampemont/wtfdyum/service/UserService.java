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
package com.jeanchampemont.wtfdyum.service;

import java.util.List;
import java.util.Set;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.type.UserLimitType;

/**
 * The Interface UserService.
 */
public interface UserService {

    /**
     * Adds the event.
     *
     * @param userId
     *            the user id
     * @param event
     *            the event
     */
    void addEvent(Long userId, Event event);

    /**
     * Apply limit.
     *
     * @param userId
     *            the user id
     * @param type
     *            the type
     * @return true, if limit is exceeded
     */
    boolean applyLimit(Long userId, UserLimitType type);

    /**
     * Disable feature.
     *
     * @param userId
     *            the user id
     * @param feature
     *            the feature
     * @return true, if successful
     */
    boolean disableFeature(Long userId, Feature feature);

    /**
     * Enable feature.
     *
     * @param userId
     *            the user id
     * @param feature
     *            the feature
     * @return true, if successful
     */
    boolean enableFeature(Long userId, Feature feature);

    /**
     * Gets the recent events.
     *
     * @param userId
     *            the user id
     * @param count
     *            the count
     * @return the recent events
     */
    List<Event> getRecentEvents(Long userId, int count);

    /**
     * Gets the unfollowers.
     *
     * @param userId
     *            the user id
     * @param currentFollowersId
     *            the current followers id
     * @return the unfollowers
     */
    Set<Long> getUnfollowers(Long userId, Set<Long> currentFollowersId);

    /**
     * Checks if the specified feature is enabled.
     *
     * @param userId
     *            the user id
     * @param feature
     *            the feature
     * @return true, if is feature enabled
     */
    boolean isFeatureEnabled(Long userId, Feature feature);

    /**
     * Reset limit.
     *
     * @param userId
     *            the user id
     * @param type
     *            the type
     */
    void resetLimit(Long userId, UserLimitType type);

    /**
     * Save followers.
     *
     * @param userId
     *            the user id
     * @param followersId
     *            the followers id
     */
    void saveFollowers(Long userId, Set<Long> followersId);
}
