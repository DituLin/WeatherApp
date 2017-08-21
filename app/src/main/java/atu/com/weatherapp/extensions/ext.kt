package atu.com.weatherapp.extensions

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import java.text.DateFormat
import java.util.*
import kotlin.properties.ReadWriteProperty

/**
 * Created by atu on 2017/7/24.
 */

val View.ctx: Context
        get() = context

fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
        val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
        return df.format(this)
}

fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)

var TextView.textColor: Int
        get() = currentTextColor
        set(v) = setTextColor(v)

fun View.slideExit() {
        if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.slideEnter() {
        if (translationY < 0f) animate().translationY(0f)
}

fun Calendar.toWeekly(): String{
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val mon = Calendar.getInstance().get(Calendar.MONTH) + 1
        val weekly = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        var week: String = ""
        when(weekly){
                1 -> week =  "星期日"
                2 -> week =  "星期一"
                3 -> week =  "星期二"
                4 -> week =  "星期三"
                5 -> week =  "星期四"
                6 -> week =  "星期五"
                7 -> week =  "星期六"
        }

        return week
}