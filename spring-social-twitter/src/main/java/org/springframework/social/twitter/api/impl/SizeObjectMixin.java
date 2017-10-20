package org.springframework.social.twitter.api.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class SizeObjectMixin {

    @JsonProperty("h")
    private Integer height;

    @JsonProperty("w")
    private Integer width;
}
