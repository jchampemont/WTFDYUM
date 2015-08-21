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

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.utils.SessionManager;
import com.jeanchampemont.wtfdyum.utils.TwitterFactoryHolder;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * The Class TwitterServiceImpl. This is used to interact with twitter.
 */
@Service
public class TwitterServiceImpl implements TwitterService {

    /**
     * Instantiates a new twitter service.
     *
     * @param twitterFactory
     *            the twitter factory
     * @param mapper
     *            the mapper
     * @param baseUrl
     *            the base url of WTFDYUM. Property wtfdyum.server-base-url
     * @param appId
     *            the app id
     * @param appSecret
     *            the app secret
     */
    @Autowired
    public TwitterServiceImpl(final TwitterFactoryHolder twitterFactory,
            final Mapper mapper,
            @Value("${wtfdyum.server-base-url}") final String baseUrl,
            @Value("${wtfdyum.twitter.appId}") final String appId,
            @Value("${wtfdyum.twitter.appSecret}") final String appSecret) {
        this.twitterFactory = twitterFactory;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    /** The base url of WTFDYUM. */
    private final String baseUrl;

    /** The twitter. */
    private final TwitterFactoryHolder twitterFactory;

    /** The mapper. */
    private final Mapper mapper;

    /** The app id. */
    private final String appId;

    /** The app secret. */
    private final String appSecret;

    /*
     * (non-Javadoc)
     *
     * @see com.jeanchampemont.wtfdyum.service.TwitterService#completeSignin(
     * twitter4j.auth.RequestToken, java.lang.String)
     */
    @Override
    public AccessToken completeSignin(final RequestToken requestToken, final String verifier) throws WTFDYUMException {
        AccessToken token = null;
        try {
            token = twitter().getOAuthAccessToken(requestToken, verifier);
        } catch (final TwitterException e) {
            throw new WTFDYUMException(e);
        }
        return token;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jeanchampemont.wtfdyum.service.TwitterService#getUser(java.lang.Long)
     */
    @Override
    public User getUser(final Long id) throws WTFDYUMException {
        User result = null;
        try {
            final twitter4j.User user = twitter(SessionManager.getPrincipal()).users().showUser(id);
            result = mapper.map(user, User.class);
        } catch (final TwitterException e) {
            throw new WTFDYUMException(e);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jeanchampemont.wtfdyum.service.TwitterService#signin(java.lang.
     * String)
     */
    @Override
    public RequestToken signin(final String path) throws WTFDYUMException {
        RequestToken token = null;
        try {
            token = twitter().getOAuthRequestToken(new StringBuilder(baseUrl).append(path).toString());
        } catch (final TwitterException e) {
            throw new WTFDYUMException(e);
        }
        return token;
    }

    /**
     * Twitter.
     *
     * @return the twitter
     */
    private Twitter twitter() {
        final Twitter instance = twitterFactory.getInstance();
        instance.setOAuthConsumer(appId, appSecret);
        return instance;
    }

    /**
     * Twitter.
     *
     * @param principal
     *            the principal
     * @return the twitter
     */
    private Twitter twitter(final Principal principal) {
        final Twitter instance = twitterFactory.getInstance();
        instance.setOAuthConsumer(appId, appSecret);
        instance.setOAuthAccessToken(new AccessToken(principal.getToken(), principal.getTokenSecret()));
        return instance;
    }
}
