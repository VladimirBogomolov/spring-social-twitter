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
package org.springframework.social.twitter.api.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.Place;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.util.Date;

/**
 * Mixin class for adding Jackson annotations to Tweet.
 *
 * @author Craig Walls
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class TweetMixin extends TwitterObjectMixin {

    @JsonCreator
    TweetMixin(
            @JsonProperty("id") String id,
            @JsonProperty("text") String text,
            @JsonProperty("created_at") @JsonDeserialize(using = TimelineDateDeserializer.class) Date createdAt) {
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = TimelineDateSerializer.class)
    private Date createdAt;

    @JsonProperty("user")
    private TwitterProfile user;

    @JsonProperty("in_reply_to_user_id")
    private Long inReplyToUserId;

    @JsonProperty("in_reply_to_status_id")
    private Long inReplyToStatusId;

    @JsonProperty("in_reply_to_screen_name")
    private String inReplyToScreenName;

    @JsonProperty("lang")
    private String languageCode;

    @JsonProperty("source")
    private String source;

    @JsonProperty("retweet_count")
    private Integer retweetCount;

    @JsonProperty("retweeted_status")
    private Tweet retweetedStatus;

    @JsonProperty("favorite_count")
    private Integer favoriteCount;

    @JsonProperty("entities")
    private Entities entities;

    @JsonProperty("favorited")
    private boolean favorited;

    @JsonProperty("retweeted")
    private boolean retweeted;

    @JsonProperty("coordinates")
    private Place.Geometry coordinates;

    @JsonProperty("place")
    private Place place;

    @JsonProperty("quoted_status_id")
    private String quotedStatusId;

    @JsonProperty("quoted_status")
    private Tweet quotedStatus;

    @JsonIgnore
    public abstract String getUnmodifiedText();

    @JsonIgnore
    public abstract String getFromUser();

    @JsonIgnore
    public abstract Long getToUserId();

    @JsonIgnore
    public abstract long getFromUserId();

    @JsonIgnore
    public abstract String getProfileImageUrl();

    @JsonIgnore
    public abstract boolean isRetweet();
}
