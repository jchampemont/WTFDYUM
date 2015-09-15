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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.jeanchampemont.wtfdyum.dto.Principal;
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
     * Gets the followers of the specified userId.
     *
     * If the principal is present, the request is made on the behalf of
     * principal. Else, the request is made on the behalf of the application
     *
     * @param userId
     *            the user id
     * @param principal
     *            the principal
     * @return the followers
     * @throws WTFDYUMException
     *             the WTFDYUM exception
     */
    Set<Long> getFollowers(Long userId, Optional<Principal> principal) throws WTFDYUMException;

    /**
     * Gets the user.
     *
     * @param principal
     *            the principal
     * @param id
     *            the id
     * @return the user
     * @throws WTFDYUMException
     *             the WTFDYUM exception
     */
    User getUser(Principal principal, Long id) throws WTFDYUMException;

    /**
     * Gets the users.
     *
     * @param principal
     *            the principal
     * @param ids
     *            the ids
     * @return the users
     * @throws WTFDYUMException
     */
    List<User> getUsers(Principal principal, long... ids) throws WTFDYUMException;

    /**
     * Send direct message.
     *
     * @param principal
     *            the principal
     * @param toUserId
     *            the to user id
     * @param text
     *            the text
     * @throws WTFDYUMException
     *             the WTFDYUM exception
     */
    void sendDirectMessage(Principal principal, Long toUserId, String text) throws WTFDYUMException;

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

    /**
     * Tweet.
     *
     * @param principal the principal
     * @param text the text
     * @throws WTFDYUMException the WTFDYUM exception
     */
    void tweet(Principal principal, String text) throws WTFDYUMException;

    /**
     * Verify credentials.
     *
     * @param principal
     *            the principal
     * @return true, if successful
     */
    boolean verifyCredentials(Principal principal);
}