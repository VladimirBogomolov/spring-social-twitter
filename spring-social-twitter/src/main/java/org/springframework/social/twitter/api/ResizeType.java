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
 * Created by Bogomolov Vladimir on 20.10.17.
 */
public enum ResizeType {

    FIT,

    CROP,

    UNKNOWN;

    private static Map<String, ResizeType> namesMap = new HashMap<String, ResizeType>(2);

    static {
        namesMap.put("fit", FIT);
        namesMap.put("crop", CROP);
    }

    @JsonCreator
    public static ResizeType forValue(String value) {
        if (value != null)
            return namesMap.getOrDefault(value.toLowerCase(), UNKNOWN);
        return UNKNOWN;
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, ResizeType> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }
        return null;
    }
}
