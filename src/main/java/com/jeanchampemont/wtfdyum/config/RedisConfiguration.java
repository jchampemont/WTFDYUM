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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.utils.EnumRedisSerializer;
import com.jeanchampemont.wtfdyum.utils.LongRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * The Class RedisConfiguration.
 */
@Configuration
public class RedisConfiguration {
    /** The env. */
    @Autowired
    private Environment env;

    /**
     * Event redis template.
     *
     * @return the redis template
     */
    @Bean
    public RedisTemplate<String, Event> eventRedisTemplate() {
        final RedisTemplate<String, Event> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(jsonSerializer(Event.class, objectMapper()));
        template.setValueSerializer(jsonSerializer(Event.class, objectMapper()));
        return template;
    }

    /**
     * Feature redis template.
     *
     * @return the redis template
     */
    @Bean
    public RedisTemplate<String, Feature> featureRedisTemplate() {
        final RedisTemplate<String, Feature> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new EnumRedisSerializer<>(Feature.class));
        template.setValueSerializer(new EnumRedisSerializer<>(Feature.class));
        return template;
    }

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
     * Object mapper.
     *
     * @return the object mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.findAndRegisterModules();
        return result;
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
        template.setHashKeySerializer(jsonSerializer(Principal.class, objectMapper()));
        template.setValueSerializer(jsonSerializer(Principal.class, objectMapper()));
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

    /**
     * Json serializer.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param mapper
     *            the mapper
     * @return the jackson2 json redis serializer
     */
    private <T> Jackson2JsonRedisSerializer<T> jsonSerializer(final Class<T> clazz, final ObjectMapper mapper) {
        final Jackson2JsonRedisSerializer<T> result = new Jackson2JsonRedisSerializer<>(clazz);
        result.setObjectMapper(mapper);
        return result;
    }
}
