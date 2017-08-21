package atu.com.weatherapp.domain.command

/**
 * Created by atu on 2017/7/23.
 */
interface Command<T> {
    fun execute(): T
}