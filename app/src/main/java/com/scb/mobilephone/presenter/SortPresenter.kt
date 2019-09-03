package com.scb.mobilephone.presenter

import android.annotation.SuppressLint
import com.scb.mobilephone.extensions.PriceHL
import com.scb.mobilephone.extensions.PriceLH
import com.scb.mobilephone.extensions.RatingHL
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.view.FavoriteFragment
import com.scb.mobilephone.view.ListFragment

class SortPresenter : SortInterface.SortPresenter {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var sortPresenter: SortInterface.SortPresenter
    }

    private var sortedList: ArrayList<PhoneBean> = ArrayList()

    override fun sortDataList(list: ArrayList<PhoneBean>, sort: String) {
        sortedList.clear()
        when (sort) {
            PriceLH -> {
                sortedList.addAll(list.sortedBy { it.price })
                list.clear()
                list.addAll(sortedList)
            }
            PriceHL -> {
                sortedList.addAll(list.sortedByDescending { it.price })
                list.clear()
                list.addAll(sortedList)
            }
            RatingHL -> {
                sortedList.addAll(list.sortedByDescending { it.rating })
                list.clear()
                list.addAll(sortedList)
            }
            else -> {
                sortedList.addAll(list)
                list.clear()
                list.addAll(sortedList)
            }
        }
        FavoriteFragment.mAdapter.notifyDataSetChanged()
        ListFragment.mAdapter.notifyDataSetChanged()
    }

}