package atu.com.weatherapp.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import atu.com.weatherapp.utils.PermissionHandler
import atu.com.weatherapp.utils.PermissionUtils
import org.jetbrains.anko.toast

abstract class BaseActivity : AppCompatActivity() {

    private var mHandler: PermissionHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    /**
     * 请求权限
     */
    fun requestPermission(permissions: Array<String>, handler: PermissionHandler) {
        if (PermissionUtils.hasSelfPermissions(this,permissions)){
            handler.onGranted()
        }else {
            mHandler = handler
            ActivityCompat.requestPermissions(this,permissions,1)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (mHandler == null)return

        if (PermissionUtils.verifyPermissions(grantResults)) {
            mHandler!!.onGranted()
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
                if (!mHandler!!.onNeverAsk()) {
                  toast("")
                }

            } else {
                mHandler!!.onDenied()
            }
        }
    }
}
