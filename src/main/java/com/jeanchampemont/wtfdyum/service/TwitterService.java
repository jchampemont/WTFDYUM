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
package com.jeanchampemont.wtfdyum.service;

import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * The Interface TwitterService. This is used to interact with twitter.
 */
public interface TwitterService {

    /**
     * Complete signin.
     *
     * @param requestToken
     *            the request token returned by {@link #signin(String) signin}
     *            method
     * @param verifier
     *            the oauth verifier
     * @return the access token to the user account
     * @throws WTFDYUMException
     *             if something is wrong with Twitter communication
     */
    AccessToken completeSignin(RequestToken requestToken, String verifier) throws WTFDYUMException;

    /**
     * Gets the user.
     *
     * @param id
     *            the id
     * @return the user
     * @throws WTFDYUMException
     */
    User getUser(Long id) throws WTFDYUMException;

    /**
     * Signin with Twitter.
     *
     * @param path
     *            the path of this application where twitter should redirect
     *            after sign-on process.
     * @return the request token
     * @throws WTFDYUMException
     *             if something is wrong with Twitter communication
     */
    RequestToken signin(String path) throws WTFDYUMException;
}