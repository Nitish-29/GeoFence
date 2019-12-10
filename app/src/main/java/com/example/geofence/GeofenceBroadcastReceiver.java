package com.example.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receiver for geofence transition changes.
 * <p>
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a JobIntentService
 * that will handle the intent in the background.
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();

    /**
     * Receives incoming intents.
     * int userId = SessionManager.getInstance().getInt(SessionConstant.PREF_UID);
     * String userName = SessionManager.getInstance().getString(SessionConstant.PREF_USERNAME);
     * String password = SessionManager.getInstance().getString(SessionConstant.PREF_PASSWORD);
     * String deviceId = ApplicationController.getInstance().getCurrentDeviceId();
     *
     * @param context the application context.
     * @param intent  sent by Location Services. This Intent is provided to Location
     *                Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        GeoLocation geoLocation = intent.getParcelableExtra("geo");
        if (geoLocation != null) {
            String key = geoLocation.getDeviceId() + geoLocation.getUserName() + geoLocation.getId();

        }


        GeofenceTransitionsJobIntentService.enqueueWork(context, intent);
        Log.d("GEO_FENCING", intent.toString());
        Log.d(TAG, "GEO_FENCE_RECEIVER_CALLED");
    }

}
