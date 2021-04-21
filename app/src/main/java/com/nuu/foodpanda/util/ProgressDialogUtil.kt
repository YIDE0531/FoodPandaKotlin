package com.nuu.foodpanda.util

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.nuu.foodpanda.R

class ProgressDialogUtil: AlertDialog(context, R.style.CustomProgressDialog) {

    companion object {
        var progressDialogUtil: ProgressDialogUtil? = null
        lateinit var context: Context

        fun instance(putContext: Context): ProgressDialogUtil{
            progressDialogUtil ?: run {
                context = putContext
                progressDialogUtil = ProgressDialogUtil()
            }
            return progressDialogUtil!!
        }
    }

    /**
     * 彈出耗時對話方塊
     * @param context
     */
    open fun showLoading() {
        val loadView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_view, null)
        progressDialogUtil!!.setView(loadView, 0, 0, 0, 0)
        progressDialogUtil!!.setCanceledOnTouchOutside(false)
        progressDialogUtil!!.show()
    }

    fun showProgressDialog(context: Context?, tip: String?) {
        var tip = tip
        if (TextUtils.isEmpty(tip)) {
            tip = "載入中..."
        }
        val loadView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_view, null)
        progressDialogUtil!!.setView(loadView, 0, 0, 0, 0)
        progressDialogUtil!!.setCanceledOnTouchOutside(false)

//        TextView tvTip = loadView.findViewById(R.id.tvTip);
//        tvTip.setText(tip);
        progressDialogUtil!!.show()
    }

    /**
     * 隱藏耗時對話方塊
     */
    fun closeLoading() {
        progressDialogUtil?.run {
            if(progressDialogUtil!!.isShowing){
                progressDialogUtil!!.dismiss()
            }
            progressDialogUtil = null
        }
    }
}