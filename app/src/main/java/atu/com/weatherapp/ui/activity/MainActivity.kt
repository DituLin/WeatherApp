package atu.com.weatherapp.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import atu.com.dueroslib.http.HttpConfig
import atu.com.dueroslib.oauth.api.BaiduDialog
import atu.com.dueroslib.oauth.api.BaiduDialogError
import atu.com.dueroslib.oauth.api.BaiduException
import atu.com.dueroslib.oauth.api.BaiduOauthImplicitGrant
import atu.com.weatherapp.R
import atu.com.weatherapp.base.BaseActivity
import atu.com.weatherapp.dcs.DcsService
import atu.com.weatherapp.dcs.OauthImpl
import atu.com.weatherapp.domain.command.RequestWeatherCommand
import atu.com.weatherapp.domain.model.Weather
import atu.com.weatherapp.utils.GDLocationUtils
import atu.com.weatherapp.utils.PermissionHandler
import com.amap.api.location.AMapLocation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : BaseActivity() {

    companion object {
        val CLIENT_ID = "NQFpplss5DRzIjONpx91Yv1d8pCGhWMj"
    }

    var latLng: String = "120.022016,30.294284"//杭州
    // 是否每次授权都强制登陆
    val isForceLogin: Boolean = false
    // 是否每次都确认登陆
    val isConfirmLogin: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("MainActivity","onCreate")
        autoOAuth()
        requestPermission()
        digitalTubeView.start()
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("MainActivity","onRestart")
        initOauth()
    }


    private fun loadForecast(latLng: String) = doAsync {
        val res = RequestWeatherCommand(latLng).execute()
        uiThread {
            bindForecast(res)
        }
    }

    private fun bindForecast(weather: Weather) = with(weather) {
        Picasso.with(ctx).load(iconUrl).into(iv_icon)
        tv_weatherTem.text = tmp.toString() + "°C"
        tv_weatherDescription.text = description
    }


    /**
     * Oauth
     */
    fun initOauth() {
        val oauth = OauthImpl()
        if (oauth.isSessionValid) {
            Log.e("MainActivity","OauthImpl")
            HttpConfig.accessToken = oauth.accessToken
        }else{
            oauth.authorize()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e("MainActivity","onDestroy")
        GDLocationUtils.destroy()
        intent = Intent(this,DcsService::class.java)
        stopService(intent)
    }


    fun autoOAuth(){
        val baiDuGrant = BaiduOauthImplicitGrant(CLIENT_ID,ctx)
        baiDuGrant.authorize(this,isForceLogin,isConfirmLogin,object: BaiduDialog.BaiduDialogListener{
            override fun onComplete(values: Bundle?) {
                toast("登录成功")
                initOauth()
                requestAudioPermission()
            }

            override fun onBaiduException(e: BaiduException?) {
                Log.e("onBaiduException",e?.message)
            }

            override fun onError(e: BaiduDialogError?) {
                toast(e?.message ?: "网络连接异常")
            }

            override fun onCancel() {
                Log.e("Cancel","Cancel")
            }

        })
    }



    /**
     * 权限申请
     */
    fun requestPermission() {
        requestPermission(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), object : PermissionHandler() {
            override fun onGranted() {
                GDLocationUtils.init(ctx)
                GDLocationUtils.getLocation(object : GDLocationUtils.NGLocationListener {
                    override fun result(location: AMapLocation?) {
                        if (location != null) {
                            latLng = location.longitude.toString() + "," + location.latitude.toString()
                        }
                        loadForecast(latLng)
                        Log.e("location", latLng)
                    }
                })
            }

            override fun onDenied() {
                loadForecast(latLng)
                toast("权限被拒绝")
                finish()
            }

            override fun onNeverAsk(): Boolean {
                android.app.AlertDialog.Builder(ctx)
                        .setTitle("权限申请")
                        .setMessage("设置-应用-权限开启相关权限")
                        .setPositiveButton("开启", { dialog, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", ctx.packageName, null)
                            intent.data = uri
                            startActivity(intent)
                            dialog.dismiss()
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .show()
                return true
            }

        })
    }

    fun requestAudioPermission(){
        requestPermission(arrayOf(Manifest.permission.RECORD_AUDIO), object : PermissionHandler() {
            override fun onGranted() {
                startService<DcsService>()
            }

            override fun onDenied() {
                toast("权限被拒绝")
                finish()
            }

            override fun onNeverAsk(): Boolean {
                android.app.AlertDialog.Builder(ctx)
                        .setTitle("权限申请")
                        .setMessage("设置-应用-权限开启相关权限")
                        .setPositiveButton("开启", { dialog, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", ctx.packageName, null)
                            intent.data = uri
                            startActivity(intent)
                            dialog.dismiss()
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .show()
                return true
            }

        })
    }



}
