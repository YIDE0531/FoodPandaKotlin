package com.yuanta.irec.config

class AppConfig {

    companion object{
        val target = Target.DEV

        /*** 後台站台   */
        const val DEV_ENDPOINT = "https://foodpandatestphthon.herokuapp.com/"    //http://172.16.2.114:5200/
        const val TEST_ENDPOINT = "https://mliaestest.mli.com.tw"
        const val PROD_ENDPOINT = "https://foodpandatestphthon.herokuapp.com/"

        const val OFFICIAL_WEB_URL = "https://www.foodpanda.com.tw"
        /***  log  */
        val ENABLE_LOG = target == Target.DEV

        val apiPlatform = serviceEndpoint

        /***  獲取全部資料  */
        val URL_GAT_ALL_DATA = serviceEndpoint + "getData"

        /***  獲取各店家商品詳細資料  */
        val URL_GAT_SHOP_INFO = serviceEndpoint + "getInfo"

        /***  優惠情報  */
        val URL_COUPON = serviceEndpoint + "/mcpservice/api/API0060.do"

        /***  廣告輪播  */
        val URL_BANNER =
            serviceEndpoint + "/mcpservice/api/API0080.do?appname=" + (if (target == Target.PROD) "MLIUSER" else "MLIUSERTEST") + "&filetype=B"

        /***  問卷調查  */
        val URL_QUESTIONAIRE = serviceEndpoint + "/mcpservice/Questionnaire.html"

        /***  統計上傳  */
        val URL_WRITE_USAGE = serviceEndpoint + "/mcpservice/api/API0150.do"

        /*** 推播時間設定  */
        val URL_WRITE_PUSH_SETTING = serviceEndpoint + "/mcpservice/api/API0170.do"

        /***  通知服務: 推播訊息  */
        val URL_PUSH_DATA = serviceEndpoint + "/mcpservice/api/API0130.do"
        val URL_READ_PUSH_DATA = serviceEndpoint + "/mcpservice/api/API0140.do"

        private val serviceEndpoint: String
            private get() {
                return when (target) {
                    Target.DEV -> DEV_ENDPOINT
                    Target.TEST -> TEST_ENDPOINT
                    Target.PROD -> PROD_ENDPOINT
                }
            }

        enum class Target {
            DEV, TEST, PROD
        }
    }
}