package com.nuu.foodpanda.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.nuu.foodpanda.R

class CustomDialog: AlertDialog(context, R.style.customDialog) {

    companion object {
        var customDialog: CustomDialog? = null
        lateinit var context: Context
        lateinit var loadView: View


        fun instance(putContext: Context): CustomDialog{
            customDialog ?: run {
                context = putContext
                loadView = LayoutInflater.from(context).inflate(R.layout.layout_ios_dialog, null)
                customDialog = CustomDialog()
            }
            return customDialog!!
        }
    }

    fun show(message: String, leftString: String, leftListener: View.OnClickListener?){
        customDialog!!.setView(loadView)

        loadView!!.findViewById<TextView>(R.id.tv_title).text = message
        loadView!!.findViewById<TextView>(R.id.btn_conform).text = leftString
        if(leftListener==null){
            loadView!!.findViewById<TextView>(R.id.btn_conform).setOnClickListener{
                close()
            }
        }else{
            loadView!!.findViewById<TextView>(R.id.btn_conform).setOnClickListener(leftListener)
        }
        loadView!!.findViewById<TextView>(R.id.btn_cancel).visibility = View.GONE
        customDialog!!.show()
    }

    fun show(message: String, leftString: String, leftListener: View.OnClickListener?, rightString: String, rightListener: View.OnClickListener?){
        customDialog!!.setView(loadView)

        loadView!!.findViewById<TextView>(R.id.tv_title).text = message
        loadView!!.findViewById<TextView>(R.id.btn_conform).text = rightString
        if(rightListener==null){
            loadView!!.findViewById<TextView>(R.id.btn_conform).setOnClickListener{
                close()
            }
        }else{
            loadView!!.findViewById<TextView>(R.id.btn_conform).setOnClickListener(rightListener)
        }
        loadView!!.findViewById<TextView>(R.id.btn_cancel).text = leftString
        if(leftListener==null){
            loadView!!.findViewById<TextView>(R.id.btn_cancel).setOnClickListener{
                close()
            }
        }else{
            loadView!!.findViewById<TextView>(R.id.btn_cancel).setOnClickListener(leftListener)
        }
        customDialog!!.show()
    }

    /**
     * 隱藏耗時對話方塊
     */
    fun close() {
        customDialog?.run {
            if(customDialog!!.isShowing){
                customDialog!!.dismiss()
            }
            customDialog = null
        }
    }
}