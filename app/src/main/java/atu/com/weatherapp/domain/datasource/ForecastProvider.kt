package atu.com.weatherapp.domain.datasource

import atu.com.weatherapp.data.db.ForecastDb
import atu.com.weatherapp.data.server.ForecastServer
import atu.com.weatherapp.domain.model.Forecast
import atu.com.weatherapp.domain.model.ForecastList
import atu.com.weatherapp.extensions.firstResult

/**
 * Created by atu on 2017/7/26.
 */
class ForecastProvider(val sources: List<ForecastDataSource> = ForecastProvider.SOURCES) {

    companion object {
        val DAY_IN_MILLIS = 1000 * 60 * 60 * 24
        val SOURCES by lazy {listOf(ForecastDb(), ForecastServer())}//数据库、网络请求 数据
    }

    fun requestByZipCode(zipCode: Long,days: Int): ForecastList = requestToSources {
        val res = it.requestForecastByZipCode(zipCode,todayTimeSpan())
        if (res != null && res.size >= days) res else null
    }

    fun requestForecast(id: Long): Forecast = requestToSources {
        it.requestDayForecast(id)
    }


    private fun <T : Any>requestToSources(f: (ForecastDataSource) -> T?): T = sources.firstResult{f(it)}

    private fun todayTimeSpan() = System.currentTimeMillis() / DAY_IN_MILLIS * DAY_IN_MILLIS
}