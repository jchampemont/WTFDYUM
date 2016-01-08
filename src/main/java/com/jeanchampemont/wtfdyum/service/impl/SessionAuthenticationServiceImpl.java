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
package com.jeanchampemont.wtfdyum.service.impl;

import com.google.common.base.Preconditions;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.utils.SessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class SessionAuthenticationServiceImpl implements AuthenticationService {

    private static final String CURRENT_USER_ID = "CURRENT_USER_ID";

    @Autowired
    public SessionAuthenticationServiceImpl(final SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    private final SessionProvider sessionProvider;

    @Override
    public Long authenticate(final Principal user) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(user.getUserId());

        session().setAttribute(CURRENT_USER_ID, user.getUserId());
        return user.getUserId();
    }

    @Override
    public Long getCurrentUserId() {
        return (Long) session().getAttribute(CURRENT_USER_ID);
    }

    @Override
    public Boolean isAuthenticated() {
        return getCurrentUserId() != null;
    }

    @Override
    public void logOut() {
        session().removeAttribute(CURRENT_USER_ID);
    }

    private HttpSession session() {
        return sessionProvider.getSession();
    }
}
