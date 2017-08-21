package atu.com.weatherapp.data.server

import atu.com.weatherapp.domain.datasource.WeatherDataSource
import atu.com.weatherapp.domain.model.Weather

/**
 * Created by atu on 2017/8/10.
 */
class WeatherServer(val serverDateMapper: ServerDataMapper = ServerDataMapper()) : WeatherDataSource {

    override fun requestNowWeather(latLng: String): Weather? {
        val result = WeatherRequest(latLng).execute()
        val converted = serverDateMapper.convertFromWeatherDataModel(result)
        return converted
    }
}