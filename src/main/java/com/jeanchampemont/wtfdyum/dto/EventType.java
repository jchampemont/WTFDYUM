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

/**
 * The Enum EventType.
 */
public enum EventType {

    /** The registration. */
    REGISTRATION("You registered to WTFDYUM!", EventSeverity.INFO),

    /** The unfollow. */
    UNFOLLOW("%s stopped following you.", EventSeverity.WARNING);

    /**
     * Instantiates a new event type.
     *
     * @param message
     *            the message
     * @param severity
     *            the severity
     */
    private EventType(final String message, final EventSeverity severity) {
        this.message = message;
        this.severity = severity;
    }

    /** The message. */
    private String message;

    /** The severity. */
    private EventSeverity severity;

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public EventSeverity getSeverity() {
        return severity;
    }
}
