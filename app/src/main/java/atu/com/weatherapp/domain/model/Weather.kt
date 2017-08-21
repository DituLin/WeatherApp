package atu.com.weatherapp.domain.model

/**
 * Created by atu on 2017/8/10.
 */
data class Weather(val id: String, val date: String, val description: String, val tmp: Int,
                    val iconUrl: String)