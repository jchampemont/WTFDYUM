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
package com.jeanchampemont.wtfdyum.utils;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * The Class EnumRedisSerializer.
 */
public class EnumRedisSerializer<T extends Enum<T>> implements RedisSerializer<T> {

    /**
     * Instantiates a new enum redis serializer.
     */
    public EnumRedisSerializer(final Class<T> enumType) {
        charset = Charset.forName("UTF8");
        this.enumType = enumType;
    }

    /** The charset. */
    private final Charset charset;

    /** The enum type. */
    private final Class<T> enumType;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.redis.serializer.RedisSerializer#deserialize(
     * byte[])
     */
    @Override
    public T deserialize(final byte[] bytes) throws SerializationException {
        return (bytes == null ? null : Enum.valueOf(enumType, new String(bytes, charset)));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.redis.serializer.RedisSerializer#serialize(java.
     * lang.Object)
     */
    @Override
    public byte[] serialize(final T val) throws SerializationException {
        return (val == null ? null : val.name().getBytes(charset));
    }
}