package com.nuu.foodpanda.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nuu.foodpanda.apibean.ShopData
import com.nuu.foodpanda.network.McpKotLinUtility
import com.nuu.foodpanda.util.LogUtility
import com.nuu.foodpanda.util.ProgressDialogUtil
import com.yuanta.irec.config.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    private val _shopData = MutableLiveData<Array<ShopData>>().apply {
//        value = "This is home Fragment"
    }
    val shopData: LiveData<Array<ShopData>> = _shopData

    fun getData() {
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.IO) {
//            val postMap:HashMap<String,String> = HashMap()
//            postMap["222"] = "Java"
            val response = McpKotLinUtility.callMcpApi(AppConfig.URL_GAT_ALL_DATA,  null)
            viewModelScope.launch(Dispatchers.Main) {
                _shopData.value = Gson().fromJson(response, Array<ShopData>::class.java)
                LogUtility.e("shopData", response)
            }
        }
    }
}