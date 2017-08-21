package atu.com.weatherapp.domain.datasource

import atu.com.weatherapp.domain.model.Forecast
import atu.com.weatherapp.domain.model.ForecastList

/**
 * Created by atu on 2017/7/26.
 */
interface ForecastDataSource {
    fun requestForecastByZipCode(zipCode: Long, date: Long): ForecastList?

    fun requestDayForecast(id: Long): Forecast?


}