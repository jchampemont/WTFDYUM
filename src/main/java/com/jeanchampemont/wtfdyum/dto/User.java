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

public class User {

    private long id;

    private String name;

    private String screenName;

    private String profileImageUrl;

    private String URL;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageURL() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getURL() {
        return URL;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setProfileImageURL(final String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setScreenName(final String screenName) {
        this.screenName = screenName;
    }

    public void setURL(final String url) {
        this.URL = url;
    }
}
