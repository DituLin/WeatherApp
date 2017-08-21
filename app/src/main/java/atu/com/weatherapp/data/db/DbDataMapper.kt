package atu.com.weatherapp.data.db

import atu.com.weatherapp.domain.model.Forecast
import atu.com.weatherapp.domain.model.ForecastList

/**
 * Created by atu on 2017/7/26.
 */
class DbDataMapper {

     fun convertFromDomain(forecastList: ForecastList) = with(forecastList) {
        val daily = dailyForecast.map { convertDayFromDomain(id,it) }
         CityForecast(id, city, country, daily)
    }


     fun convertDayFromDomain(cityId: Long,forecast: Forecast) = with(forecast) {
         DayForecast(date, description, high, low, iconUrl, cityId)
    }

    fun convertToDomain(forecast: CityForecast) = with(forecast) {
        val daily = dailyForecast.map { convertDayToDomain(it) }
        ForecastList(_id,city,country,daily)

    }

    fun convertDayToDomain(dayForecast: DayForecast) = with(dayForecast) {
        Forecast(_id,date,description,high,low,iconUrl)
    }
}