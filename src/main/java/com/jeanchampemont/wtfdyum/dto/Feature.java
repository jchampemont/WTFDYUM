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
package com.jeanchampemont.wtfdyum.dto;

public enum Feature {

    NOTIFY_UNFOLLOW("Send me a direct message when someone stops following me", "unfollow notifications"),

    TWEET_UNFOLLOW("Send a public tweet with @mention when someone stops following me", "unfollow tweet");

    private Feature(final String message, final String shortName) {
        this.message = message;
        this.shortName = shortName;
    }

    private String message;

    private String shortName;

    public String getMessage() {
        return message;
    }

    public String getShortName() {
        return shortName;
    }
}
