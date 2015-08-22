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

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.service.UserService;

/**
 * The Class UserServiceImpl.
 */
@Service
public class UserServiceImpl implements UserService {

    /** The Constant EVENTS_KEY_PREFIX. */
    private static final String EVENTS_KEY_PREFIX = "EVENTS_";

    /** The Constant FEATURES_KEY_PREFIX. */
    private static final String FEATURES_KEY_PREFIX = "FEATURES_";

    /** The Constant FOLLOWERS_KEY_PREFIX. */
    private static final String FOLLOWERS_KEY_PREFIX = "FOLLOWERS_";

    /** The Constant TEMP_FOLLOWERS_KEY_PREFIX. */
    private static final String TEMP_FOLLOWERS_KEY_PREFIX = "TEMP_FOLLOWERS_";

    /**
     * Instantiates a new user service impl.
     *
     * @param eventRedisTemplate
     *            the event redis template
     * @param featureRedisTemplate
     *            the feature redis template
     * @param longRedisTemplate
     *            the long redis template
     */
    @Autowired
    public UserServiceImpl(final RedisTemplate<String, Event> eventRedisTemplate,
            final RedisTemplate<String, Feature> featureRedisTemplate,
            final RedisTemplate<String, Long> longRedisTemplate) {
        this.eventRedisTemplate = eventRedisTemplate;
        this.featureRedisTemplate = featureRedisTemplate;
        this.longRedisTemplate = longRedisTemplate;
    }

    /** The event redis template. */
    private final RedisTemplate<String, Event> eventRedisTemplate;

    /** The feature redis template. */
    private final RedisTemplate<String, Feature> featureRedisTemplate;

    /** The long redis template. */
    private final RedisTemplate<String, Long> longRedisTemplate;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.UserService#addEvent(java.lang.Long,
     * com.jeanchampemont.wtfdyum.dto.EventType, java.lang.String)
     */
    @Override
    public void addEvent(final Long userId, final Event event) {
        eventRedisTemplate.opsForList().leftPush(eventsKey(userId), event);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.UserService#disableFeature(java.lang.
     * Long, com.jeanchampemont.wtfdyum.dto.Feature)
     */
    @Override
    public boolean disableFeature(final Long userId, final Feature feature) {
        return featureRedisTemplate.opsForSet().remove(featuresKey(userId), feature) == 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.UserService#enableFeature(java.lang.
     * Long, com.jeanchampemont.wtfdyum.dto.Feature)
     */
    @Override
    public boolean enableFeature(final Long userId, final Feature feature) {
        return featureRedisTemplate.opsForSet().add(featuresKey(userId), feature) == 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.UserService#getRecentEvents(java.lang.
     * Long, int)
     */
    @Override
    public List<Event> getRecentEvents(final Long userId, final int count) {
        return eventRedisTemplate.opsForList().range(eventsKey(userId), 0, count);
    }

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
     * com.jeanchampemont.wtfdyum.service.UserService#isFeatureEnabled(java.lang
     * .Long, com.jeanchampemont.wtfdyum.dto.Feature)
     */
    @Override
    public boolean isFeatureEnabled(final Long userId, final Feature feature) {
        return featureRedisTemplate.opsForSet().isMember(featuresKey(userId), feature);
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
     * Build the Events key.
     *
     * @param userId
     *            the user id
     * @return the string
     */
    private String eventsKey(final Long userId) {
        return new StringBuilder(EVENTS_KEY_PREFIX).append(userId.toString()).toString();
    }

    /**
     * Build the features key.
     *
     * @param userId
     *            the user id
     * @return the string
     */
    private String featuresKey(final Long userId) {
        return new StringBuilder(FEATURES_KEY_PREFIX).append(userId.toString()).toString();
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
