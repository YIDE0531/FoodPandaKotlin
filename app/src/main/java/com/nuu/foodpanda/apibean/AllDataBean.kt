package com.nuu.foodpanda.apibean
import java.io.Serializable

data class ShopData(
    val image: String,
    val infoUrl: String,
    val title: String,
    var type: String = "normal"
)

data class ShopInfoData(
    val itemAddress: String,
    val itemComment: List<ItemComment>,
    val itemDate: String,
    val itemResult: List<ItemResult>,
    val title: List<String>,
    val titleNum: List<Int>
)

data class ItemComment(
    val itemCommentDate: String,
    val itemCommentText: String,
    val itemCommentUserName: String
): Serializable

data class ItemResult(
    val itemImage: String,
    var itemName: String,
    var itemPrice: String,
    var itemNum: Int = 0    //  不是後台資料，用來計算目前選取的個數
): Serializable