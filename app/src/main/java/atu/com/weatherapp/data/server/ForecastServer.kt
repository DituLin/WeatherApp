package atu.com.weatherapp.data.server

import atu.com.weatherapp.data.db.ForecastDb
import atu.com.weatherapp.domain.datasource.ForecastDataSource
import atu.com.weatherapp.domain.model.ForecastList

/**
 *
 * 获取网络数据存入数据库，再从数据库读取
 * Created by atu on 2017/7/26.
 */
class ForecastServer(val serverDateMapper: ServerDataMapper = ServerDataMapper(),val forecastDb: ForecastDb = ForecastDb() ) : ForecastDataSource {

    override fun requestForecastByZipCode(zipCode: Long, date: Long): ForecastList? {
        val result = ForecastRequest(zipCode).execute()
        val converted = serverDateMapper.convertFromDataModel(zipCode,result)
        forecastDb.saveForecast(converted)
        return forecastDb.requestForecastByZipCode(zipCode,date)
    }

    override fun requestDayForecast(id: Long) = throw UnsupportedOperationException()
}