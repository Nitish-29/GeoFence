package com.example.geofence;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MSupport {

    public static final int REQUEST_CODE_SETTINGS = 171;

    public static boolean isMSupportDevice(Context ctx) {
        /**
         * @return true in case of M Device,
         * false in case of below M devices
         */
        return Build.VERSION.SDK_INT >= MSupportConstants.SDK_VERSION;
    }

    /**
     * for the activity
     *
     * @param mActivity      Calling activity Context
     * @param permissionName for which permission is needed for performing the perticular funtion
     * @param requestcode    request code to identify the request
     * @return true in case of permission is granted
     * false in case of permission is not granted
     * in case of false we have to request that permission
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkOrRequestPermission(Activity mActivity, String permissionName, int requestcode) {

        if (MSupport.isMSupportDevice(mActivity)) {
            if (ContextCompat.checkSelfPermission(mActivity, permissionName)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                mActivity.requestPermissions(new String[]{permissionName}, requestcode);
                return false;
            }
        } else
            return true;
    }

    /**
     * for the fragment
     *
     * @param f              Calling activity Context
     * @param permissionName for which permission is needed for performing the perticular funtion
     * @param requestcode    request code to identify the request
     * @return true in case of permission is granted
     * false in case of permission is not granted
     * in case of false we have to request that permission
     */
    public static boolean checkOrRequestPermission(Fragment f, String permissionName, int requestcode) {

        if (MSupport.isMSupportDevice(f.getActivity())) {
            if (ContextCompat.checkSelfPermission(f.getActivity(), permissionName)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                f.requestPermissions(new String[]{permissionName}, requestcode);
                return false;
            }
        } else
            return true;
    }

    /**
     * MEthod to check permissions set and in this method we can
     * send the bunch of permission we needed to perform
     * the perticular function
     *
     * @param mActivity     Calling activity context
     * @param permissionSet Permission set to check
     * @param requestCode   request code
     * @return true in case of permission is granted or pre marshmallow
     * false in case of permission is not granted
     * in case of false we have to request that permission
     */
    @TargetApi(23)
    public static boolean checkPermission(Activity mActivity, String[] permissionSet, int requestCode) {

        if (MSupport.isMSupportDevice(mActivity)) {

            List<String> permissions = new ArrayList<>();
            for (String aPermissionSet : permissionSet) {
                int hasPermission = ContextCompat.checkSelfPermission(mActivity, aPermissionSet);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(aPermissionSet);
                }
            }
            if (!permissions.isEmpty()) {

                mActivity.requestPermissions(permissions.toArray(new String[permissions.size()]), requestCode);
                return false;
            } else
                return true;

        } else
            return true;
    }


    public static void goToSettings(Context context, int requestCode) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity) context).startActivityForResult(myAppSettings, requestCode);
    }
}

