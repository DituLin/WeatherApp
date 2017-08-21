package atu.com.weatherapp.domain.command

import atu.com.weatherapp.data.server.HeWeather
import atu.com.weatherapp.domain.datasource.WeatherProvider
import atu.com.weatherapp.domain.model.Weather

/**
 * Created by atu on 2017/8/10.
 */
class RequestWeatherCommand(val latLng: String,val provider: WeatherProvider = WeatherProvider()) : Command<Weather> {

    override fun execute(): Weather = provider.requestNowWeather(latLng)
}