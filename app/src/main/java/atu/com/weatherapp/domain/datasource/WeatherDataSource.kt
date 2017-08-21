package atu.com.weatherapp.domain.datasource

import atu.com.weatherapp.domain.model.Weather

/**
 * Created by atu on 2017/8/10.
 */
interface WeatherDataSource {

    fun requestNowWeather(latLng: String): Weather?
}