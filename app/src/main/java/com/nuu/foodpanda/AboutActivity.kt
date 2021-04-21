package com.nuu.foodpanda

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.nuu.foodpanda.databinding.ActivityAboutBinding
import com.nuu.foodpanda.ui.Fragment.CommentFragment
import com.nuu.foodpanda.ui.Fragment.MapFragment
import com.nuu.foodpanda.ui.adapter.AboutAdapter
import kotlin.math.abs

class AboutActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {
    private lateinit var binding: ActivityAboutBinding
    private val context = this

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true
    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.6f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200
    private var mCurrentState = State.IDLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initListener()
    }

    private fun initData(){
        binding.viewPager.adapter = AboutAdapter(
            supportFragmentManager,
            binding.toolbarTab.tabCount
        )
        binding.viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.toolbarTab))
        intent.getStringExtra("shopImage")?.run {
            Glide.with(context)
                .load(intent.getStringExtra("shopImage"))
                .thumbnail(Glide.with(context).load(R.drawable.loadingpanda))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.imvShop)
            binding.tvShopName.text = intent.getStringExtra("shopName")
            binding.tvTitleShopName.text = intent.getStringExtra("shopName")
            binding.tvAddress.text = intent.getStringExtra("address")
        }
        CommentFragment.newInstance()?.arguments = intent.extras
        MapFragment.newInstance()?.arguments = intent.extras
        binding.idAppbarlayout.addOnOffsetChangedListener(context)
    }

    private fun initListener(){
        binding.imvBackWhite.setOnClickListener {
            finish()
        }

        binding.toolbarTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val pos = tab!!.position
                binding.viewPager.currentItem = pos
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage: Float = abs(verticalOffset).toFloat() / maxScroll.toFloat()

        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                binding.tvTitleShopName.visibility = View.INVISIBLE
                binding.tvShopName.visibility = View.VISIBLE
            }
            mCurrentState = State.EXPANDED;
        } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                binding.tvTitleShopName.visibility = View.VISIBLE
                binding.tvShopName.visibility = View.INVISIBLE
            }
            mCurrentState = State.COLLAPSED;
        }
//        handleAlphaOnTitle(percentage)
//        handleToolbarTitleVisibility(percentage)
    }

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }


    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                binding.tvTitleShopName.visibility = View.INVISIBLE

                startAlphaAnimation(
                    binding.tvTitleShopName,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.VISIBLE
                )
                mIsTheTitleVisible = true
            }
        } else {
            if (mIsTheTitleVisible) {       //下拉後的動畫
                binding.tvTitleShopName.visibility = View.VISIBLE

                startAlphaAnimation(
                    binding.tvTitleShopName,
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
}