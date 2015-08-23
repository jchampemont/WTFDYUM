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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.service.PrincipalService;

/**
 * The Class PrincipalServiceImpl.
 */
@Service
public class PrincipalServiceImpl implements PrincipalService {

    private static final String MEMBERS_KEY = "MEMBERS";

    /**
     * Instantiates a new user service impl.
     *
     * @param principalRedisTemplate
     *            the principal redis template
     * @param longRedisTemplate
     *            the long redis template
     */
    @Autowired
    public PrincipalServiceImpl(final RedisTemplate<String, Principal> principalRedisTemplate,
            final RedisTemplate<String, Long> longRedisTemplate) {
        this.principalRedisTemplate = principalRedisTemplate;
        this.longRedisTemplate = longRedisTemplate;
    }

    /** The principal redis template. */
    private final RedisTemplate<String, Principal> principalRedisTemplate;

    /** The long redis template. */
    private final RedisTemplate<String, Long> longRedisTemplate;

    /*
     * (non-Javadoc)
     * 
     * @see com.jeanchampemont.wtfdyum.service.PrincipalService#countMembers()
     */
    @Override
    public int countMembers() {
        return longRedisTemplate.opsForSet().size(MEMBERS_KEY).intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.PrincipalService#get(java.lang.Long)
     */
    @Override
    public Principal get(final Long id) {
        Preconditions.checkNotNull(id);
        return principalRedisTemplate.opsForValue().get(id.toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jeanchampemont.wtfdyum.service.PrincipalService#saveUpdate(com.
     * jeanchampemont.wtfdyum.dto.User)
     */
    @Override
    public void saveUpdate(final Principal user) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(user.getUserId());

        principalRedisTemplate.opsForValue().set(user.getUserId().toString(), user);
        longRedisTemplate.opsForSet().add(MEMBERS_KEY, user.getUserId());
    }

}
