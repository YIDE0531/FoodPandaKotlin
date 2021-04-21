package com.nuu.foodpanda.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle


class SystemUtility {
    private val TAG = SystemUtility::class.java.simpleName

    companion object {
        /*** start acitvity: 指定 requestCode  */
        fun startActivity(context: Context, activityClass: Class<*>?, bundle: Bundle?, requestCode: Int) {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.setClass(context, activityClass!!)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            (context as Activity).startActivityForResult(intent, requestCode)
        }

        fun startActivity(context: Context?, targetClass: Class<*>?, bundle: Bundle?) {
            val intent = Intent()
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.setClass(context!!, targetClass!!)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            (context as Activity).startActivity(intent)
        }

        fun startActivity(context: Context, uristr: String?) {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            val uri = Uri.parse(uristr)
            val i = Intent(Intent.ACTION_VIEW, uri)
            (context as Activity).startActivity(i)
        }

        fun closeApp(context: Context) {
            val activity = context as Activity
            activity.finish()
            if (androidSDK >= 16) {
                activity.finishAffinity()
            }
        }

        /*** 檢查網路是否連線  */
        fun getConnectionType(context: Context): Int {
            var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                                result = 2
                            }
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                                result = 1
                            }
                            hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                                result = 3
                            }
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        when (type) {
                            ConnectivityManager.TYPE_WIFI -> {
                                result = 2
                            }
                            ConnectivityManager.TYPE_MOBILE -> {
                                result = 1
                            }
                            ConnectivityManager.TYPE_VPN -> {
                                result = 3
                            }
                        }
                    }
                }
            }
            return result
        }


        /***
         * 取得 Android SDK code
         *
         * @return int: 例如，Android 4.0.3 環境，回傳 15，若取不到 android api，回傳 0
         */
        val androidSDK: Int
            get() {
                var sdk = 0
                try {
                    sdk = Build.VERSION.SDK_INT
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return sdk
            }

        /***
         * 取得螢幕解析度: density
         */
        fun getScreenDensity(context: Context): Float {
            //		LogUtility.v(TAG, "density:" + density);
            return context.resources.displayMetrics.density
        }

        /*** 依 dimen 取得 dp 或 sp  */
        fun getDensitySize(context: Context, dimenRourceID: Int): Float {
            val pixels = context.resources.getDimensionPixelSize(dimenRourceID).toFloat()
            return pixels / getScreenDensity(context)
        }

        fun getPixelSize(context: Context, size: Int): Int {
            return (size * getScreenDensity(context)).toInt()
        }
    }
}
