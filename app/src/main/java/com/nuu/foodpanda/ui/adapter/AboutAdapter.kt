package com.nuu.foodpanda.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nuu.foodpanda.ui.Fragment.CommentFragment
import com.nuu.foodpanda.ui.Fragment.MapFragment

class AboutAdapter(
    fm: FragmentManager, //integer to count number of tabs
    var tabCount: Int
) :
    FragmentPagerAdapter(fm, tabCount) {
    //Overriding method getItem
    override fun getItem(position: Int): Fragment {
        //Returning the current tabs
        return when (position) {
            0 -> MapFragment.newInstance()!!
            1 -> CommentFragment.newInstance()!!
            else -> MapFragment.newInstance()!!
        }
    }

    //Overriden method getCount to get the number of tabs
    override fun getCount(): Int {
        return tabCount
    }
}