package atu.com.weatherapp.data.server

import com.google.gson.Gson

/**
 * 和风天气请求
 * Created by atu on 2017/8/10.
 */
class WeatherRequest(val latLng: String){

    companion object {
        private val APP_KEY = "580267e029d54780906be8fbda41b41e"
        private val HE_URL = "https://free-api.heweather.com/v5/now?"
        private val COMPLETE_HE_URL = "${HE_URL}key=${APP_KEY}&city="
    }

    fun execute(): HeWeatherResult {
        val forecastJsonStr = java.net.URL(COMPLETE_HE_URL + latLng).readText()
        return Gson().fromJson(forecastJsonStr, HeWeatherResult::class.java)
    }
}
