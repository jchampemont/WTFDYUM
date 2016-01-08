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
package com.jeanchampemont.wtfdyum.web;

import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.service.UserService;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;

/**
 * WTFDYUM Main controller.
 */
@Controller
public class MainController {

    static final String SESSION_REQUEST_TOKEN = "requestToken";

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Value("${wtfdyum.max-members}")
    private int maxMembers;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public RedirectView signin(final HttpServletRequest request) throws WTFDYUMException {
        if (authenticationService.isAuthenticated()) {
            return new RedirectView("/user", true);
        }

        if (maxMembers > 0 && principalService.countMembers() >= maxMembers) {
            throw new WTFDYUMException(WTFDYUMExceptionType.MEMBER_LIMIT_EXCEEDED);
        }

        final RequestToken requestToken = twitterService.signin("/signin/callback");

        request.getSession().setAttribute(SESSION_REQUEST_TOKEN, requestToken);

        return new RedirectView(requestToken.getAuthenticationURL());
    }

    @RequestMapping(value = "/signin/callback", method = RequestMethod.GET)
    public RedirectView signinCallback(@RequestParam("oauth_verifier") final String verifier,
            final HttpServletRequest request) throws WTFDYUMException {
        final RequestToken requestToken = (RequestToken) request.getSession().getAttribute(SESSION_REQUEST_TOKEN);
        request.getSession().removeAttribute(SESSION_REQUEST_TOKEN);

        final AccessToken accessToken = twitterService.completeSignin(requestToken, verifier);

        if (principalService.get(accessToken.getUserId()) == null) {
            userService.addEvent(accessToken.getUserId(), new Event(EventType.REGISTRATION, null));
        }

        final Principal user = new Principal(accessToken.getUserId(), accessToken.getToken(), accessToken.getTokenSecret());
        principalService.saveUpdate(user);
        authenticationService.authenticate(user);

        return new RedirectView("/user", true);
    }
}
