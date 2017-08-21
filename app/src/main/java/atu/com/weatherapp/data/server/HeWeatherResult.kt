package atu.com.weatherapp.data.server

/**
 *
 * 和风天气数据返回实例
 * Created by atu on 2017/8/10.
 */
data class HeWeatherResult(val HeWeather5: List<HeWeather>)

data class HeWeather(val basic: Basic, val now: Now, val status: String)

data class Basic(val city: String, val cnty: String, val id: String, val lat: Double, val lon: Double, val update: Update)

data class Update(val loc: String, val utc: String)

data class Now(val cond: Cond, val fl: Int, val hum: Int, val pcpn: Int, val pres: Int, val tmp: Int, val vis: Int, val wind: Wind)

data class Cond(val code: Int, val txt: String)

data class Wind(val deg: Int, val dir: String, val sc: String, val spd: Int)