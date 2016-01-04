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

import java.util.Set;

/**
 * The Interface FollowersService.
 */
public interface FollowersService {
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
     * Save followers.
     *
     * @param userId
     *            the user id
     * @param followersId
     *            the followers id
     */
    void saveFollowers(Long userId, Set<Long> followersId);
}
