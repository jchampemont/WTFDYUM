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
package com.jeanchampemont.wtfdyum.dto.type;

public enum EventType {
    REGISTRATION("You registered to WTFDYUM!", EventSeverityType.INFO),
    FEATURE_ENABLED("You enabled %s.", EventSeverityType.INFO),
    FEATURE_DISABLED("You disabled %s.", EventSeverityType.WARNING),
    UNFOLLOW("@%s stopped following you.", EventSeverityType.WARNING),
    TWITTER_ERROR("Error while contacting twitter.", EventSeverityType.ERROR),
    RATE_LIMIT_EXCEEDED("Twitter's rate limit is exceeded, you might have too many followers.", EventSeverityType.ERROR),
    INVALID_TWITTER_CREDENTIALS(
            "Could not access your twitter account. If this error persists, please verify you allowed this application.",
            EventSeverityType.ERROR),
    CREDENTIALS_INVALID_LIMIT_REACHED(
            "All settings where disabled due to several errors while accessing your twitter account",
            EventSeverityType.ERROR),
    UNKNOWN_ERROR("An unknown error occured", EventSeverityType.ERROR);

    private EventType(final String message, final EventSeverityType severity) {
        this.message = message;
        this.severity = severity;
    }

    private String message;

    private EventSeverityType severity;

    public String getMessage() {
        return message;
    }

    public EventSeverityType getSeverity() {
        return severity;
    }
}
