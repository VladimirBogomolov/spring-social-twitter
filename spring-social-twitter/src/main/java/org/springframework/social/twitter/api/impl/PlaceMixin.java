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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.social.twitter.api.Place;
import org.springframework.social.twitter.api.Place.GeoPoint;
import org.springframework.social.twitter.api.Place.Geometry;
import org.springframework.social.twitter.api.Place.GeometryType;
import org.springframework.social.twitter.api.PlaceType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class PlaceMixin extends TwitterObjectMixin {

    @JsonCreator
    public PlaceMixin(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("full_name") String fullName,
            @JsonProperty("attributes") @JsonDeserialize(using = StreetAddressDeserializer.class) String streetAddress,
            @JsonProperty("country") String country,
            @JsonProperty("country_code") String countryCode,
            @JsonProperty("place_type") @JsonDeserialize(using = PlaceTypeDeserializer.class) PlaceType placeType) {
    }

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("place_type")
    private PlaceType placeType;

    @JsonProperty("contained_within")
    private List<Place> containedWithin;

    @JsonProperty("bounding_box")
    private Geometry boundingBox;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @JsonSerialize(using = GeometrySerializer.class)
    public static class GeometryMixin {
//        private List<List<GeoPoint>> coordinates;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    public static class GeoPointMixin {
    }

    private static class StreetAddressDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Map<String, String> attributesMap = jp.readValueAs(new TypeReference<Map<String, String>>() {
            });
            return attributesMap.getOrDefault("street_address", null);
        }
    }

    private static class BoundingBoxDeserializer extends JsonDeserializer<List<GeoPoint>> {
        @Override
        public List<GeoPoint> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            List<GeoPoint> points = new ArrayList<Place.GeoPoint>();
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jp.getCurrentName();
                if ("type".equals(fieldname)) {
                    jp.nextToken();
                }
                if ("coordinates".equals(fieldname)) {
                    jp.nextToken();
                    jp.nextToken();
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        jp.nextToken();
                        double longitude = jp.getDoubleValue();
                        jp.nextToken();
                        double latitude = jp.getDoubleValue();
                        points.add(new Place.GeoPoint(latitude, longitude));
                        jp.nextToken();
                    }
                    jp.nextToken();
                }
            }
            return points;
        }
    }

    private static class GeometryDeserializer extends JsonDeserializer<Geometry> {
        @Override
        public Geometry deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            List<List<GeoPoint>> geoPoints = new ArrayList<List<GeoPoint>>();

            List<?> coordinatesList = null;
            String type = null;
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jp.getCurrentName();
                if ("coordinates".equals(fieldname)) {
                    jp.nextToken(); // to get to the value token;
                    coordinatesList = jp.readValueAs(new TypeReference<List<?>>() {
                    });
                } else if ("type".equals(fieldname)) {
                    jp.nextToken();
                    type = jp.getText();
                }
            }

            if ("point".equals(type.toLowerCase())) {
                setPoint(geoPoints, coordinatesList);
            } else if ("polygon".equals(type.toLowerCase())) {
                setPolygon(geoPoints, coordinatesList);
            } else if ("multipolygon".equals(type.toLowerCase())) {
                setMultiPolygon(geoPoints, coordinatesList);
            }

            return new Geometry(GeometryType.valueOf(type.toUpperCase()), geoPoints);
        }

        private void setMultiPolygon(List<List<GeoPoint>> geoPoints,
                                     List<?> coordinatesList) {
            for (Object o : coordinatesList) {
                List<?> mCoordinatesList = (List<?>) o;
                setPolygon(geoPoints, mCoordinatesList);
            }
        }

        private void setPolygon(List<List<GeoPoint>> geoPoints, List<?> coordinatesList) {
            @SuppressWarnings("unchecked")
            List<List<Double>> polyCoords = (List<List<Double>>) coordinatesList.get(0);
            List<GeoPoint> polyPoints = new ArrayList<Place.GeoPoint>();
            for (List<Double> polyCoord : polyCoords) {
                if (polyCoord != null && polyCoord.size() == 2) {
                    double latitude = polyCoord.get(1);
                    double longitude = polyCoord.get(0);
                    polyPoints.add(new GeoPoint(latitude, longitude));
                }
            }
            geoPoints.add(polyPoints);
        }

        private void setPoint(List<List<GeoPoint>> geoPoints, List<?> coordinatesList) {
            List<GeoPoint> points = new ArrayList<GeoPoint>();
            if (coordinatesList != null && coordinatesList.size() == 2) {
                double latitude = Double.valueOf(String.valueOf(coordinatesList.get(1)));
                double longitude = Double.valueOf(String.valueOf(coordinatesList.get(0)));
                points.add(new GeoPoint(latitude, longitude));
            }
            geoPoints.add(points);
        }
    }

    private static class GeometrySerializer extends JsonSerializer<Geometry> {

        @Override
        public void serialize(Geometry value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeStartObject();
            gen.writeStringField("type", value.getType().toString().toLowerCase());
            if (value.getType() == GeometryType.POLYGON) {
                gen.writeFieldName("coordinates");
                gen.writeObject(value.getCoordinates());
            } else if (value.getType() == GeometryType.POINT) {
                gen.writeArrayFieldStart("coordinates");
                if (value.getCoordinates() != null && value.getCoordinates().size() > 0 && value.getCoordinates().get(0).size() > 0 && value.getCoordinates().get(0).get(0) != null) {
                    gen.writeNumber(value.getCoordinates().get(0).get(0).getLongitude());
                    gen.writeNumber(value.getCoordinates().get(0).get(0).getLatitude());
                }
                gen.writeEndArray();
            } else if (value.getType() == GeometryType.MULTIPOLYGON) {
                gen.writeFieldName("coordinates");
                List<Object> listToWrite = new ArrayList<>();
                for (Object list : value.getCoordinates()) {
                    listToWrite.add(new ArrayList<Object>() {
                        private static final long serialVersionUID = 1L;

                        {
                            add(list);
                        }
                    });
                }
                gen.writeObject(listToWrite);
            }
            gen.writeEndObject();
        }
    }

}
