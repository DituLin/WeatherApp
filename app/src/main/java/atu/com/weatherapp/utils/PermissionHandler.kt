package atu.com.weatherapp.utils

/**
 * Created by atu on 2017/8/10.
 */

abstract class PermissionHandler {
    /**
     * 权限通过
     */
    abstract fun onGranted()

    /**
     * 权限拒绝
     */
    open fun onDenied() {}

    /**
     * 不再询问

     * @return 如果要覆盖原有提示则返回true
     */
    open fun onNeverAsk(): Boolean {
        return false
    }
}
