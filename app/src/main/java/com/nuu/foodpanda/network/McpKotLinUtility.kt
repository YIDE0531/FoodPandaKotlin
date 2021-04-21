package com.nuu.foodpanda.network

import android.content.Context
import com.nuu.foodpanda.util.LogUtility
import java.io.DataOutputStream
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.*

class McpKotLinUtility {
    lateinit var mcpCallBack: CallBack

    constructor(context: Context, apiUrl: String, params: HashMap<String, String>, callBack: CallBack) {
        var connection: HttpsURLConnection? = null
        try {
            connection = (URL(apiUrl).openConnection() as HttpsURLConnection).apply {
                requestMethod = "POST"//设置请求方式
                connectTimeout = 8000//设置连接超时时间
                readTimeout = 8000//设置读取超时时间
//                sslSocketFactory = getSSLContext(context).socketFactory//使用信任管理器
//                hostnameVerifier = MyHostnameVerifier()//设置主机名校验
            }

            val postData: ByteArray = getQuery(params).toByteArray(StandardCharsets.UTF_8)
            DataOutputStream(connection.outputStream).write(postData)//傳送參數

            connection.inputStream.bufferedReader().useLines {  //接收回傳結果
                var sb = StringBuilder()
                it.forEach {
                    sb.append(StringBuilder().apply {
                        append(it)
                    })
                }
                LogUtility.d("McpKotLinUtility", "onCreate2: ${sb}")
                callBack.apiCallBack(sb.toString())
            }
        }catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
    }

    companion object {  //只加這個java需JsonKTUtility.Companion.setDataListJson();   //可改Companion名稱
        @JvmStatic  //加入後可以直接static sonKTUtility.setDataListJson();
        fun callMcpApi(apiUrl: String, params: HashMap<String, String>?): String {
            var connection: HttpsURLConnection? = null
            var sb = StringBuilder()
            try {
                connection = (URL(apiUrl).openConnection() as HttpsURLConnection).apply {
                    requestMethod = params?.let {
                        "POST"
                    } ?: run {
                        "GET"
                    }
                    connectTimeout = 8000//设置连接超时时间
                    readTimeout = 8000//设置读取超时时间
                    setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
//                sslSocketFactory = getSSLContext(context).socketFactory//使用信任管理器
//                hostnameVerifier = MyHostnameVerifier()//设置主机名校验
                }

                params?.run {
                    val postData: ByteArray = getQuery(params!!).toByteArray(StandardCharsets.UTF_8)
                    DataOutputStream(connection.outputStream).write(postData)//傳送參數
                }

                connection.inputStream.bufferedReader().useLines {  //接收回傳結果
                    it.forEach {
                        sb.append(StringBuilder().apply {
                            append(it)
                        })
                    }
                    LogUtility.d("McpKotLinUtility", "onCreate2: ${sb}")
                }
                return sb.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
            return ""
        }

        @JvmStatic
        fun callJsonMcpApi(context: Context, apiUrl: String, jsonParams: String): String {
            var connection: HttpsURLConnection? = null
            var sb = StringBuilder()
            try {
                connection = (URL(apiUrl).openConnection() as HttpsURLConnection).apply {
                    requestMethod = "POST"//设置请求方式
                    connectTimeout = 8000//设置连接超时时间
                    readTimeout = 8000//设置读取超时时间
                    setRequestProperty("Content-Type", "application/json")
//                sslSocketFactory = getSSLContext(context).socketFactory//使用信任管理器
//                hostnameVerifier = MyHostnameVerifier()//设置主机名校验
                }

                val postData: ByteArray = jsonParams.toByteArray(StandardCharsets.UTF_8)
                DataOutputStream(connection.outputStream).write(postData)//傳送參數

                connection.inputStream.bufferedReader().useLines {  //接收回傳結果
                    it.forEach {
                        sb.append(StringBuilder().apply {
                            append(it)
                        })
                    }
                    LogUtility.d("McpKotLinUtility", "onCreate2: ${sb}")
                }
                return sb.toString();
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
            return ""
        }

        private fun getQuery(params: HashMap<String, String>): String {
            val result = java.lang.StringBuilder()
            var first = true
            for ((key, value) in params) {
                if (first) first = false else result.append("&")
                result.append(URLEncoder.encode(key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(value, "UTF-8"))
            }
            return result.toString()
        }
    }

    fun setCallBack(callBack: CallBack){
        mcpCallBack = callBack
    }

    interface CallBack{
        fun apiCallBack(respond: String)
    }

    private class MyHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            return  true
        }
    }

    private fun getSSLContext(context: Context): SSLContext {
        //创建SSL上下文对象
        val sslContext = SSLContext.getInstance("TLS")
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        val ks  = KeyStore.getInstance(KeyStore.getDefaultType())
        ks.load(null)
        val cf = CertificateFactory.getInstance("X.509")
        val open = context.assets.open("twd.cer")
        val cert  = cf.generateCertificate(open)
        ks.setCertificateEntry("twd", cert)
        tmf.init(ks)
        val tm = tmf.trustManagers
        sslContext.init(null, tm, null)
        return sslContext
    }
}
