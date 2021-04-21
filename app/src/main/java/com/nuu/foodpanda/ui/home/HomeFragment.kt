package com.nuu.foodpanda.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nuu.foodpanda.MenuActivity
import com.nuu.foodpanda.R
import com.nuu.foodpanda.apibean.ShopData
import com.nuu.foodpanda.databinding.FragmentHomeBinding
import com.nuu.foodpanda.ui.adapter.HorizontalAdapter
import com.nuu.foodpanda.util.CustomDialog
import com.nuu.foodpanda.util.ProgressDialogUtil
import com.nuu.foodpanda.util.SystemUtility
import com.yuanta.irec.config.AppConfig.Companion.OFFICIAL_WEB_URL

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            binding.tvAnnouncement.text = it
        })

        homeViewModel.shopData.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                val dataArray = it
                var i = 0
                for (data in dataArray) {
                    if (i < 3) {
                        data.type = "horizontal"
                        i++
                    } else {
                        data.type = "normal"
                    }
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                binding.recyclerView.setHasFixedSize(true)//是否固定大小
                binding.recyclerView.adapter = MainAdapter(requireContext(), dataArray)
            }else{
                CustomDialog.instance(requireContext()).show("取得資料失敗，請在下拉更新一次", "確定", null)
            }
            ProgressDialogUtil.instance(requireContext()).closeLoading()
        })
        initListener()
        ProgressDialogUtil.instance(requireContext()).showLoading()
        homeViewModel.getData()
        return binding.root
    }

    private fun initListener(){
        binding.swipeContainer.setOnRefreshListener {
            ProgressDialogUtil.instance(requireContext()).showLoading()
            homeViewModel.getData()
            binding.swipeContainer.isRefreshing = false  //讓下拉更新的進度條（轉圈）停止顯示
        }
    }

    inner class MainAdapter(private val context: Context, private val mData: Array<ShopData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType) {
                1 -> MainHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_template2, parent, false))
                2 -> HorizontalHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_recycel_view, parent, false))
                else -> MainHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_template2, parent, false))
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val homeItemModel = mData[position]

            if(homeItemModel.type == "horizontal"){
                (holder as HorizontalHolder).recyclerHView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                holder.recyclerHView.setHasFixedSize(true)
                holder.recyclerHView.adapter = HorizontalAdapter(context, mData)
            }else{
                (holder as MainHolder).constMain.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("infoUrl", OFFICIAL_WEB_URL + homeItemModel.infoUrl)
                        putString("shopName", homeItemModel.title)
                        putString("shopImage", homeItemModel.image)
                    }
                    SystemUtility.startActivity(context, MenuActivity::class.java, bundle)
                }
                holder.tvStoreName.text = homeItemModel.title
                Glide.with(context)
                    .load(homeItemModel.image)
                    .thumbnail(Glide.with(context).load(R.drawable.loadingpanda))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imvTemp)
            }
        }

        override fun getItemCount(): Int {
            return mData.size
        }

        override fun getItemViewType(position: Int): Int {
            return when (mData[position].type) {
                "normal" -> 1
                "horizontal" -> 2
                else -> 1
            }
        }
    }

    inner class MainHolder(view: View): RecyclerView.ViewHolder(view){
        var constMain: ConstraintLayout = view.findViewById(R.id.ll_main)
        var tvStoreName: TextView = view.findViewById(R.id.tv_storeName)
        var imvTemp: ImageView = view.findViewById(R.id.imv_temp)
//
//        fun bindBus(bus: Bus, position: Int){
//            tvBusTitle.text = "BusID：${bus.datas[position].BusID}"
//            tvRouteId.text =  "RouteID：${bus.datas[position].RouteID}"
//            tvSpeed.text = "Speed ：${bus.datas[position].Speed}"
//        }
    }

    inner class HorizontalHolder(view: View): RecyclerView.ViewHolder(view){
        var recyclerHView: RecyclerView = view.findViewById(R.id.recycler_horizontal_view)
//        var tvRouteId: TextView = view.tv_route_id
//        var tvSpeed: TextView = view.tv_speed
//
//        fun bindBus(bus: Bus, position: Int){
//            tvBusTitle.text = "BusID：${bus.datas[position].BusID}"
//            tvRouteId.text =  "RouteID：${bus.datas[position].RouteID}"
//            tvSpeed.text = "Speed ：${bus.datas[position].Speed}"
//        }
    }
}