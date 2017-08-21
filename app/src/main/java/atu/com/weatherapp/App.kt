package atu.com.weatherapp

import android.app.Application
import atu.com.dueroslib.DuerOS
import atu.com.weatherapp.delegates.DelegatesExt

/**
 * Created by atu on 2017/7/24.
 */
class App : Application() {

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        DuerOS.init(this)
    }
}