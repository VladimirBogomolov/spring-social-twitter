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

import java.util.List;

/**
 * Represents a place that a Twitter user may send a tweet from.
 * @author Craig Walls
 */
public class Place extends TwitterObject {

	private final String id;
	
	private final String name;
	
	private final String fullName;
	
	private final String streetAddress;
	
	private final String country;
	
	private final String countryCode;
	
	private final PlaceType placeType;
	
	private List<Place> containedWithin;
	
	private Geometry boundingBox;

	private Geometry geometry;

	public Place(String id, String name, String fullName, String streetAddress, String country, String countryCode, PlaceType placeType) {
		this.id = id;
		this.name = name;
		this.fullName = fullName;
		this.country = country;
		this.streetAddress = streetAddress;
		this.countryCode = countryCode;
		this.placeType = placeType;
	}

	/**
	 * @return the place's ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return a brief name for the place.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the full name for the place.
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @return the place's street address. May be null.
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * @return the place's country.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the place's country code.
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @return the place type.
	 */
	public PlaceType getPlaceType() {
		return placeType;
	}

	/**
	 * @return a list of other places that this place is contained within.
	 */
	public List<Place> getContainedWithin() {
		return containedWithin;
	}

	/**
	 * @return a list of points defining a box that fully contains the place's geometry.
	 */
	public Geometry getBoundingBox() {
		return boundingBox;
	}

	/**
	 * @return the place's geometry.
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Represents a point in geospace (e.g., latitude/longitude)
	 */
	public static class GeoPoint {
		private final double longitude;
		private final double latitude;

		public GeoPoint(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			GeoPoint geoPoint = (GeoPoint) o;

			return Double.compare(geoPoint.latitude, latitude) == 0 && Double.compare(geoPoint.longitude, longitude) == 0;
		}

		@Override
		public int hashCode() {
			int result;
			long temp;
			temp = Double.doubleToLongBits(latitude);
			result = (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(longitude);
			result = 31 * result + (int) (temp ^ (temp >>> 32));
			return result;
		}
	}

	/**
	 * Represents a place's geometry.
	 */
	public static class Geometry {
		private List<List<GeoPoint>> coordinates;
		private GeometryType type;

		public Geometry(GeometryType type, List<List<GeoPoint>> coordinates) {
			this.type = type;
			this.coordinates = coordinates;
		}

		/**
		 * @return the geometry's type, either POINT, POLYGON, or MULTIPOLYGON.
		 */
		public GeometryType getType() {
			return type;
		}

		/**
		 * The coordinates defining a place's geometry.
		 * If type is POINT, then it is a List containing a single List containing a single point.
		 * If type is POLYGON, then it is a List containing a List of points that define the polygon.
		 * If type is MULTIPOLYGON, then it is a List of polygon-defining Lists.
		 * @return the coordinates defining a place's geometry.
		 */
		public List<List<GeoPoint>> getCoordinates() {
			return coordinates;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Geometry geometry = (Geometry) o;

			return (coordinates != null ? coordinates.equals(geometry.coordinates) : geometry.coordinates == null) && type == geometry.type;
		}

		@Override
		public int hashCode() {
			int result = coordinates != null ? coordinates.hashCode() : 0;
			result = 31 * result + (type != null ? type.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "Geometry{" +
					"coordinates=" + coordinates +
					", type=" + type +
					'}';
		}
	}

	public static enum GeometryType {
		POINT, POLYGON, MULTIPOLYGON
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Place place = (Place) o;

		if (id != null ? !id.equals(place.id) : place.id != null) return false;
		if (name != null ? !name.equals(place.name) : place.name != null) return false;
		if (fullName != null ? !fullName.equals(place.fullName) : place.fullName != null) return false;
		if (streetAddress != null ? !streetAddress.equals(place.streetAddress) : place.streetAddress != null)
			return false;
		if (country != null ? !country.equals(place.country) : place.country != null) return false;
		if (countryCode != null ? !countryCode.equals(place.countryCode) : place.countryCode != null) return false;
		if (placeType != place.placeType) return false;
		if (containedWithin != null ? !containedWithin.equals(place.containedWithin) : place.containedWithin != null)
			return false;
		if (boundingBox != null ? !boundingBox.equals(place.boundingBox) : place.boundingBox != null) return false;
		return geometry != null ? geometry.equals(place.geometry) : place.geometry == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
		result = 31 * result + (streetAddress != null ? streetAddress.hashCode() : 0);
		result = 31 * result + (country != null ? country.hashCode() : 0);
		result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
		result = 31 * result + (placeType != null ? placeType.hashCode() : 0);
		result = 31 * result + (containedWithin != null ? containedWithin.hashCode() : 0);
		result = 31 * result + (boundingBox != null ? boundingBox.hashCode() : 0);
		result = 31 * result + (geometry != null ? geometry.hashCode() : 0);
		return result;
	}
}
