package atu.com.weatherapp.domain.command

import atu.com.weatherapp.domain.model.ForecastList
import atu.com.weatherapp.domain.datasource.ForecastProvider

/**
 * Created by atu on 2017/7/23.
 */
class RequestForecastCommand(val zipCode: Long,val forecastProvider: ForecastProvider = ForecastProvider()) : Command<ForecastList> {

    override fun execute(): ForecastList =  forecastProvider.requestByZipCode(zipCode, DAYS)

    companion object {
        val DAYS = 1
    }

}