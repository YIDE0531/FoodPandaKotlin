package com.nuu.foodpanda.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nuu.foodpanda.R

class CustomBottomSheetDialog: BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {

    companion object {
        var customDialog: CustomBottomSheetDialog? = null
        lateinit var context: Context
        lateinit var loadView: View


        fun instance(putContext: Context): CustomBottomSheetDialog{
            customDialog ?: run {
                context = putContext
                loadView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet, null)
                customDialog = CustomBottomSheetDialog()
            }
            return customDialog!!
        }
    }

    fun showDialog(){
        customDialog!!.setContentView(loadView)

//        CustomDialog.loadView!!.findViewById<TextView>(R.id.tv_title).text = message
//        CustomDialog.loadView!!.findViewById<TextView>(R.id.btn_conform).text = leftString
//        CustomDialog.loadView!!.findViewById<TextView>(R.id.btn_conform).setOnClickListener {
//            leftListener
//        }
//        CustomDialog.loadView!!.findViewById<TextView>(R.id.btn_cancel).visibility = View.GONE
        customDialog!!.show()
    }


}