/*
 * Copyright (C) 2015, 2016 WTFDYUM
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

public enum WTFDYUMExceptionType {

    TWITTER_ERROR("Something went wrong while communicating with Twitter..."),

    MEMBER_LIMIT_EXCEEDED("This application cannot accept more members"),

    GET_FOLLOWERS_RATE_LIMIT_EXCEEDED(
            "You have too many followers for this application to work properly. (More than 75.000...)");

    private WTFDYUMExceptionType(final String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }
}
