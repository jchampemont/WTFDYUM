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
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.utils.TwitterFactoryHolder;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMExceptionType;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TwitterServiceImpl implements TwitterService {

    @Autowired
    public TwitterServiceImpl(final TwitterFactoryHolder twitterFactory, final Mapper mapper,
            @Value("${wtfdyum.server-base-url}") final String baseUrl,
            @Value("${wtfdyum.twitter.appId}") final String appId,
            @Value("${wtfdyum.twitter.appSecret}") final String appSecret) {
        this.twitterFactory = twitterFactory;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String baseUrl;

    private final TwitterFactoryHolder twitterFactory;

    private final Mapper mapper;

    private final String appId;

    private final String appSecret;

    @Override
    public AccessToken completeSignin(final RequestToken requestToken, final String verifier) throws WTFDYUMException {
        AccessToken token = null;
        try {
            token = twitter().getOAuthAccessToken(requestToken, verifier);
        } catch (final TwitterException e) {
            log.debug("Error while completeSignin", e);
            throw new WTFDYUMException(e, WTFDYUMExceptionType.TWITTER_ERROR);
        }
        return token;
    }

    @Override
    public Set<Long> getFollowers(final Long userId, final Optional<Principal> principal) throws WTFDYUMException {
        Preconditions.checkNotNull(userId);

        final Twitter twitter = principal.isPresent() ? twitter(principal.get()) : twitter();

        final Set<Long> result = new HashSet<>();
        try {
            IDs followersIDs = null;
            final long cursor = -1;
            do {
                followersIDs = twitter.getFollowersIDs(userId, cursor);

                checkRateLimitStatus(followersIDs.getRateLimitStatus(),
                        WTFDYUMExceptionType.GET_FOLLOWERS_RATE_LIMIT_EXCEEDED);

                final Set<Long> currentFollowers = Arrays.stream(followersIDs.getIDs()).boxed()
                        .collect(Collectors.toCollection(() -> new HashSet<>()));

                result.addAll(currentFollowers);
            } while (followersIDs.hasNext());

        } catch (final TwitterException e) {
            log.debug("Error while getFollowers", e);
            throw new WTFDYUMException(e, WTFDYUMExceptionType.TWITTER_ERROR);
        }
        return result;
    }

    @Override
    public User getUser(final Principal principal, final Long id) throws WTFDYUMException {
        User result = null;
        try {
            final twitter4j.User user = twitter(principal).users().showUser(id);
            result = mapper.map(user, User.class);
        } catch (final TwitterException e) {
            log.debug("Error while getUser", e);
            throw new WTFDYUMException(e, WTFDYUMExceptionType.TWITTER_ERROR);
        }
        return result;
    }

    @Override
    public List<User> getUsers(final Principal principal, final long... ids) throws WTFDYUMException {
        final List<User> result = new ArrayList<>();
        if (ids.length == 0) {
            return result;
        }
        try {
            final List<twitter4j.User> users = new ArrayList<>();
            for (int i = 0; i <= (ids.length - 1) / 100; i++) {
                final ResponseList<twitter4j.User> lookupUsers = twitter(principal).users()
                    .lookupUsers(Arrays.copyOfRange(ids, i * 100, Math.min((i + 1) * 100, ids.length)));

                users.addAll(lookupUsers);
            }

            for (final twitter4j.User u : users) {
                result.add(mapper.map(u, User.class));
            }

        } catch (final TwitterException e) {
            log.debug("Error while getUsers", e);
            throw new WTFDYUMException(e, WTFDYUMExceptionType.TWITTER_ERROR);
        }
        return result;
    }

    @Override
    public void sendDirectMessage(final Principal principal, final Long toUserId, final String text)
            throws WTFDYUMException {
        try {
            twitter(principal).sendDirectMessage(toUserId, text);
        } catch (final TwitterException e) {
            log.debug("Error while sendDirectMessage", e);
            throw new WTFDYUMException(e, WTFDYUMExceptionType.TWITTER_ERROR);
        }
    }

    @Override
    public RequestToken signin(final String path) throws WTFDYUMException {
        RequestToken token = null;
        try {
            token = twitter().getOAuthRequestToken(new StringBuilder(baseUrl).append(path).toString());
        } catch (final TwitterException e) {
            log.debug("Error while signin", e);
            throw new WTFDYUMException(e, WTFDYUMExceptionType.TWITTER_ERROR);
        }
        return token;
    }

    @Override
    public void tweet(final Principal principal, final String text) throws WTFDYUMException {
        try {
            twitter(principal).updateStatus(text);
        } catch (final TwitterException e) {
            log.debug("Error while tweet", e);
            throw new WTFDYUMException(e, WTFDYUMExceptionType.TWITTER_ERROR);
        }
    }

    @Override
    public boolean verifyCredentials(final Principal principal) {
        boolean result = true;
        try {
            twitter(principal).verifyCredentials();
        } catch (final TwitterException e) {
            result = false;
        }
        return result;
    }

    private void checkRateLimitStatus(final RateLimitStatus status, final WTFDYUMExceptionType exceptionType)
            throws WTFDYUMException {
        if (status.getRemaining() == 0) {
            throw new WTFDYUMException(exceptionType);
        }
    }

    private Twitter twitter() {
        final Twitter instance = twitterFactory.getInstance();
        instance.setOAuthConsumer(appId, appSecret);
        return instance;
    }

    private Twitter twitter(final Principal principal) {
        final Twitter instance = twitter();
        instance.setOAuthAccessToken(new AccessToken(principal.getToken(), principal.getTokenSecret()));
        return instance;
    }
}
