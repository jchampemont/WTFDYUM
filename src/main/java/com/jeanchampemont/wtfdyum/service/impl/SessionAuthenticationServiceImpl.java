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
package com.jeanchampemont.wtfdyum.service.impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.utils.SessionProvider;

/**
 * The Class SessionAuthenticationServiceImpl.
 */
@Service
public class SessionAuthenticationServiceImpl implements AuthenticationService {

    /** The Constant CURRENT_USER_ID. */
    private static final String CURRENT_USER_ID = "CURRENT_USER_ID";

    /**
     * Instantiates a new session authentication service impl.
     *
     * @param sessionProvider
     *            the session provider
     */
    @Autowired
    public SessionAuthenticationServiceImpl(final SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    /** The session provider. */
    private final SessionProvider sessionProvider;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.AuthenticationService#authenticate(com
     * .jeanchampemont.wtfdyum.dto.User)
     */
    @Override
    public Long authenticate(final User user) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(user.getUserId());

        session().setAttribute(CURRENT_USER_ID, user.getUserId());
        return user.getUserId();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.AuthenticationService#getCurrentUserId
     * ()
     */
    @Override
    public Optional<Long> getCurrentUserId() {
        return Optional.fromNullable((Long) session().getAttribute(CURRENT_USER_ID));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeanchampemont.wtfdyum.service.AuthenticationService#isAuthenticated(
     * )
     */
    @Override
    public Boolean isAuthenticated() {
        return getCurrentUserId().isPresent();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jeanchampemont.wtfdyum.service.AuthenticationService#logOut()
     */
    @Override
    public void logOut() {
        session().removeAttribute(CURRENT_USER_ID);
    }

    /**
     * Session.
     *
     * @return the http session
     */
    private HttpSession session() {
        return sessionProvider.getSession();
    }
}
