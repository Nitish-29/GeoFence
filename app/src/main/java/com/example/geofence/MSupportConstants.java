package com.example.geofence;

import android.Manifest;

public class MSupportConstants {
    /**
     * This Constants for SDK needed to be compare with
     */
    public static final int SDK_VERSION = 23;
    public static final int MY_PERMISSIONS_REQUEST_READ_CAMERA = 1;
    public static final int MY_PERMISSION_REQUEST_STORAGE_READ_WRITE = 2;
    public static final int MY_PERMISSION_SEND_SMS = 3;
    public static final int MY_PERMISSION_LOCATION = 4;
    public static final int MY_STORAGE_DB = 5;
    public static final int MY_PHONE_STATE = 6;
    public static final int MY_CAMERA = 7;
    public static final int MY_CONTACTS = 8;
    public static final int COARSE_LOCATION = 9;
    public static final int MY_EXTERNAL = 10;
    public static final int FINE_LOCATION = 11;


    /**
     * marshmallow permission list constants
     */
    public static final String SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String INTERNET_ACCESS_FULL = Manifest.permission.INTERNET;
    public static final String AUDIO_SETTING = Manifest.permission.MODIFY_AUDIO_SETTINGS;
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String BLUETOOTH = Manifest.permission.BLUETOOTH;
    public static final String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;
    public static final String WAKE_LOCK = Manifest.permission.WAKE_LOCK;
    public static final String GET_ACCOUNT = Manifest.permission.GET_ACCOUNTS;
    public static final String VIBRATE = Manifest.permission.VIBRATE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE;
    public static final String ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
}
