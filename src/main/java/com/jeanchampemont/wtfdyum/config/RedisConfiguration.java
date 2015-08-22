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
package com.jeanchampemont.wtfdyum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.utils.LongRedisSerializer;

/**
 * The Class RedisConfiguration.
 */
@Configuration
public class RedisConfiguration {
    /** The env. */
    @Autowired
    private Environment env;

    /**
     * Long redis template.
     *
     * @return the redis template
     */
    @Bean
    public RedisTemplate<String, Long> longRedisTemplate() {
        final RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new LongRedisSerializer());
        template.setValueSerializer(new LongRedisSerializer());
        return template;
    }

    /**
     * Principal redis template.
     *
     * @return the redis template
     */
    @Bean
    public RedisTemplate<String, Principal> principalRedisTemplate() {
        final RedisTemplate<String, Principal> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Principal.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Principal.class));
        return template;
    }

    /**
     * Redis connection factory.
     *
     * @return the redis connection factory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        final JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(env.getProperty("wtfdyum.redis.server"));
        jedisConnectionFactory.setPort(env.getProperty("wtfdyum.redis.port", Integer.class));
        jedisConnectionFactory.setDatabase(env.getProperty("wtfdyum.redis.database", Integer.class));
        return jedisConnectionFactory;
    }
}
