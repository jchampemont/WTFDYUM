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

import java.util.Objects;

public class Principal {

    public Principal() {
        // left deliberately empty
    }

    public Principal(final Long userId, final String token, final String tokenSecret) {
        this.userId = userId;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    private Long userId;

    private String token;

    private String tokenSecret;

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

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, tokenSecret, userId);
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void setTokenSecret(final String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
