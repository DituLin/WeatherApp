package atu.com.weatherapp.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.Preference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by atu on 2017/7/27.
 */


object DelegatesExtensions{
    fun <T> preference(context: Context, name: String,
                       default: T) = LongPreference(context, name, default)
}

class LongPreference<T>(val context: Context, val name: String, val default: T) : ReadWriteProperty<Any?, T> {

    val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("default",Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
       return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name,value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findPreference(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        res as T
    }

    @SuppressLint("CommitPrefEdits")
    private fun putPreference(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }.apply()
    }

}