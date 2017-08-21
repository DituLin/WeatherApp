package atu.com.weatherapp.data.db

/**
 * Created by atu on 2017/7/25.
 */
class DayForecast(var map: MutableMap<String, Any?>) {
    var _id: Long by map
    var date: Long by map
    var description: String by map
    var high: Int by map
    var low: Int by map
    var iconUrl: String by map
    var cityId: Long by map

    constructor(date: Long,description: String,high: Int,low: Int,
            iconUrl: String,cityId: Long) : this(HashMap()){
        this.date = date
        this.description = description
        this.high = high
        this.low = low
        this.iconUrl = iconUrl
        this.cityId = cityId
    }

}