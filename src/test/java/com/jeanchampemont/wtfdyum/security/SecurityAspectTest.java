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
package com.jeanchampemont.wtfdyum.security;

import com.jeanchampemont.wtfdyum.service.AuthenticationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.annotation.Annotation;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SecurityAspectTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ProceedingJoinPoint pjp;

    @InjectMocks
    private SecurityAspect sut;

    @Test
    public void aroundSecuredMethodTestAuthorized() throws Throwable {
        final Object expectedResult = new Object();

        when(authenticationService.isAuthenticated()).thenReturn(true);
        when(pjp.proceed()).thenReturn(expectedResult);

        final Object result = sut.aroundSecuredMethod(pjp, new Secured() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        Assertions.assertThat(result).isSameAs(expectedResult);
        verify(pjp, times(1)).proceed();
    }

    @Test(expected = SecurityException.class)
    public void aroundSecuredMethodTestUnauthorized() throws Throwable {
        when(authenticationService.isAuthenticated()).thenReturn(false);

        sut.aroundSecuredMethod(pjp, new Secured() {
            @Override
            public Class<? extends Annotation> annotationType() {
                // TODO Auto-generated method stub
                return null;
            }
        });

        Assertions.failBecauseExceptionWasNotThrown(SecurityException.class);
    }
}
