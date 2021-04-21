package com.nuu.foodpanda.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nuu.foodpanda.R
import com.nuu.foodpanda.apibean.ItemResult

class MenuAdapter(
    private val context: Context,
    private val mData: List<ItemResult>,
    private val titleList: List<String>,
    private val titleNumList: List<Int>,
    private var numPrice: NumPrice
)
    : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = mData[position].itemName
        holder.tvCost.text = mData[position].itemPrice

        var isTitle = false
        var isTitleDivide = false
        for((nowCount, titleNum) in titleNumList.withIndex()){
            if(position == titleNum-1 && position!=1){
                holder.viewGroup.visibility = View.VISIBLE
                holder.viewItem.visibility = View.GONE
                isTitleDivide = true
            }
            if(position == titleNum){
                holder.tvTitle.visibility = View.VISIBLE
                holder.tvTitle.text = titleList[nowCount]
                isTitle = true
            }
        }
        if(!isTitleDivide) {
            holder.viewGroup.visibility = View.GONE
            holder.viewItem.visibility = View.VISIBLE
        }
        if(!isTitle){
            holder.tvTitle.visibility = View.GONE
        }

        if(mData[position].itemImage != "notfind"){
            holder.imbShop.visibility = View.VISIBLE
            Glide.with(context)
                .load(mData[position].itemImage)
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imbShop)
        }

        holder.tvNum.visibility = if(mData[position].itemNum>0){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }

        holder.constItem.setOnClickListener {
            if(stringPriceToInt(mData[position].itemPrice) != 0) {
                numPrice.itemClick(mData[position])
                mData[position].itemNum = mData[position].itemNum + 1
                holder.tvNum.text = "${mData[position].itemNum}"
                holder.tvNum.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_menu,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tv_item_name)
        val tvCost = itemView.findViewById<TextView>(R.id.tv_item_cost)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val tvNum = itemView.findViewById<TextView>(R.id.tv_num)
        val imbShop = itemView.findViewById<ImageButton>(R.id.imb_shop)

        val constItem = itemView.findViewById<ConstraintLayout>(R.id.cons_item)
        val viewItem = itemView?.findViewById<View>(R.id.view_item)
        val viewGroup = itemView?.findViewById<View>(R.id.view_group)

    }

    private fun stringPriceToInt(price: String): Int {
        var price = price
        price = price.substring(price.indexOf("$") + 1, price.indexOf("."))
        return price.toInt()
    }

    interface NumPrice {
        fun itemClick(menuModel: ItemResult)
    }
}