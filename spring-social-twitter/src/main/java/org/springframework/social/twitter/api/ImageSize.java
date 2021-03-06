/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.twitter.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of image sizes supported by Twitter
 *
 * @author Craig Walls
 */
public enum ImageSize {

    THUMB,

    LARGE,

    MEDIUM,

    SMALL,

    UNKNOWN;

    private static Map<String, ImageSize> namesMap = new HashMap<String, ImageSize>(4);

    static {
        namesMap.put("thumb", THUMB);
        namesMap.put("large", LARGE);
        namesMap.put("medium", MEDIUM);
        namesMap.put("small", SMALL);
    }

    @JsonCreator
    public static ImageSize forValue(String value) {
        if (value != null)
            return namesMap.getOrDefault(value.toLowerCase(), UNKNOWN);
        return UNKNOWN;
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, ImageSize> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }
        return null;
    }
}
