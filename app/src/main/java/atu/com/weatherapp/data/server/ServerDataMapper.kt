package atu.com.weatherapp.data.server

import android.widget.Toast
import atu.com.weatherapp.domain.model.ForecastList
import atu.com.weatherapp.domain.model.Forecast as ModelForecast
import atu.com.weatherapp.domain.model.Weather as ModelWeather
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by atu on 2017/7/23.
 */
public class ServerDataMapper {

    fun convertFromDataModel(zipCode: Long,forecast: ForecastResult) = with(forecast){
        ForecastList(zipCode,forecast.city.name,forecast.city.country,convertForecastListToDomain(forecast.list))
    }

    private fun convertForecastListToDomain(list: List<Forecast>): List<ModelForecast> {
        return list.mapIndexed { index, forecast ->
            val dt = Calendar.getInstance().timeInMillis + TimeUnit.DAYS.toMillis(index.toLong())
            convertForecastItemToDomain(forecast.copy(dt = dt))
        }
        
    }

    private fun convertForecastItemToDomain(forecast: Forecast) = with(forecast) {
         ModelForecast(-1, dt, forecast.weather[0].description, forecast.temp.max.toInt(), forecast.temp.min.toInt(), generateIconUrl(forecast.weather[0].icon))
    }


    /**
     * 和风天气
     */
    fun convertFromWeatherDataModel(weatherResult: HeWeatherResult) = with(weatherResult) {
        val result = weatherResult.HeWeather5[0]
        if(result.status.equals("ok")) {
            convertWeatherItemToDomain(result)
        }else{
            throw NoSuchElementException("No element matching predicate was found.")
        }
    }



    private fun convertWeatherItemToDomain(weather: HeWeather) = with(weather) {
        ModelWeather(weather.basic.id,weather.basic.update.loc,weather.now.cond.txt,weather.now.tmp,generateWeatherIconUrl(weather.now.cond.code))
    }


    private fun convertDate(date: Long): String {
        val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
        return df.format(date * 1000)
    }

    private fun generateIconUrl(iconCode: String): String = "https://cdn.heweather.com/cond_icon/$iconCode.png"

    private fun generateWeatherIconUrl(iconCode: Int): String = "https://cdn.heweather.com/cond_icon/$iconCode.png"
}