package com.nuu.foodpanda.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nuu.foodpanda.R
import com.nuu.foodpanda.apibean.ItemResult

class EditOrderAdapter (private val context: Context, private val mData: MutableList<ItemResult>, private val numPrice: NumPrice)
    : RecyclerView.Adapter<EditOrderAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var price = stringPriceToInt(mData[position].itemPrice) * mData[position].itemNum
        holder.tvPrice.text = price.toString()
        holder.tvName.text = mData[position].itemName
        holder.tvNum.text = mData[position].itemNum.toString()

        holder.imvDelete.setOnClickListener {
            mData[position].itemNum -= 1
            price = stringPriceToInt(mData[position].itemPrice) * mData[position].itemNum
            holder.tvPrice.text = price.toString()
            if(mData[position].itemNum == 0){
                mData.removeAt(position)
            }
            notifyDataSetChanged()
            numPrice.itemClick(mData)
        }

        holder.imvAdd.setOnClickListener {
            mData[position].itemNum += 1
            price = stringPriceToInt(mData[position].itemPrice) * mData[position].itemNum
            holder.tvPrice.text = price.toString()
            notifyDataSetChanged()
            numPrice.itemClick(mData)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_edit_order, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var imvDelete: ImageView = view.findViewById(R.id.imv_delete)
        var tvNum: TextView = view.findViewById(R.id.tv_num)
        var tvName: TextView = view.findViewById(R.id.tv_name)
        var tvPrice: TextView = view.findViewById(R.id.tv_price)
        var imvAdd: ImageView = view.findViewById(R.id.imv_add)

    }

    private fun stringPriceToInt(price: String): Int {
        var price = price
        price = price.substring(price.indexOf("$") + 1, price.indexOf("."))
        return price.toInt()
    }

    interface NumPrice {
        fun itemClick(menuModel: MutableList<ItemResult>)
    }
}