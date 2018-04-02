/*
 * Copyright (C) 2018 WTFDYUM
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

import com.jeanchampemont.wtfdyum.dto.Feature;
import com.jeanchampemont.wtfdyum.security.Secured;
import com.jeanchampemont.wtfdyum.service.AdminService;
import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import com.jeanchampemont.wtfdyum.service.PrincipalService;
import com.jeanchampemont.wtfdyum.service.TwitterService;
import com.jeanchampemont.wtfdyum.utils.SessionManager;
import com.jeanchampemont.wtfdyum.utils.WTFDYUMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private AdminService adminService;

    @RequestMapping(method = RequestMethod.GET)
    @Secured
    public ModelAndView index() {
        if(!authenticationService.isAdmin()) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView result = new ModelAndView("admin/index");

        final Long userId = authenticationService.getCurrentUserId();

        try {
            result.getModel().put("user", twitterService.getUser(SessionManager.getPrincipal(), userId));
        } catch (final WTFDYUMException e) {
            authenticationService.logOut();
            return new ModelAndView("redirect:/");
        }

        result.getModel().put("membersCount", principalService.countMembers());

        Map<String, Integer> featureEnabledCount = adminService.countEnabledFeature(principalService.getMembers()).entrySet().stream().collect(toMap(e -> e.getKey().name(), Map.Entry::getValue));

        result.getModel().put("availableFeatures", Feature.values());
        result.getModel().put("featureEnabledCount", featureEnabledCount);

        return result;
    }
}
