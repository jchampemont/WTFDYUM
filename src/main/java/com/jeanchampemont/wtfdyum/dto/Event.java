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

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The Class Event.
 */
public class Event {

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

    /** The creation date time. */
    private LocalDateTime creationDateTime;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        if (additionalData == null) {
            if (other.additionalData != null) {
                return false;
            }
        } else if (!additionalData.equals(other.additionalData)) {
            return false;
        }
        if (creationDateTime == null) {
            if (other.creationDateTime != null) {
                return false;
            }
        } else if (!creationDateTime.equals(other.creationDateTime)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    /**
     * Gets the additional data.
     *
     * @return the additional data
     */
    public String getAdditionalData() {
        return additionalData;
    }

    /**
     * Gets the creation date time.
     *
     * @return the creation date time
     */
    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public EventType getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.creationDateTime, this.additionalData);
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
     * Sets the creation date time.
     *
     * @param creationDateTime
     *            the new creation date time
     */
    public void setCreationDateTime(final LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
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
