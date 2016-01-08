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
package com.jeanchampemont.wtfdyum.utils;

import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class AuthenticationInterceptor. This class:
 *
 * - adds authentication information in each model, for the templates to use. -
 * add the principal to the SessionManager
 */
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PrincipalService principalService;

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
            final ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.getModel().put("authenticated", authenticationService.isAuthenticated());
        }
        SessionManager.setPrincipal(null);
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final Long currentUserId = authenticationService.getCurrentUserId();
        if (currentUserId != null) {
            final Principal principal = principalService.get(currentUserId);
            SessionManager.setPrincipal(principal);
        }
        return super.preHandle(request, response, handler);
    }
}
