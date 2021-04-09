package com.nuu.foodpanda.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nuu.foodpanda.R
import com.nuu.foodpanda.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            binding.tvAnnouncement.text = it
        })

        var testList = arrayListOf("123", "123", "123", "123", "2333", "3232", "2333", "3232", "2333")
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)//是否固定大小
        binding.recyclerView.adapter = MainAdapter(testList)
        return binding.root
    }

    inner class MainAdapter(private val mData: ArrayList<String>): RecyclerView.Adapter<MainHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_template2, parent, false)
            return MainHolder(view)
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
            val homeItemModel = mData[position]

            if(homeItemModel == "123"){

            }else{

            }
//            holder.bindBus(busess, position)
//            holder.tvBusTitle.text = bb[1]
        }

        override fun getItemCount(): Int {
            return mData.size
        }
    }

    inner class MainHolder(view: View): RecyclerView.ViewHolder(view){
//        var tvBusTitle: TextView = view.findViewById(R.id.tv_storeName)
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