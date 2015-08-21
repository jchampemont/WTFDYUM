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
package com.jeanchampemont.wtfdyum.dto;

/**
 * The Class User.
 */
public class User {

    /** The id. */
    private long id;

    /** The name. */
    private String name;

    /** The screen name. */
    private String screenName;

    /** The profile image URL. */
    private String profileImageUrl;

    /** The url. */
    private String URL;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the profile image URL.
     *
     * @return the profile image URL
     */
    public String getProfileImageURL() {
        return profileImageUrl;
    }

    /**
     * Gets the screen name.
     *
     * @return the screen name
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Gets the URL.
     *
     * @return the URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the profile image URL.
     *
     * @param profileImageUrl
     *            the new profile image URL
     */
    public void setProfileImageURL(final String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * Sets the screen name.
     *
     * @param screenName
     *            the new screen name
     */
    public void setScreenName(final String screenName) {
        this.screenName = screenName;
    }

    /**
     * Sets the URL.
     *
     * @param url
     *            the new url
     */
    public void setURL(final String url) {
        this.URL = url;
    }
}
