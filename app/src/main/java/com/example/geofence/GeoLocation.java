package com.example.geofence;


public class GeoLocation  {

    private String mId;
    private double mLatitude;
    private double mLongitude;
    public float mRadiusIn;
    public float mRadiusOut;
    private long mExpirationDuration;
    private int mTransitionType;
    private String deviceId;
    private String returnGeoFenceId;
    private String leaveGeoFenceId;
    private double geoSeekProgressLeave = 0.0;
    private double geoSeekProgressReturn = 5.0;
    private String userName;
    private String deviceName;

    public GeoLocation(String id, double lat, double lng, float radiusIN, long expirationDuration, int transitionType, String type, String outCommand, String inCommand, String devices, float radiusOut, String name, String deviceid) {
        this.mId = id;
        this.mLatitude = lat;
        this.mLongitude = lng;
        this.mRadiusIn = radiusIN;
        this.mRadiusOut = radiusOut;
        this.mExpirationDuration = expirationDuration;
        this.mTransitionType = transitionType;
        this.deviceName = "";
        this.geoSeekProgressLeave = radiusOut / 1000;
        this.geoSeekProgressReturn = radiusIN / 1000;
        this.userName = "123";
        this.deviceId = deviceid;
        this.returnGeoFenceId = "";
        this.leaveGeoFenceId = "";


    }


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLeaveRecipeName() {
        return leaveRecipeName;
    }

    public void setLeaveRecipeName(String leaveRecipeName) {
        this.leaveRecipeName = leaveRecipeName;
    }

    public String getReturnRecipeName() {
        return returnRecipeName;
    }

    public void setReturnRecipeName(String returnRecipeName) {
        this.returnRecipeName = returnRecipeName;
    }

    private String leaveRecipeName;
    private String returnRecipeName;

    @Override
    public String toString() {
        return "GeoLocation{" +
                "mId='" + mId + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mRadiusIn=" + mRadiusIn +
                ", mRadiusOut=" + mRadiusOut +
                ", mExpirationDuration=" + mExpirationDuration +
                ", mTransitionType=" + mTransitionType +
                ", deviceId='" + deviceId + '\'' +
                ", returnGeoFenceId='" + returnGeoFenceId + '\'' +
                ", leaveGeoFenceId='" + leaveGeoFenceId + '\'' +
                ", geoSeekProgressLeave=" + geoSeekProgressLeave +
                ", geoSeekProgressReturn=" + geoSeekProgressReturn +
                '}';
    }


    public double getGeoSeekProgressLeave() {
        return geoSeekProgressLeave;
    }

    public void setGeoSeekProgressLeave(double geoSeekProgressLeave) {
        this.geoSeekProgressLeave = geoSeekProgressLeave;
    }

    public double getGeoSeekProgressReturn() {
        return geoSeekProgressReturn;
    }

    public void setGeoSeekProgressReturn(double geoSeekProgressReturn) {
        this.geoSeekProgressReturn = geoSeekProgressReturn;
    }


    public String getReturnGeoFenceId() {
        return returnGeoFenceId;
    }

    public void setReturnGeoFenceId(String returnGeoFenceId) {
        this.returnGeoFenceId = returnGeoFenceId;
    }

    public String getLeaveGeoFenceId() {
        return leaveGeoFenceId;
    }

    public void setLeaveGeoFenceId(String leaveGeoFenceId) {
        this.leaveGeoFenceId = leaveGeoFenceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public float getRadiusIn() {
        return mRadiusIn;
    }

    public void setRadiusIn(float mRadiusIn) {
        this.mRadiusIn = mRadiusIn;
    }

    public float getRadiusOut() {
        return mRadiusOut;
    }

    public void setRadiusOut(float mRadiusOut) {
        this.mRadiusOut = mRadiusOut;
    }

    public long getExpirationDuration() {
        return mExpirationDuration;
    }

    public void setExpirationDuration(long mExpirationDuration) {
        this.mExpirationDuration = mExpirationDuration;
    }

    public int getTransitionType() {
        return mTransitionType;
    }

    public void setTransitionType(int mTransitionType) {
        this.mTransitionType = mTransitionType;
    }

    public GeoLocation() {
    }

}
