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

import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;

import java.util.ArrayList;

public class ResponseListMockForTest<T> extends ArrayList<T>implements ResponseList<T> {

    private static final long serialVersionUID = -2964295762238022575L;

    @Override
    public int getAccessLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        throw new UnsupportedOperationException();
    }
}
