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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.social.twitter.api.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class EntitiesMixin extends TwitterObjectMixin {

	@JsonCreator
	public EntitiesMixin(
			@JsonProperty("urls") List<UrlEntity> urls,
			@JsonProperty("hashtags") List<HashTagEntity> tags,
			@JsonProperty("user_mentions") List<MentionEntity> mentions,
			@JsonProperty("media") List<MediaEntity> media) {}

	@JsonProperty("hashtags")
	private List<HashTagEntity> tags;

	@JsonProperty("user_mentions")
	private List<MentionEntity> mentions;

	@JsonProperty("urls")
	private List<UrlEntity> urls;

	@JsonProperty("media")
	private List<MediaEntity> media;

	@JsonIgnore
	private List<TickerSymbolEntity> tickerSymbols;

	@JsonIgnore
	public abstract List<UrlEntity> getUrls();

	@JsonIgnore
	public abstract List<HashTagEntity> getHashTags();

	@JsonIgnore
	public abstract List<MentionEntity> getMentions();

	@JsonIgnore
	public abstract List<MediaEntity> getMedia();

	@JsonIgnore
	public abstract List<TickerSymbolEntity> getTickerSymbols();

}
