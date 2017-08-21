package atu.com.weatherapp.domain.model

/**
 * Created by atu on 2017/7/23.
 */
data class Forecast(val id: Long, val date: Long, val description: String, val high: Int,
                    val low: Int, val iconUrl: String)