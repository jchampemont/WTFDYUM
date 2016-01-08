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
package com.jeanchampemont.wtfdyum.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeanchampemont.wtfdyum.dto.type.EventType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event {

    public Event() {
        // left deliberately empty
    }

    public Event(final EventType type, final String additionalData) {
        this.type = type;
        this.additionalData = additionalData;
    }

    private EventType type;

    private String additionalData;

    private LocalDateTime creationDateTime;

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

    public String getAdditionalData() {
        return additionalData;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    @JsonIgnore
    public String getMessage() {
        return String.format(type.getMessage(), additionalData);
    }

    public EventType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.creationDateTime, this.additionalData);
    }

    public void setAdditionalData(final String additionalData) {
        this.additionalData = additionalData;
    }

    public void setCreationDateTime(final LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public void setType(final EventType type) {
        this.type = type;
    }
}
