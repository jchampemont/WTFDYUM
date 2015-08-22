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

import java.util.Objects;

/**
 * The Class Principal.
 */
public class Principal {

    /**
     * Instantiates a new user.
     */
    public Principal() {
        // left deliberately empty
    }

    /**
     * Instantiates a new principal.
     *
     * @param userId
     *            the user id
     * @param token
     *            the token
     * @param tokenSecret
     *            the token secret
     */
    public Principal(final Long userId, final String token, final String tokenSecret) {
        this.userId = userId;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    /** The user id. */
    private Long userId;

    /** The token. */
    private String token;

    /** The token secret. */
    private String tokenSecret;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Principal other = (Principal) obj;
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        if (tokenSecret == null) {
            if (other.tokenSecret != null) {
                return false;
            }
        } else if (!tokenSecret.equals(other.tokenSecret)) {
            return false;
        }
        if (userId == null) {
            if (other.userId != null) {
                return false;
            }
        } else if (!userId.equals(other.userId)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the token secret.
     *
     * @return the token secret
     */
    public String getTokenSecret() {
        return tokenSecret;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(token, tokenSecret, userId);
    }

    /**
     * Sets the token.
     *
     * @param token
     *            the new token
     */
    public void setToken(final String token) {
        this.token = token;
    }

    /**
     * Sets the token secret.
     *
     * @param tokenSecret
     *            the new token secret
     */
    public void setTokenSecret(final String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *            the new user id
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
