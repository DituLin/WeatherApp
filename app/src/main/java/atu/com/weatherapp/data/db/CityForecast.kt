package atu.com.weatherapp.data.db

import atu.com.weatherapp.data.db.DayForecast

/**
 * Created by atu on 2017/7/25.
 */
class CityForecast(val map: MutableMap<String, Any?>,
                   val dailyForecast: List<DayForecast>) {

    var _id: Long by map
    var city: String by map
    var country: String by map


    constructor(id: Long, city: String, country: String, dailyForecast: List<DayForecast>) : this(HashMap(), dailyForecast) {
        this._id = id
        this.city = city
        this.country = country
    }

}