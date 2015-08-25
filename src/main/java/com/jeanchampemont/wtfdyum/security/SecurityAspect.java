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
package com.jeanchampemont.wtfdyum.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeanchampemont.wtfdyum.service.AuthenticationService;

/**
 * The Class SecurityAspect.
 */
@Aspect
@Component
public class SecurityAspect {

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /** The authentication service. */
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Around secured method.
     *
     * @param pjp
     *            the pjp
     * @param secured
     *            the secured
     * @return the object
     * @throws Throwable
     *             the throwable
     */
    @Around("execution(public * *(..)) && @annotation(secured)")
    public Object aroundSecuredMethod(final ProceedingJoinPoint pjp, final Secured secured) throws Throwable {
        log.trace("Secured method called: {}", pjp.getSignature());
        if (!authenticationService.isAuthenticated()) {
            log.trace("Secured call not authorized, throwing SecurityException");
            throw new SecurityException();
        }
        log.trace("Secured call authorized");
        return pjp.proceed();
    }
}
