package com.example.geofence;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.geofence.databinding.FragmentChangeGeolocationBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import static android.content.Context.LOCATION_SERVICE;


public class GeoFenceSettings extends Fragment implements OnMapReadyCallback,
        View.OnClickListener, OnCompleteListener<Void> {

    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FragmentChangeGeolocationBinding mBinding;
    public static final String TAG = GeoFenceSettings.class.getName();
    private GoogleMap mMap;
    private Marker mMarker;
    private GeoLocation geoLocation;
    String[] geoProgessArray;
    private CircleOptions circleOptions;
    private Circle circle;
    private int userId;
    private String deviceId;
    private final int LEAVING_GEO_STATUS = 2;
    private final int RETURNING_GEO_STATUS = 1;
    private int CURRENT_GEO_STATUS = LEAVING_GEO_STATUS;
    private float RADIUS_OUT = 0.5f;
    private float RADIUS_IN = 5.0f;
    private int radiusProgressLeave = 0;
    private int radiusProgressReturn = 5;
    private int CIRCLE_RETURN_COLOR = Color.argb(112, 227, 198, 154);
    private int CIRCLE_LEAVE_COLOR = Color.argb(112, 152, 199, 184);
    private float MILES_TO_METERS_CONVERTER = 1609.34f;
    private GeofencingClient geofencingClient;
    private PendingIntent geoFenceIntent;
    private ArrayList<Geofence> geofences = new ArrayList();
    private String GEO_FENCE_RETURN_HOME_ID;
    private String GEO_FENCE_LEAVING_HOME_ID;
    private String GEO_FENCE_TOGGLE_ID;
    private String userName;
    private Handler uiHandler;
    private FusedLocationProviderClient mFusedLocationClient;
    private SupportMapFragment mapFragment;
    LocationSettingsRequest.Builder mLocationSettingsBuilder;
    SettingsClient mSettingsClient;
    Task<LocationSettingsResponse> mLocationSettingsTask;
    LocationManager locationManager;
    AtomicBoolean shouldSetCurrentLocation = new AtomicBoolean(false);
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 60 * 3;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 2000;
    public LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private double base = 400 / zoomToDistFactor(15);
    private View myLocationButton;
    private LatLng currentLatLng;
    private USER_STATUS userStatus;
    private String GEO_EXIT_OUT_ID;
    private String GEO_EXIT_IN_ID;
    private Handler mUiHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHandler = new Handler(Looper.getMainLooper());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_geolocation, container, false);
        mBinding = FragmentChangeGeolocationBinding.bind(view);
        return mBinding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initClickListner();
        geoProgessArray = getActivity().getResources().getStringArray(R.array.geoProgress);
        userName = "1232";
        userId = 1;
        deviceId = "123";
        GEO_FENCE_RETURN_HOME_ID = userName + String.valueOf(userId) + deviceId + "HOME_RETURN";
        GEO_EXIT_IN_ID = userName + String.valueOf(userId) + deviceId + "HOME_LEAVE_OUT";
        GEO_EXIT_OUT_ID = userName + String.valueOf(userId) + deviceId + "HOME_LEAVE_ENTER";
        GEO_FENCE_TOGGLE_ID = userName + String.valueOf(userId) + deviceId;
        initGoogleMap();
    }


    private enum USER_STATUS {
        ATHOME, OUTOFHOME
    }


    public static GeoFenceSettings getInstance(Bundle bundle) {
        GeoFenceSettings geoLocationFragment = new GeoFenceSettings();
        geoLocationFragment.setArguments(bundle);
        return geoLocationFragment;
    }

    @SuppressLint("RestrictedApi")
    private LocationRequest getLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        }
        return mLocationRequest;
    }


    private void initGoogleMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (((View) mapFragment.getView() != null && ((View) mapFragment.getView().findViewById(Integer.parseInt("1")) != null && ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent() != null)))) {
            myLocationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            if (myLocationButton != null)
                myLocationButton.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        geofences = new ArrayList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationSettingsBuilder = new LocationSettingsRequest.Builder();
        mLocationSettingsBuilder.addLocationRequest(getLocationRequest());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());
        mLocationSettingsTask = mSettingsClient.checkLocationSettings(mLocationSettingsBuilder.build());
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        createLocationCallback();
    }

    @SuppressLint("MissingPermission")
    private void addGeofences(ArrayList<Geofence> geofenceArrayList) {
        if (getGeofencePendingIntent() != null) {
            geofencingClient.addGeofences(getGeofencingRequest(geofenceArrayList), getGeofencePendingIntent())
                    .addOnCompleteListener(this);
        }

    }


    public Geofence getEnterGeofence(GeoLocation geoLocation) {
        Geofence enterGeoFence = new Geofence.Builder()
                .setRequestId(GEO_FENCE_RETURN_HOME_ID)
                .setCircularRegion(
                        geoLocation.getLatitude(),
                        geoLocation.getLongitude(),
                        (geoLocation.getRadiusIn() * MILES_TO_METERS_CONVERTER) / 2)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setNotificationResponsiveness(5000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
        return enterGeoFence;

    }

    public Geofence getLeaveOutGeofence(GeoLocation geoLocation) {
        Geofence leaveGeoFence = new Geofence.Builder()
                .setRequestId(GEO_EXIT_OUT_ID)
                .setCircularRegion(
                        geoLocation.getLatitude(),
                        geoLocation.getLongitude(), (geoLocation.getRadiusOut() * MILES_TO_METERS_CONVERTER) / 2)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setNotificationResponsiveness(5000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        return leaveGeoFence;

    }

    public Geofence getLeaveEnterGeofence(GeoLocation geoLocation) {
        Geofence leaveGeoFence = new Geofence.Builder()
                .setRequestId(GEO_EXIT_IN_ID)
                .setCircularRegion(
                        geoLocation.getLatitude(),
                        geoLocation.getLongitude(),
                        ((geoLocation.getRadiusOut() * MILES_TO_METERS_CONVERTER) / 2) - 100) // 100 is the offset
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setNotificationResponsiveness(5000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
        return leaveGeoFence;

    }


    public void updateGeofences(ArrayList<Geofence> geofenceArrayList) {
        if (getActivity() != null) {
            mLocationSettingsTask.addOnCompleteListener(getActivity(), new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        LocationSettingsStates states = response.getLocationSettingsStates();
                        addGeofences(geofenceArrayList);

                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    GeoFenceSettings.this.startIntentSenderForResult(resolvable.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null);
                                } catch (IntentSender.SendIntentException e) {
                                } catch (ClassCastException e) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                break;
                        }
                    }
                }

            });


        }
    }


    private boolean getUserStatus(GeoLocation geoLocation) {

        LatLng geoLocationLatLng = new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude());
        LatLng currentLocation = currentLatLng;
        Float distance = GlobalUtility.getDistanceFromLatLonInKm(geoLocationLatLng, currentLocation, geoLocation);
        Float radius = geoLocation.getRadiusOut() * MILES_TO_METERS_CONVERTER;
        boolean isHome = userStatus == USER_STATUS.ATHOME;
        if (distance != null && radius != null) {
            Float difference = Math.abs(distance - radius);
            if (difference > 100.0f) {
                isHome = !(distance > radius);
                userStatus = isHome ? USER_STATUS.ATHOME : USER_STATUS.OUTOFHOME;
            }
        }

        return isHome;

    }


    private GeofencingRequest getGeofencingRequest(ArrayList<Geofence> geofences) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.addGeofences(geofences);
        builder.setInitialTrigger(0);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {

        if (geoFenceIntent != null) {
            return geoFenceIntent;
        }
        if (getActivity() != null) {
            geoFenceIntent = PendingIntent.getBroadcast(getActivity(), 0, prepareBroadcastIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return geoFenceIntent;
    }

    private Intent prepareBroadcastIntent() {
        Intent intent = new Intent(getActivity(), GeofenceBroadcastReceiver.class);
        int userId = 1;
        String userName = "123";
        String password = "123";
        intent.putExtra("PREF_USERNAME", userName);
        intent.putExtra("PREF_UID", userId);
        intent.putExtra("DEVICE_ID", deviceId);
        intent.putExtra("PREF_PASSWORD", password);
        return intent;
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);

        super.onDestroyView();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (geoLocation != null) {
                            addGeofences(geofences);
                        } else {
                            startLocationUpdates();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        break;
                }
                break;
        }

    }


    @Override
    public void onPause() {
        Log.e(TAG, "onPause ");
        stopLocationUpdates();
        super.onPause();

    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @SuppressLint("MissingPermission")
    private void setGeoLocationUI(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
        setMapUI(geoLocation);
        setGeofences();


    }

    private void setGeofences() {
        if (geoLocation != null) {
            geofences.clear();
            geofences.add(getEnterGeofence(geoLocation));
            geofences.add(getLeaveEnterGeofence(geoLocation));
            geofences.add(getLeaveOutGeofence(geoLocation));
            updateGeofences(geofences);


        }

    }


    private void setSeekbarProgress(GeoLocation geoLocation) {
        mBinding.geoLocationBottomLl.seekBarGeoLocation.setOnSeekBarChangeListener(null);
        if (CURRENT_GEO_STATUS == LEAVING_GEO_STATUS) {
            mBinding.geoLocationBottomLl.seekBarGeoLocation.setProgress((int) geoLocation.getGeoSeekProgressLeave());
            mBinding.geoLocationBottomLl.geoLocationRadiusTv.setText(String.valueOf(geoLocation.getRadiusOut()) + " " + "miles");
        } else {
            mBinding.geoLocationBottomLl.seekBarGeoLocation.setProgress((int) geoLocation.getGeoSeekProgressReturn());
            mBinding.geoLocationBottomLl.geoLocationRadiusTv.setText(String.valueOf(geoLocation.getRadiusIn()) + " " + "miles");
        }
        mBinding.geoLocationBottomLl.seekBarGeoLocation.setOnSeekBarChangeListener(radiusChangeListener);


    }

    private void setMapUI(GeoLocation geoLocation) {
        LatLng latLng = new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude());
        addCircle(getCircleOptions(geoLocation, CURRENT_GEO_STATUS == RETURNING_GEO_STATUS));
        updateCurrentMarker(latLng);
    }

    public CircleOptions getCircleOptions(GeoLocation geoLocation, boolean isReturn) {
        int argbColor = isReturn ? CIRCLE_RETURN_COLOR : CIRCLE_LEAVE_COLOR;
        circleOptions = new CircleOptions()
                .center(new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude()))
                .radius(isReturn ? geoLocation.getRadiusIn() * MILES_TO_METERS_CONVERTER : geoLocation.getRadiusOut() * MILES_TO_METERS_CONVERTER)
                .fillColor(argbColor).strokeColor(Color.TRANSPARENT);
        return circleOptions;
    }

    private void updateCurrentMarker(LatLng latLng) {
        if (mMarker == null) {
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        } else {
            mMarker.setPosition(latLng);
        }
        setCameraPosition(latLng, circle);


    }

    public void addCircle(CircleOptions circleOptions) {
        if (circle != null)
            circle.remove();
        circle = mMap.addCircle(circleOptions);
    }


    public void setCameraPosition(LatLng latLng, Circle circle) {
        CameraUpdate currentZoomLevel = getZoomForDistance(latLng, circle.getRadius());
        mMap.moveCamera(currentZoomLevel);

    }


    private void initClickListner() {
        mBinding.geoLocationBottomLl.selectRecipe.setOnClickListener(this);
        mBinding.geoLocationBottomLl.userContainer.setOnClickListener(this);
        mBinding.myLocationButton.setOnClickListener(this);
        mBinding.geoLocationBottomLl.triggerLeavingRadioBtn.setOnClickListener(this);
        mBinding.geoLocationBottomLl.triggerReturnRadioBtn.setOnClickListener(this);
    }


    public void setCameraPositionListener() {
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (circle != null) {
                    CameraPosition cameraPosition = mMap.getCameraPosition();
                    LatLng center = cameraPosition.target;
                    float z2 = cameraPosition.zoom;
                    double newR = base * zoomToDistFactor(z2);
                    circle.setRadius(newR);


                }
            }
        });

    }

    private double zoomToDistFactor(double z) {
        return Math.pow(2, 8 - z) / 1.6446;
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMarkerDragListener(markerDragListenr);
        mMap.setOnMapClickListener(onMapClickListenr);
        setCameraPositionListener();
        if (getActivity() != null) {
            if (MSupport.checkOrRequestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSIONS_REQUEST_CODE)) {
                shouldSetCurrentLocation.set(geoLocation == null);
                if (geoLocation != null) {
                    setSeekbarProgress(geoLocation);
                    setGeoLocationUI(geoLocation);
                }
                mLocationSettingsTask.addOnCompleteListener(getActivity(), new OnCompleteListener<LocationSettingsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                        try {
                            LocationSettingsResponse response = task.getResult(ApiException.class);
                            startLocationUpdates();
                        } catch (ApiException exception) {
                            switch (exception.getStatusCode()) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                                        GeoFenceSettings.this.startIntentSenderForResult(resolvable.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null);
                                    } catch (IntentSender.SendIntentException e) {
                                    } catch (ClassCastException e) {
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    }

                });

            }


        }


    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, Looper.myLooper());
//        if (mService != null) mService.requestLocationUpdates();
    }


    private void setCurrentGeoLocation(LatLng location) {
        if (location != null) {
            geoLocation = getCurrentGeoLocation(location);
            setGeoLocationUI(geoLocation);
            setSeekbarProgress(geoLocation);
        }
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (currentLatLng == null)
                    currentLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                if (shouldSetCurrentLocation.get()) {
                    if (mapFragment != null && mMap != null) {
                        setCurrentGeoLocation(currentLatLng);

                    }

                }

            }
        };
    }

    public GeoLocation getCurrentGeoLocation(LatLng location) {
        if (geoLocation == null)
            geoLocation = new GeoLocation();
        geoLocation.setTransitionType(CURRENT_GEO_STATUS);
        geoLocation.setExpirationDuration(Geofence.NEVER_EXPIRE);
        geoLocation.setReturnGeoFenceId(GEO_FENCE_RETURN_HOME_ID);
        geoLocation.setLeaveGeoFenceId(GEO_FENCE_LEAVING_HOME_ID);
        geoLocation.setDeviceId(deviceId);
        geoLocation.setGeoSeekProgressReturn(geoLocation.getGeoSeekProgressReturn() == 0 ? radiusProgressReturn : geoLocation.getGeoSeekProgressReturn());
        geoLocation.setGeoSeekProgressLeave(geoLocation.getGeoSeekProgressLeave() == 0 ? radiusProgressLeave : geoLocation.getGeoSeekProgressLeave());
        geoLocation.setRadiusOut(geoLocation.getRadiusOut() == 0.0f ? RADIUS_OUT : geoLocation.getRadiusOut());
        geoLocation.setRadiusIn(geoLocation.getRadiusIn() == 0.0f ? RADIUS_IN : geoLocation.getRadiusIn());
        geoLocation.setId(GEO_FENCE_TOGGLE_ID);
        geoLocation.setLatitude(location.latitude);
        geoLocation.setLongitude(location.longitude);
        return geoLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                resetMap();
            } else {
                if (getActivity() != null) {
//                    GlobalUtility.showSnackbar(getActivity(), R.string.permission_denied_explanation, R.string.settings,
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
////                                    GlobalUtility.goToSettings(getActivity());
//
//                                }
//                            });
                }

            }
        }

    }

    @Override
    public void onClick(View v) {

    }


    private SeekBar.OnSeekBarChangeListener radiusChangeListener = new SeekBar.OnSeekBarChangeListener() {
        float radius = 0.0f;
        int seekBarProgress = 0;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mBinding.geoLocationBottomLl.geoLocationRadiusTv.setText(geoProgessArray[progress] + " miles");
            try {
                radius = Float.parseFloat(geoProgessArray[progress]);

            } catch (NumberFormatException e) {
                radius = 0.5f;
            }
            seekBarProgress = progress;

        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (geoLocation != null) {
                if (CURRENT_GEO_STATUS == LEAVING_GEO_STATUS) {
                    RADIUS_OUT = radius;
                    geoLocation.setGeoSeekProgressLeave(seekBarProgress);
                    geoLocation.setRadiusOut(RADIUS_OUT);
                } else {
                    RADIUS_IN = radius;
                    geoLocation.setGeoSeekProgressReturn(seekBarProgress);
                    geoLocation.setRadiusIn(RADIUS_IN);
                }
                setGeoLocationUI(geoLocation);
            }


        }
    };


    public void resetMap() {
        if (mapFragment == null) {
            initGoogleMap();
            mBinding.geoLocationBottomLl.triggerLeavingRadioBtn.setChecked(true);
        } else {
            if (mMap == null) {
                mapFragment.getMapAsync(this);
            } else {
                onMapReady(mMap);
            }
        }

    }

    private CameraUpdate getZoomForDistance(LatLng originalPosition, double distance) {
        LatLng rightBottom = SphericalUtil.computeOffset(originalPosition, distance, 135);
        LatLng leftTop = SphericalUtil.computeOffset(originalPosition, distance, -45);
        LatLngBounds sBounds = new LatLngBounds(new LatLng(rightBottom.latitude, leftTop.longitude), new LatLng(leftTop.latitude, rightBottom.longitude));
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);
        return CameraUpdateFactory.newLatLngBounds(sBounds, width, height, padding);

    }


    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task != null && task.isSuccessful()) {

            geoLocation.setId(GEO_FENCE_TOGGLE_ID);
            geoLocation.setUserName(userName);

            Log.d(TAG, "onComplete: Geofence Registered");


        }

    }


    private void setMarker(LatLng latLng) {
        if (mMarker == null) {
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        } else {
            mMarker.setPosition(latLng);
        }
    }

    private GoogleMap.OnMapClickListener onMapClickListenr = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            shouldSetCurrentLocation.set(false);
            if (geoLocation != null) {
                geoLocation.setLatitude(latLng.latitude);
                geoLocation.setLongitude(latLng.longitude);
                setMarker(latLng);
                setCirle(latLng);
            }
        }
    };
    private GoogleMap.OnMarkerDragListener markerDragListenr = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            mMarker = marker;
            LatLng latLng = marker.getPosition();
            shouldSetCurrentLocation.set(false);
            if (geoLocation != null) {
                geoLocation.setLatitude(latLng.latitude);
                geoLocation.setLongitude(latLng.longitude);
                setMarker(latLng);
                setCirle(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

        }
    };

    public void setCirle(LatLng latLng) {
        if (circle != null) {
            uiHandler.postDelayed(() -> {
                circle.setCenter(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }, 200);


        }

    }


}
