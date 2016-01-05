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
package com.jeanchampemont.wtfdyum.service.impl;

import com.jeanchampemont.wtfdyum.service.FollowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * The Class FollowersServiceImpl.
 */
@Service
public class FollowersServiceImpl implements FollowersService {

    /** The Constant FOLLOWERS_KEY_PREFIX. */
    private static final String FOLLOWERS_KEY_PREFIX = "FOLLOWERS_";

    /** The Constant TEMP_FOLLOWERS_KEY_PREFIX. */
    private static final String TEMP_FOLLOWERS_KEY_PREFIX = "TEMP_FOLLOWERS_";

    /**
     * Instantiates a new followers service impl.
     *
     * @param longRedisTemplate
     *            the long redis template
     */
    @Autowired
    public FollowersServiceImpl(final RedisTemplate<String, Long> longRedisTemplate) {
        this.longRedisTemplate = longRedisTemplate;
    }

    /** The long redis template. */
    private final RedisTemplate<String, Long> longRedisTemplate;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.UserService#getUnfollowers(java.lang.
     * Long, java.util.Set)
     */
    @Override
    public Set<Long> getUnfollowers(final Long userId, final Set<Long> currentFollowersId) {
        longRedisTemplate.opsForSet().add(tempFollowersKey(userId),
                currentFollowersId.toArray(new Long[currentFollowersId.size()]));

        final Set<Long> unfollowers = longRedisTemplate.opsForSet().difference(followersKey(userId),
                tempFollowersKey(userId));
        longRedisTemplate.delete(tempFollowersKey(userId));
        return unfollowers;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.UserService#saveFollowers(java.lang.
     * Long, java.util.Set)
     */
    @Override
    public void saveFollowers(final Long userId, final Set<Long> followersId) {
        longRedisTemplate.delete(followersKey(userId));
        longRedisTemplate.opsForSet().add(followersKey(userId), followersId.toArray(new Long[followersId.size()]));
    }

    /**
     * Followers key.
     *
     * @param userId
     *            the user id
     * @return the string
     */
    private String followersKey(final Long userId) {
        return new StringBuilder(FOLLOWERS_KEY_PREFIX).append(userId.toString()).toString();
    }

    /**
     * Temp followers key.
     *
     * @param userId
     *            the user id
     * @return the string
     */
    private String tempFollowersKey(final Long userId) {
        return new StringBuilder(TEMP_FOLLOWERS_KEY_PREFIX).append(userId.toString()).toString();
    }
}
