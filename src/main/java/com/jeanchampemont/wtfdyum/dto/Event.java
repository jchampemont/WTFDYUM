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
package com.jeanchampemont.wtfdyum.dto;

import java.io.Serializable;

/**
 * The Class Event.
 */
public class Event implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3209093493359737774L;

    /**
     * Instantiates a new event.
     */
    public Event() {
        // left deliberately empty
    }

    /**
     * Instantiates a new event.
     *
     * @param type
     *            the type
     * @param additionalData
     *            the additional data
     */
    public Event(final EventType type, final String additionalData) {
        this.type = type;
        this.additionalData = additionalData;
    }

    /** The type. */
    private EventType type;

    /** The additional data. */
    private String additionalData;

    /**
     * Gets the additional data.
     *
     * @return the additional data
     */
    public String getAdditionalData() {
        return additionalData;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public EventType getType() {
        return type;
    }

    /**
     * Sets the additional data.
     *
     * @param additionalData
     *            the new additional data
     */
    public void setAdditionalData(final String additionalData) {
        this.additionalData = additionalData;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(final EventType type) {
        this.type = type;
    }

}
