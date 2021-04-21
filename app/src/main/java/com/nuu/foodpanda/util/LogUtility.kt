package com.nuu.foodpanda.util

import android.util.Log
import com.yuanta.irec.config.AppConfig

/***
 * .啟用/關閉 Log
 * .弱掃前先註記所有 log method 實作
 */
class LogUtility {
    companion object {
        private var enabled: Boolean = AppConfig.ENABLE_LOG
        private val maxLength = 2048 //訊息顯示字數限制: 2048
        fun checkEnabled(): Boolean {
            return enabled
        }
        fun setEnabled(enable: Boolean) {
            enabled = enable
        }
        fun v(tag: String?, msg: String) {
            var msg = msg
            if (enabled) {
                val length = msg.length.toLong()
                if (length < maxLength || length == maxLength.toLong()) {
                    Log.v(tag, msg) //fortify
                } else {
                    while (msg.length > maxLength) {
                        val logContent = msg.substring(0, maxLength)
                        Log.v(tag, logContent)
                        msg = msg.replace(logContent, "")
                    }
                    Log.v(tag, msg) //fortify
                }
            }
        }

        fun v(tag: String?, msg: String?, tr: Throwable?) {
            if (enabled) Log.v(tag, msg, tr)
        }

        fun d(tag: String?, msg: String?) {
            if (enabled) {
                Log.d(tag, msg!!) //fortify
            }
        }

        fun d(tag: String?, msg: String?, tr: Throwable?) {
            if (enabled) Log.d(tag, msg, tr)
        }

        fun i(tag: String?, msg: String?) {
            if (enabled) Log.i(tag, msg!!)
        }

        fun i(tag: String?, msg: String?, tr: Throwable?) {
            if (enabled) Log.i(tag, msg, tr)
        }

        fun w(tag: String?, msg: String?) {
            if (enabled) Log.w(tag, msg!!)
        }

        fun w(tag: String?, msg: String?, tr: Throwable?) {
            if (enabled) Log.w(tag, msg, tr)
        }

        fun e(tag: String?, msg: String?) {
            if (enabled) {
                Log.e(tag, msg!!) //fortify
            }
        }

        fun e(tag: String?, msg: String?, tr: Throwable?) {
            if (enabled) Log.e(tag, msg, tr)
        }
    }
}