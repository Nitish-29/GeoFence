package com.example.geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class GeoFenceHandler {
    private final Context context;
    private PendingIntent geoFenceIntent;
    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofences;

    public GeoFenceHandler(Context context) {
        this.context = context;
        geofencingClient = LocationServices.getGeofencingClient(context);
        geofences = new ArrayList<>();

    }

    private GeofencingRequest getGeofencingRequest(ArrayList<Geofence> geofences) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geoFenceIntent != null) {
            return geoFenceIntent;
        }
        Intent intent = new Intent(context, GeofenceTransitionsJobIntentService.class);
        geoFenceIntent = PendingIntent.getService(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geoFenceIntent;
    }

    public ArrayList<Geofence> addGeoFence(GeoLocation geoLocation) {
        if (geofences != null && geofences.size() > 0) {
            geofences.clear();
        }
        geofences.add(getGeofence(geoLocation, true));//return
        geofences.add(getGeofence(geoLocation, false));// leave

        return geofences;
    }

    public ArrayList<Geofence> getGeoFenceList() {
        return geofences;
    }


    public Geofence getGeofence(GeoLocation geoLocation, boolean isEnter) {
        return new Geofence.Builder()
                .setRequestId(geoLocation.getId())
                .setCircularRegion(
                        geoLocation.getLatitude(),
                        geoLocation.getLongitude(),
                        isEnter ? geoLocation.getRadiusIn() : geoLocation.getRadiusIn())
                .setExpirationDuration(geoLocation.getExpirationDuration())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

    }


}
