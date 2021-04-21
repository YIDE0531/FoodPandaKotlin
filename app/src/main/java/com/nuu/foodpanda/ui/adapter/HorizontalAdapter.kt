package com.nuu.foodpanda.ui.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nuu.foodpanda.MenuActivity
import com.nuu.foodpanda.R
import com.nuu.foodpanda.apibean.ShopData
import com.nuu.foodpanda.util.SystemUtility
import com.yuanta.irec.config.AppConfig

class HorizontalAdapter (private val context: Context, private val mData: Array<ShopData>)
    : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder?.bind(weathers[position])
        val homeItemModel = mData[position]

        holder.constMain.setOnClickListener {
            val bundle = Bundle().apply {
                putString("infoUrl", AppConfig.OFFICIAL_WEB_URL + homeItemModel.infoUrl)
                putString("shopName", homeItemModel.title)
                putString("shopImage", homeItemModel.image)
            }
            SystemUtility.startActivity(context, MenuActivity::class.java, bundle)
        }

        holder.tvStoreName.text = homeItemModel.title
        Glide.with(context)
            .load(homeItemModel.image)
            .thumbnail(Glide.with(context).load(R.drawable.loadingpanda))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.imvTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_template, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 5

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var constMain: ConstraintLayout = view.findViewById(R.id.ll_main)
        var tvStoreName: TextView = view.findViewById(R.id.tv_storeName)
        var imvTemp: ImageView = view.findViewById(R.id.imv_temp)
//        private val date = itemView?.findViewById<TextView>(R.id.weekTimeTextView)
//        private val minTemp = itemView?.findViewById<TextView>(R.id.tempMinTextView)
//        private val maxTemp = itemView?.findViewById<TextView>(R.id.tempMaxTextView)
//        private val humidity = itemView?.findViewById<TextView>(R.id.humidityTextView)
//        private val desc = itemView?.findViewById<TextView>(R.id.descriptionTextView)

//        fun bind(weather: Weather) {
//            date?.text = weather.date
//            minTemp?.text = weather.minTemp.toString()
//            maxTemp?.text = weather.maxTemp.toString()
//            humidity?.text = weather.humidity.toString()
//            desc?.text = weather.description
//        }
    }
}