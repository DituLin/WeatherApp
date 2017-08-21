package atu.com.weatherapp.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atu.com.weatherapp.R
import atu.com.weatherapp.extensions.ctx
import atu.com.weatherapp.domain.model.Forecast
import atu.com.weatherapp.domain.model.ForecastList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_forecast.view.*
import java.text.DateFormat
import java.util.*

/**
 * Created by atu on 2017/7/21.
 */
class ForecastListAdapter(val items: ForecastList,val itemClick: (Forecast) -> Unit) : RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(items.dailyForecast[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_forecast,parent,false)

        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return items.dailyForecast.size
    }


    class ViewHolder(view: View,val itemClick: (Forecast) -> Unit) : RecyclerView.ViewHolder(view){


        fun bindForecast(forecast: Forecast) {
            with(forecast) {
                Picasso.with(itemView.ctx).load(iconUrl).into(itemView.icon)
                itemView.date.text = convertDate(date)//直接访问所有的public的方法和属性
                itemView.description.text = description
                itemView.maxTemperature.text = "$high"
                itemView.minTemperature.text = "$low"
                itemView.setOnClickListener {
                    itemClick(this)//使用this
                }
            }
        }

        private fun convertDate(date: Long): String {
            val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
            return df.format(date)
        }
    }
}