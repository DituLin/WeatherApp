package atu.com.weatherapp.domain.datasource

import atu.com.weatherapp.data.server.WeatherServer
import atu.com.weatherapp.domain.model.Weather

/**
 * Created by atu on 2017/8/10.
 */
class WeatherProvider(val source: WeatherDataSource = WeatherProvider.SOURCES) {

    companion object {
        val SOURCES by lazy { WeatherServer() }//数据库、网络请求 数据
    }

    fun requestNowWeather(latLng: String): Weather{
        return source.requestNowWeather(latLng)!!
    }


}