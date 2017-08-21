package atu.com.weatherapp.domain

import atu.com.weatherapp.domain.model.Forecast

/**
 * Created by atu on 2017/7/24.
 */
public interface OnItemClickListener {
    operator fun invoke(forecast: Forecast)
}