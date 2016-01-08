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
package com.jeanchampemont.wtfdyum.service;

import com.jeanchampemont.wtfdyum.dto.Principal;

public interface AuthenticationService {

    /**
     * Authenticate.
     *
     * @param user
     *            the user
     * @return the connected userId
     */
    Long authenticate(Principal user);

    /**
     * Gets the current user id.
     *
     * @return the current user id
     */
    Long getCurrentUserId();

    /**
     * Checks if the current user is authenticated.
     *
     * @return the boolean
     */
    Boolean isAuthenticated();

    /**
     * Log out.
     */
    void logOut();
}
