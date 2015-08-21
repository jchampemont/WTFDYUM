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

import com.jeanchampemont.wtfdyum.dto.Principal;

/**
 * The Class SessionManager.
 */
public class SessionManager {

    /** The principal. */
    private static ThreadLocal<Principal> principal = new ThreadLocal<>();

    /**
     * Gets the principal.
     *
     * @return the principal
     */
    public static Principal getPrincipal() {
        return principal.get();
    }

    /**
     * Sets the principal.
     *
     * @param principal
     *            the new principal
     */
    public static void setPrincipal(final Principal principal) {
        if (principal == null) {
            SessionManager.principal.remove();
        } else {
            SessionManager.principal.set(principal);
        }
    }
}
