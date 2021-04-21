package com.nuu.foodpanda

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nuu.foodpanda.apibean.ItemResult
import com.nuu.foodpanda.databinding.ActivityOrderCheckBinding
import com.nuu.foodpanda.ui.adapter.EditOrderAdapter
import com.nuu.foodpanda.util.CustomDialog
import java.io.Serializable

class OrderCheckActivity : AppCompatActivity(), EditOrderAdapter.NumPrice {
    private lateinit var binding: ActivityOrderCheckBinding
    private val context = this
    private var myCartData: MutableList<ItemResult> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initListener()
    }

    private fun initData(){
        intent.getSerializableExtra("CartData")?.run {
            myCartData = intent.getSerializableExtra("CartData") as MutableList<ItemResult>
        }
        val manager = LinearLayoutManager(this)
        binding.rvEditOrder.layoutManager = manager
        val editOrderAdapter = EditOrderAdapter(
            context,
            myCartData,
            this
        )
        binding.rvEditOrder.adapter = editOrderAdapter

        var itemNum = 0
        var totalPrice = 0
        for(data in myCartData){
            itemNum += data.itemNum
            totalPrice += stringPriceToInt(data.itemPrice) * data.itemNum
        }
        binding.tvNum.text = itemNum.toString()
        binding.tvPrice.text = "$ ${totalPrice+20+2}"
        binding.tvTotalOrder.text = "$ ${totalPrice+20+2}"
    }

    private fun initListener(){
        binding.imvBackWhite.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("CartData", myCartData as Serializable)
            }
            setResult(RESULT_OK, Intent().putExtras(bundle))
            finish()
        }

        binding.llCheck.setOnClickListener {
            val rightListener =
                View.OnClickListener {
                    finish()
                    val intent = Intent(this@OrderCheckActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    CustomDialog.instance(context).close()
                }
            CustomDialog.instance(context).show("確定要送出訂單嗎？", "取消", null, "確定", rightListener)
        }
    }

    private fun stringPriceToInt(price: String): Int {
        var price = price
        price = price.substring(price.indexOf("$") + 1, price.indexOf("."))
        return price.toInt()
    }

    override fun itemClick(menuModel: MutableList<ItemResult>) {
        var itemNum = 0
        var totalPrice = 0
        for(data in menuModel){
            itemNum += data.itemNum
            totalPrice += stringPriceToInt(data.itemPrice) * data.itemNum
        }
        if(itemNum==0){
            val rightListener =
                View.OnClickListener {
                    val bundle = Bundle().apply {
                        putSerializable("CartData", myCartData as Serializable)
                    }
                    setResult(RESULT_OK, Intent().putExtras(bundle))
                    finish()
                    val intent = Intent(this@OrderCheckActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    CustomDialog.instance(context).close()
                }
            CustomDialog.instance(context).show("請重新下訂", "取消", null, "確定", rightListener)
        }else {
            binding.tvNum.text = itemNum.toString()
            val mSet = AnimatorSet()
            val anim = ObjectAnimator.ofFloat(binding.tvNum, "scaleY", 1f, 1.5f, 1f)
            val anim2 = ObjectAnimator.ofFloat(binding.tvNum, "scaleX", 1f, 1.5f, 1f)
            mSet.playTogether(anim, anim2)
            mSet.duration = 500
            mSet.start()

            binding.tvPrice.text = "$ ${totalPrice+20+2}"
            binding.tvTotalOrder.text = "$ ${totalPrice+20+2}"
        }
    }
}