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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.social.twitter.api.*;
import org.springframework.test.web.client.MockRestServiceServer;

public abstract class AbstractTwitterApiTest {

	protected TwitterTemplate twitter;
	
	protected TwitterTemplate appAuthTwitter;
	
	protected MockRestServiceServer mockServer;
	
	protected MockRestServiceServer appAuthMockServer;

	@Before
	public void setup() {
		twitter = new TwitterTemplate("API_KEY", "API_SECRET", "ACCESS_TOKEN", "ACCESS_TOKEN_SECRET");
		mockServer = MockRestServiceServer.createServer(twitter.getRestTemplate());
		appAuthTwitter = new TwitterTemplate("APP_ACCESS_TOKEN");
		appAuthMockServer = MockRestServiceServer.createServer(appAuthTwitter.getRestTemplate());
	}
	
	protected Resource jsonResource(String filename) {
		return new ClassPathResource(filename + ".json", getClass());
	}

	protected void assertSingleTweet(Tweet tweet) {
		assertSingleTweet(tweet, false);
	}
	
	protected void assertSingleTweet(Tweet tweet, boolean isSearchResult) {
		assertEquals("12345", tweet.getId());
		assertEquals("Tweet 1", tweet.getText());
		assertEquals("habuma", tweet.getUser().getScreenName());
		assertEquals(112233, tweet.getUser().getId());
		assertEquals("http://a3.twimg.com/profile_images/1205746571/me2_300.jpg", tweet.getUser().getProfileImageUrl());
		assertEquals("Spring Social Showcase", tweet.getSource());
		assertEquals(1279042701000L, tweet.getCreatedAt().getTime());
		assertEquals(Long.valueOf(123123123123L), tweet.getInReplyToStatusId());
		if (!isSearchResult) {
			assertEquals(12, tweet.getRetweetCount().intValue());
			assertTrue(tweet.isRetweeted());
		} else {
			assertNull(tweet.getRetweetCount());
		}
		
		assertEquals(1001, (int) tweet.getFavoriteCount());
		
		assertTrue(tweet.isFavorited());
		Entities entities = tweet.getEntities();
		List<HashTagEntity> hashtags = entities.getHashTags();
		assertEquals(1,  hashtags.size());
		assertEquals(2, hashtags.get(0).getIndices().length);
		assertEquals(89, hashtags.get(0).getIndices()[0]);
		assertEquals(98, hashtags.get(0).getIndices()[1]);
		assertEquals("testhash", hashtags.get(0).getText());
		List<UrlEntity> urls = entities.getUrls();
		assertEquals(1, urls.size());
		assertEquals(2, urls.get(0).getIndices().length);
		assertEquals(10, urls.get(0).getIndices()[0]);
		assertEquals(30, urls.get(0).getIndices()[1]);
		assertEquals("fb.me/t35tur1", urls.get(0).getDisplayUrl());
		assertEquals("http://fb.me/t35tur1", urls.get(0).getExpandedUrl());
		assertEquals("http://t.co/t35tur1", urls.get(0).getUrl());
		List<MentionEntity> mentions = entities.getMentions();
		assertEquals(2, mentions.size());
		MentionEntity mention1 = mentions.get(0);
		assertEquals(11223344, mention1.getId());
		assertEquals("Bucky Greenhorn", mention1.getName());
		assertEquals("ukuleleman", mention1.getScreenName());
		assertEquals(2, mention1.getIndices().length);
		assertEquals(3, mention1.getIndices()[0]);
		assertEquals(18, mention1.getIndices()[1]);
		MentionEntity mention2 = mentions.get(1);
		assertEquals(44332211, mention2.getId());
		assertEquals("Jack Diamond", mention2.getName());
		assertEquals("jackdiamond", mention2.getScreenName());
		assertEquals(2, mention2.getIndices().length);
		assertEquals(23, mention2.getIndices()[0]);
		assertEquals(37, mention2.getIndices()[1]);
		Place.Geometry geometry = tweet.getCoordinates();
		assertEquals(geometry.getType(), Place.GeometryType.POINT);
		List<List<Place.GeoPoint>> coordinates = geometry.getCoordinates();
		assertEquals(1, coordinates.size());
		assertEquals(1, coordinates.get(0).size());
		Place.GeoPoint point = coordinates.get(0).get(0);
		assertEquals(-122.41888889, point.getLongitude(),0.000001);
		assertEquals(37.80194444, point.getLatitude(),0.000001);
		Place place = tweet.getPlace();
		assertPlace(place);
		String quotedStatusId = tweet.getQuotedStatusId();
		assertEquals("987654321",quotedStatusId);
		Tweet quotedStatus = tweet.getQuotedStatus();
		assertEquals("987654321",quotedStatus.getId());
		assertEquals("A domino can knock over another domino about 1.5x larger than itself",quotedStatus.getText());
	}
	
	protected void assertTimelineTweets(List<Tweet> tweets) {
		assertTimelineTweets(tweets, false);
	}
	
	protected void assertTimelineTweets(List<Tweet> tweets, boolean isSearchResult) {
		assertEquals(3, tweets.size());
		assertSingleTweet(tweets.get(0), isSearchResult);
		Tweet tweet2 = tweets.get(1);
		assertEquals("54321", tweet2.getId());
		assertEquals("Tweet 2", tweet2.getText());
		assertEquals("rclarkson", tweet2.getUser().getScreenName());
		assertEquals(332211, tweet2.getUser().getId());
		assertEquals("http://a3.twimg.com/profile_images/1205746571/me2_300.jpg", tweet2.getUser().getProfileImageUrl());
		assertEquals("Twitter", tweet2.getSource());
		assertEquals(1279654701000L, tweet2.getCreatedAt().getTime());
		if (!isSearchResult) {
			assertEquals(0, tweet2.getRetweetCount().intValue());
		} else {
			assertNull(tweet2.getRetweetCount());
		}
		Entities entities = tweet2.getEntities();
		assertEquals(0, entities.getHashTags().size());
		Tweet tweet3 = tweets.get(2);
		Entities entities2 = tweet3.getExtendedEntities();
		assertEquals(3, entities2.getMedia().size());
		assertEquals(4, entities2.getMedia().get(0).getSizes().size());
    }

	protected void assertPlace(Place place) {
		assertEquals("6b9811c8d9de10b9", place.getId());
		assertEquals("Twitter 3rd Floor Lunch Room", place.getName());
		assertEquals("Twitter 3rd Floor Lunch Room, Twitter HQ", place.getFullName());
		assertNull(place.getStreetAddress());
		assertEquals("United States", place.getCountry());
		assertEquals("US", place.getCountryCode());
		assertEquals(PlaceType.POINT_OF_INTEREST, place.getPlaceType());
		assertEquals(1, place.getContainedWithin().size());
		Place containedWithin = place.getContainedWithin().get(0);
		assertEquals("247f43d441defc03", containedWithin.getId());
		assertEquals("Twitter HQ", containedWithin.getName());
		assertEquals("Twitter HQ, San Francisco", containedWithin.getFullName());
		assertEquals("795 Folsom St", containedWithin.getStreetAddress());
		assertEquals("United States", containedWithin.getCountry());
		assertEquals("US", containedWithin.getCountryCode());
		assertEquals(PlaceType.POINT_OF_INTEREST, containedWithin.getPlaceType());
		Place.Geometry boundingBox = place.getBoundingBox();
		assertEquals(Place.GeometryType.POLYGON,boundingBox.getType());
		List<List<Place.GeoPoint>> coordinates = boundingBox.getCoordinates();
		assertEquals(1, coordinates.size());
		assertEquals(4, coordinates.get(0).size());
		List<Place.GeoPoint> points = coordinates.get(0);
		assertEquals(-122.40061283111572, boundingBox.getCoordinates().get(0).get(0).getLongitude(), 0.000001);
		assertEquals(37.78211205989559, points.get(0).getLatitude(), 0.000001);
		assertEquals(-122.40061283111572, points.get(1).getLongitude(), 0.000001);
		assertEquals(37.78211205989559, points.get(1).getLatitude(), 0.000001);
		assertEquals(-122.40061283111572, points.get(2).getLongitude(), 0.000001);
		assertEquals(37.78211205989559, points.get(2).getLatitude(), 0.000001);
		assertEquals(-122.40061283111572, points.get(3).getLongitude(), 0.000001);
		assertEquals(37.78211205989559, points.get(3).getLatitude(), 0.000001);
		Place.Geometry geometry = place.getGeometry();
		assertEquals(Place.GeometryType.POINT, geometry.getType());
		coordinates = geometry.getCoordinates();
		assertEquals(1, coordinates.size());
		assertEquals(1, coordinates.get(0).size());
		Place.GeoPoint point = coordinates.get(0).get(0);
		assertEquals(37.7821120598957, point.getLatitude(), 0.000001);
		assertEquals(-122.400612831117, point.getLongitude(), 0.000001);
	}

	protected void assertUser(TwitterProfile profile) throws Exception {
		assertEquals(161064614, profile.getId());
		assertEquals("artnames", profile.getScreenName());
		assertEquals("Art Names", profile.getName());
		assertEquals("I'm just a normal kinda guy", profile.getDescription());
		assertEquals("Denton, TX", profile.getLocation());
		assertEquals("http://www.springsource.org", profile.getUrl());
		assertEquals("http://a1.twimg.com/sticky/default_profile_images/default_profile_4_normal.png", profile.getProfileImageUrl());
		assertTrue(profile.isNotificationsEnabled());
		assertFalse(profile.isVerified());
		assertTrue(profile.isGeoEnabled());
		assertTrue(profile.isContributorsEnabled());
		assertTrue(profile.isTranslator());
		assertTrue(profile.isFollowing());
		assertTrue(profile.isFollowRequestSent());
		assertTrue(profile.isProtected());
		assertEquals("en", profile.getLanguage());
		assertEquals(125, profile.getStatusesCount());
		assertEquals(1001, profile.getListedCount());
		assertEquals(14, profile.getFollowersCount());
		assertEquals(194, profile.getFriendsCount());
		assertEquals(4, profile.getFavoritesCount());
		assertEquals("Mountain Time (US & Canada)", profile.getTimeZone());
		assertEquals(-25200, profile.getUtcOffset());
		assertTrue(profile.useBackgroundImage());
		assertEquals("C0DEED", profile.getSidebarBorderColor());
		assertEquals("DDEEF6", profile.getSidebarFillColor());
		assertEquals("C0DEED", profile.getBackgroundColor());
		assertEquals("http://a3.twimg.com/a/1301419075/images/themes/theme1/bg.png", profile.getBackgroundImageUrl());
		assertFalse(profile.isBackgroundImageTiled());
		assertEquals("333333", profile.getTextColor());
		assertEquals("0084B4", profile.getLinkColor());
	}
}
