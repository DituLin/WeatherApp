package atu.com.weatherapp.utils

import android.content.Context
import android.util.Log

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/**
 * Created by atu on 2017/8/10.
 */

object GDLocationUtils {


    private var mlocationClient: AMapLocationClient? = null
    var mLocationOption: AMapLocationClientOption? = null
    var sLocation: AMapLocation? = null
    /**

     * @Title: init
     * *
     * @Description: 初始化地图导航，在Application onCreate中调用，只需调用一次
     * *
     * @param context
     */
    fun init(context: Context) {
        // 声明mLocationOption对象
        mlocationClient = AMapLocationClient(context)
        // 初始化定位参数
        mLocationOption = AMapLocationClientOption()
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption!!.interval = 2 * 3600 * 1000
        // 设置定位参数
        mlocationClient!!.setLocationOption(mLocationOption)
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    }

    /**

     * @ClassName: MyLocationListener
     * *
     * @Description: 定位结果回调
     */
    interface NGLocationListener {
        fun result(location: AMapLocation?)
    }

    /**

     * @Title: getLocation
     * *
     * @Description: 获取位置，如果之前获取过定位结果，则不会重复获取
     * *
     * @param listener
     */
    fun getLocation(listener: NGLocationListener) {
        if (sLocation == null) {
            getCurrentLocation(listener)
        } else {
            listener.result(sLocation!!)
        }
    }

    /**

     * @Title: getCurrentLocation
     * *
     * @Description: 获取位置，重新发起获取位置请求
     * *
     * @param listener
     */
    fun getCurrentLocation(listener: NGLocationListener) {
        if (mlocationClient == null) {
            return
        }
        // 设置定位监听
        mlocationClient!!.setLocationListener { location ->
            if (location != null) {
                if (location.errorCode == 0) {
                    //定位成功，取消定位
                    mlocationClient!!.stopLocation()
                    sLocation = location
                }else{
                    //获取定位数据失败
                    sLocation = null
                }

            } else {
                //获取定位数据失败
                sLocation = null
            }

            listener.result(sLocation)
        }
        // 启动定位
        mlocationClient!!.startLocation()
    }

    /**

     * @Title: destroy
     * *
     * @Description: 销毁定位，必须在退出程序时调用，否则定位会发生异常
     */
    fun destroy() {
        if (mlocationClient != null) {
            mlocationClient!!.onDestroy()
        }
    }
}
