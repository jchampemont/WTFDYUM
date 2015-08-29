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
package com.jeanchampemont.wtfdyum.dto.type;

/**
 * The Enum EventType.
 */
public enum EventType {

    /** The registration. */
    REGISTRATION("You registered to WTFDYUM!", EventSeverityType.INFO),

    /** The feature enabled. */
    FEATURE_ENABLED("You enabled %s.", EventSeverityType.INFO),

    /** The feature disabled. */
    FEATURE_DISABLED("You disabled %s.", EventSeverityType.WARNING),

    /** The unfollow. */
    UNFOLLOW("@%s stopped following you.", EventSeverityType.WARNING),

    /** The twitter error. */
    TWITTER_ERROR("Error while contacting twitter.", EventSeverityType.ERROR),

    /** The rate limit exceeded. */
    RATE_LIMIT_EXCEEDED("Twitter's rate limit is exceeded, you might have too many followers.", EventSeverityType.ERROR),

    /** The invalid twitter credentials. */
    INVALID_TWITTER_CREDENTIALS(
            "Could not access your twitter account. If this error persists, please verify you allowed this application.",
            EventSeverityType.ERROR),

    /** The credentials invalid limit reached. */
    CREDENTIALS_INVALID_LIMIT_REACHED(
            "All settings where disabled due to several errors while accessing your twitter account",
            EventSeverityType.ERROR),

    /** The unknown error. */
    UNKNOWN_ERROR("An unknown error occured", EventSeverityType.ERROR);

    /**
     * Instantiates a new event type.
     *
     * @param message
     *            the message
     * @param severity
     *            the severity
     */
    private EventType(final String message, final EventSeverityType severity) {
        this.message = message;
        this.severity = severity;
    }

    /** The message. */
    private String message;

    /** The severity. */
    private EventSeverityType severity;

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
    public EventSeverityType getSeverity() {
        return severity;
    }
}
