package atu.com.weatherapp.data.db

import atu.com.weatherapp.domain.datasource.ForecastDataSource
import atu.com.weatherapp.domain.model.ForecastList
import atu.com.weatherapp.extensions.clear
import atu.com.weatherapp.extensions.parseList
import atu.com.weatherapp.extensions.parseOpt
import atu.com.weatherapp.extensions.toVarargArray
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * Created by atu on 2017/7/26.
 */
class ForecastDb(val forecastDbHelper: ForecastDbHelper = ForecastDbHelper.instance,
                 val dataMapper: DbDataMapper = DbDataMapper()) : ForecastDataSource{


    override fun requestForecastByZipCode(zipCode: Long, date: Long) = forecastDbHelper.use {

        val dailyRequest = "${DayForecastTable.CITY_ID} = ? "+"AND ${DayForecastTable.DATE} >= ?"

        val dailyForecast = select(DayForecastTable.NAME)
                .whereSimple(dailyRequest, zipCode.toString(), date.toString())
                .parseList { DayForecast(HashMap(it)) }

        val city = select(CityForecastTable.NAME)
                .whereSimple("${CityForecastTable.ID} = ?",zipCode.toString())
                .parseOpt { CityForecast(HashMap(it), dailyForecast) }

        city?.let { dataMapper.convertToDomain(it) }
    }

    override fun requestDayForecast(id: Long) = forecastDbHelper.use {
        val forecat = select(DayForecastTable.NAME)
                .whereSimple("_id = ?",id.toString())
                .parseOpt { DayForecast(HashMap(it)) }

        forecat?.let { dataMapper.convertDayToDomain(it) }
    }


    //保存数据库中
    fun saveForecast(forecastList: ForecastList) = forecastDbHelper.use {

        clear(CityForecastTable.NAME)
        clear(DayForecastTable.NAME)

        with(dataMapper.convertFromDomain(forecastList)) {
            insert(CityForecastTable.NAME, *map.toVarargArray())
            dailyForecast.forEach { insert(DayForecastTable.NAME, *it.map.toVarargArray()) }

        }
    }




}