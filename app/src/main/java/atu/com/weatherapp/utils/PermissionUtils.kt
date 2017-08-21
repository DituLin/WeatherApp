package atu.com.weatherapp.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
import android.support.v4.util.SimpleArrayMap

/**
 * Created by atu on 2017/8/10.
 */

object PermissionUtils {

    // Map of dangerous permissions introduced in later framework versions.
    // Used to conditionally bypass permission-hold checks on older devices.
    private val MIN_SDK_PERMISSIONS: SimpleArrayMap<String, Int> = SimpleArrayMap<String, Int>(8)

    init {
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14)
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20)
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9)
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23)
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23)
    }

    @Volatile private var targetSdkVersion = -1

    /**
     * Checks all given permissions have been granted.

     * @param grantResults results
     * *
     * @return returns true if all permissions have been granted.
     */
    fun verifyPermissions(grantResults: IntArray): Boolean {
        if (grantResults.size == 0) {
            return false
        }
        return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
    }

    /**
     * Returns true if the Activity or Fragment has access to all given permissions.

     * @param context     context
     * *
     * @param permissions permission list
     * *
     * @return returns true if the Activity or Fragment has access to all given permissions.
     */
    fun hasSelfPermissions(context: Context, permissions: Array<String>): Boolean {
        val result = permissions.none { permissionExists(it) && !hasSelfPermission(context, it) }
        return result
    }

    /**
     * Returns true if the permission exists in this SDK version

     * @param permission permission
     * *
     * @return returns true if the permission exists in this SDK version
     */
    private fun permissionExists(permission: String): Boolean {
        // Check if the permission could potentially be missing on this device
        val minVersion = MIN_SDK_PERMISSIONS.get(permission)
        // If null was returned from the above call, there is no need for a device API level check for the permission;
        // otherwise, we check if its minimum API level requirement is met
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion
    }

    /**
     * Determine context has access to the given permission.
     *
     *
     * This is a workaround for RuntimeException of Parcel#readException.
     * For more detail, check this issue https://github.com/hotchemi/PermissionsDispatcher/issues/107

     * @param context    context
     * *
     * @param permission permission
     * *
     * @return returns true if context has access to the given permission, false otherwise.
     * *
     * @see .hasSelfPermissions
     */
    private fun hasSelfPermission(context: Context, permission: String): Boolean {
        try {
            return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        } catch (t: RuntimeException) {
            return false
        }

        //        boolean result = true;
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //
        //            if (getTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
        //                // targetSdkVersion >= Android M, we can use Context#checkSelfPermission
        //                result = ContextCompat.checkSelfPermission(context, permission)
        //                        == PackageManager.PERMISSION_GRANTED;
        //            } else {
        //                // targetSdkVersion < Android M, we have to use PermissionChecker
        //                result = PermissionChecker.checkSelfPermission(context, permission)
        //                        == PermissionChecker.PERMISSION_GRANTED;
        //            }
        //        }
        //        return result;
    }

    /**
     * Checks given permissions are needed to show rationale.

     * @param object    Activity/Fragment
     * *
     * @param permissions permission list
     * *
     * @return returns true if one of the permission is needed to show rationale.
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun shouldShowRequestPermissionRationale(obj: Any,permissions: Array<out String>): Boolean {
        for (permission in permissions) {
            if (obj is Activity) {
                return ActivityCompat.shouldShowRequestPermissionRationale(obj, permission)
            } else if (obj is Fragment) {
                return obj.shouldShowRequestPermissionRationale(permission)
            } else if (obj is android.app.Fragment) {
                return obj.shouldShowRequestPermissionRationale(permission)
            } else {
                return false
            }
        }
        return false
    }

    /**
     * Get target sdk version.

     * @param context context
     * *
     * @return target sdk version
     */
    fun getTargetSdkVersion(context: Context): Int {
        try {
            if (targetSdkVersion != -1) {
                return targetSdkVersion
            }
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        return targetSdkVersion
    }

}
