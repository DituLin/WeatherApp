package atu.com.weatherapp.domain.command

import atu.com.weatherapp.domain.datasource.ForecastProvider
import atu.com.weatherapp.domain.model.Forecast

/**
 * Created by atu on 2017/7/26.
 */
class RequestDayForecastCommand(val id: Long,val forecastProvider: ForecastProvider = ForecastProvider()) : Command<Forecast> {
    override fun execute(): Forecast = forecastProvider.requestForecast(id)
}