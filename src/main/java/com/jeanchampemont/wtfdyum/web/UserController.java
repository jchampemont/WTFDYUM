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
package com.jeanchampemont.wtfdyum.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jeanchampemont.wtfdyum.service.AuthenticationService;

/**
 * The Class UserController.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    /** The authentication service. */
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Index.
     *
     * @return the string
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        final ModelAndView result = new ModelAndView("user/index");

        result.getModel().put("userId", authenticationService.getCurrentUserId().get());

        return result;
    }

    /**
     * Logout.
     *
     * @return the redirect view
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public RedirectView logout() {
        authenticationService.logOut();
        return new RedirectView("/", true);
    }
}
