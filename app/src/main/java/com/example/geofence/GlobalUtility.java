package com.example.geofence;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;


/**
 * contains all utility functions which are used in application.
 */
public class GlobalUtility {

    public static Float getDistanceFromLatLonInKm(LatLng A, LatLng B, GeoLocation geoLocation) {
        if (A == null || B == null) {
            return null;
        }
        float radiusOut = geoLocation.getRadiusOut();
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(A.latitude);
        startPoint.setLongitude(A.longitude);

        Location endPoint = new Location("locationB");
        endPoint.setLatitude(B.latitude);
        endPoint.setLongitude(B.longitude);
        float distance = startPoint.distanceTo(endPoint);
        return distance;


    }

}
