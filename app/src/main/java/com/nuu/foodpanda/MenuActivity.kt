package com.nuu.foodpanda

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.media.Session2Command
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.nuu.foodpanda.apibean.ItemResult
import com.nuu.foodpanda.apibean.ShopInfoData
import com.nuu.foodpanda.databinding.ActivityMenuBinding
import com.nuu.foodpanda.network.McpKotLinUtility
import com.nuu.foodpanda.ui.adapter.MenuAdapter
import com.nuu.foodpanda.util.CustomBottomSheetDialog
import com.nuu.foodpanda.util.ProgressDialogUtil
import com.nuu.foodpanda.util.SystemUtility
import com.yuanta.irec.config.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable
import kotlin.math.abs

class MenuActivity : AppCompatActivity(), OnOffsetChangedListener, MenuAdapter.NumPrice {
    private lateinit var binding: ActivityMenuBinding
    private val context = this
    private var isScrolled = false
    private lateinit var manager: LinearLayoutManager
    private lateinit var shopInfoData: ShopInfoData
    private var itemNum = 0
    private var totalPrice = 0
    private lateinit var myCartData: MutableList<ItemResult>

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true
    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.6f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200
    private var mCurrentState = State.IDLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ProgressDialogUtil.instance(context).showLoading()
        GlobalScope.launch(Dispatchers.IO){
            intent.getStringExtra("infoUrl")?.run {
                val postMap: HashMap<String, String> = HashMap()
                postMap["infoUrl"] = intent.getStringExtra("infoUrl")!!
                val response = McpKotLinUtility.callMcpApi(AppConfig.URL_GAT_SHOP_INFO, postMap)
                shopInfoData = Gson().fromJson(response, ShopInfoData::class.java)
            }

            runOnUiThread {
                initData()
                initListener()
                ProgressDialogUtil.instance(context).closeLoading()
            }
        }

        binding.tvShopName.text = intent.getStringExtra("shopName")
        Glide.with(context)
            .load(intent.getStringExtra("shopImage"))
            .thumbnail(Glide.with(context).load(R.drawable.loadingpanda))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imvHeader)
    }

    private fun initData(){
        myCartData = mutableListOf()
        binding.idAppbarlayout.addOnOffsetChangedListener(context)
        manager = LinearLayoutManager(this)
        binding.recyclerMenuItem.layoutManager = manager
        if(shopInfoData!=null){
            binding.toolbarTab.removeAllTabs()
            for (i in shopInfoData.title.indices) {
                val tab1: TabLayout.Tab = binding.toolbarTab.newTab().setText(shopInfoData.title[i])
                binding.toolbarTab.addTab(tab1)
            }
            binding.toolbarTab.tabMode = TabLayout.MODE_SCROLLABLE

            val menuAdapter = MenuAdapter(
                context,
                shopInfoData.itemResult,
                shopInfoData.title,
                shopInfoData.titleNum,
                this
            )
            binding.recyclerMenuItem.adapter = menuAdapter
        }

//        when (val background: Drawable = binding.llCheck.background) {
//            is ShapeDrawable -> {
//                // cast to 'ShapeDrawable'
//                val shapeDrawable = background as ShapeDrawable
//                shapeDrawable.paint.color = ContextCompat.getColor(context, R.color.foodpanda_main)
//            }
//            is GradientDrawable -> {
//                // cast to 'GradientDrawable'
//                val gradientDrawable = background as GradientDrawable
//                gradientDrawable.setColor(ContextCompat.getColor(context, R.color.foodpanda_main))
//            }
//            is ColorDrawable -> {
//                // alpha value may need to be set again after this call
//                val colorDrawable = background as ColorDrawable
//                colorDrawable.color = ContextCompat.getColor(context, R.color.foodpanda_main)
//            }
//        }
    }

    private fun initListener(){

        binding.imvBack.setOnClickListener {
            finish()
        }

        binding.imvBackWhite.setOnClickListener {
            finish()
        }

        binding.consMinute.setOnClickListener {
            CustomBottomSheetDialog.instance(context).showDialog()
        }

        binding.llCheck.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("CartData", myCartData as Serializable)
            }
            SystemUtility.startActivity(context, OrderCheckActivity::class.java, bundle, 101)
        }

        binding.imvInfo.setOnClickListener {
            val bundle = Bundle().apply {
                putString("address", shopInfoData.itemAddress)
                putString("shopImage", intent.getStringExtra("shopImage"))
                putString("shopName", intent.getStringExtra("shopName"))
                putSerializable("shopData", shopInfoData.itemComment as Serializable)
            }
            SystemUtility.startActivity(context, AboutActivity::class.java, bundle)
        }

        binding.imvInfoWhite.setOnClickListener {
            val bundle = Bundle().apply {
                putString("address", shopInfoData.itemAddress)
                putString("shopImage", intent.getStringExtra("shopImage"))
                putString("shopName", intent.getStringExtra("shopName"))
                putSerializable("shopData", shopInfoData.itemComment as Serializable)
            }
            SystemUtility.startActivity(context, AboutActivity::class.java, bundle)
        }

        binding.toolbarTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val pos = tab!!.position
                manager.scrollToPositionWithOffset(shopInfoData.titleNum.get(pos), 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.recyclerMenuItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //重写该方法主要是判断recyclerview是否在滑动
                //0停止 ，12都是滑动
                isScrolled = newState != 0
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //这个主要是recyclerview滑动时让tab定位的方法
                if (isScrolled) {
                    val top: Int = manager.findFirstVisibleItemPosition()
                    val bottom: Int = manager.findLastVisibleItemPosition()
                    var pos = 0
                    if (bottom == shopInfoData.titleNum.size - 1) {
                        //先判断滑到底部，tab定位到最后一个
                        pos = shopInfoData.titleNum.size - 1
                    } else if (top == shopInfoData.titleNum[shopInfoData.titleNum.size - 1]) {
                        //如果top等于指定的位置，对应到tab即可，
                        pos = shopInfoData.titleNum[shopInfoData.titleNum.size - 1]
                    } else {
                        //循环遍历，需要比较i+1的位置，所以循环长度要减1，
                        //  如果 i<top<i+1,  那么tab应该定位到i位置的字符，不管是向上还是向下滑动
                        for (i in 0 until shopInfoData.titleNum.size - 1) {
                            if (top == shopInfoData.titleNum[i]) {
                                pos = i
                                break
                            } else if (top > shopInfoData.titleNum[i] && top < shopInfoData.titleNum[i + 1]) {
                                pos = i
                                break
                            }
                        }
                    }

                    //设置tab滑动到第pos个
                    binding.toolbarTab.setScrollPosition(pos, 0f, true)
                }
            }
        })
    }

    //  下拉動畫調整
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage: Float = abs(verticalOffset).toFloat() / maxScroll.toFloat()

//        handleAlphaOnTitle(percentage)
//        handleToolbarTitleVisibility(percentage)

        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                binding.tvTitle.setTextColor(Color.parseColor("#ffffff"))

            }
            mCurrentState = State.EXPANDED;
        } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                binding.tvTitle.setTextColor(Color.parseColor("#000000"))
            }
            mCurrentState = State.COLLAPSED;
        }
    }

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
//                binding.imvBack.visibility = View.VISIBLE
//                binding.imvInfo.visibility = View.VISIBLE
//                binding.imvBackWhite.visibility = View.INVISIBLE
//                binding.imvInfoWhite.visibility = View.INVISIBLE
                binding.tvTitle.setTextColor(Color.parseColor("#ffffff"))
                startAlphaAnimation(
                    binding.consTitleShrink,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.VISIBLE
                )
                mIsTheTitleVisible = true
            }
        } else {
            if (mIsTheTitleVisible) {       //下拉後的動畫
//                binding.imvBack.visibility = View.INVISIBLE
//                binding.imvInfo.visibility = View.INVISIBLE
//                binding.imvBackWhite.visibility = View.VISIBLE
//                binding.imvInfoWhite.visibility = View.VISIBLE

                binding.tvTitle.setTextColor(Color.parseColor("#000000"))
                startAlphaAnimation(
                    binding.consTitleShrink,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE
                )
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = false
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = true
            }
        }
    }

    private fun startAlphaAnimation(cons: View, duration: Long, visibility: Int) {
        val alphaAnimation =
            if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        cons.startAnimation(alphaAnimation)
    }

    override fun itemClick(menuModel: ItemResult) {
        itemNum += 1
        if(itemNum>0){
            binding.llCheck.visibility = View.VISIBLE
            binding.tvNum.text = itemNum.toString() + ""
        }
        val mSet = AnimatorSet()
        val anim = ObjectAnimator.ofFloat(binding.tvNum, "scaleY", 1f, 1.5f, 1f)
        val anim2 = ObjectAnimator.ofFloat(binding.tvNum, "scaleX", 1f, 1.5f, 1f)
        mSet.playTogether(anim, anim2)
        mSet.duration = 500
        mSet.start()

        totalPrice += stringPriceToInt(menuModel.itemPrice)
        binding.tvPrice.text = "$ $totalPrice"

        var checkItem = false
        for(cardData in myCartData){
            if(cardData.itemName == menuModel.itemName){
                checkItem = true
                break
            }
        }
        if(!checkItem){
            myCartData.add(menuModel)
        }
    }

    private fun stringPriceToInt(price: String): Int {
        var price = price
        price = price.substring(price.indexOf("$") + 1, price.indexOf("."))
        return price.toInt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            101 -> {
                if(resultCode == RESULT_OK){
                    myCartData.clear()
                    myCartData = data?.getSerializableExtra("CartData") as MutableList<ItemResult>

                    for(data in shopInfoData.itemResult){
                        data.itemNum = 0
                    }

                    for(data in shopInfoData.itemResult){
                        for(data2 in myCartData){
                            if(data.itemName == data2.itemName){
                                data.itemNum = data2.itemNum
                                binding.recyclerMenuItem.adapter?.notifyDataSetChanged()
                                break
                            }
                        }
                    }
                    itemNum = 0
                    totalPrice = 0
                    for(data in shopInfoData.itemResult){
                        itemNum += data.itemNum
                        totalPrice += stringPriceToInt(data.itemPrice) * data.itemNum
                    }

                    binding.tvNum.text = itemNum.toString() + ""
                    binding.tvPrice.text = "$ $totalPrice"
                }
            }
        }
    }
}