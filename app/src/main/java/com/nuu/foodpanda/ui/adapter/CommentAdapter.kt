package com.nuu.foodpanda.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nuu.foodpanda.R
import com.nuu.foodpanda.apibean.ItemComment

class CommentAdapter(private val context: Context, private val mData: List<ItemComment>)
    : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position==0){
            holder.tvCommentCount.visibility = View.VISIBLE
            holder.tvCommentCount.text = "${mData.size-1} 評論"
            holder.constComment.visibility = View.GONE
        }else{
            holder.tvCommentCount.visibility = View.GONE
            holder.constComment.visibility = View.VISIBLE
            holder.tvName.text = if(mData[position].itemCommentUserName == "noperson") {
                ""
            }else{
                mData[position].itemCommentUserName.trim()
            }

            holder.tvDate.text = mData[position].itemCommentDate.trim()
            holder.tvComment.text = mData[position].itemCommentText.trim()

            //  建立星星
            val blueStar = (0..4).random()
            holder.llStar.removeAllViews()
            var imvStar:ImageView? = null
            for(i in 0..4){
                imvStar = ImageView(context)
                val layoutParams = LinearLayout.LayoutParams(30, 30)
                layoutParams.setMargins(0, 0, 5, 0);
                imvStar.layoutParams = layoutParams
                holder.llStar.addView(imvStar)
                val star = if(i<=blueStar){ R.drawable.ic_star }else{ R.drawable.ic_star_gray }
                Glide.with(context)
                    .load(star)
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imvStar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_comment,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvCommentCount = itemView.findViewById<TextView>(R.id.tv_comment_count)!!
        val constComment = itemView.findViewById<ConstraintLayout>(R.id.const_comment)!!
        val tvName = itemView.findViewById<TextView>(R.id.tv_name)!!
        val tvDate = itemView.findViewById<TextView>(R.id.tv_date)!!
        val tvComment = itemView.findViewById<TextView>(R.id.tv_comment)!!
        val llStar = itemView.findViewById<LinearLayout>(R.id.ll_star)!!
    }
}