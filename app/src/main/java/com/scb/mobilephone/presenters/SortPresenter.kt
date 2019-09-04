package com.scb.mobilephone.presenters

import android.annotation.SuppressLint
import com.scb.mobilephone.extensions.PriceHL
import com.scb.mobilephone.extensions.PriceLH
import com.scb.mobilephone.extensions.RatingHL
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.entities.FavoritesListEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
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
    }

    override fun updatePhonesList(phonesList: ArrayList<PhoneBean>) {
        val task = Runnable {
            ListPresenter.mDatabaseAdapter!!.phonesListDao().updatePhonesList(
                PhonesListEntity(1, phonesList)
            )
        }
        ListPresenter.mThreadManager.postTask(task)
        ListFragment.mAdapter.notifyDataSetChanged()
    }

    override fun updateFavoritesList(favoritesList: ArrayList<PhoneBean>) {
        val task = Runnable {
            ListPresenter.mDatabaseAdapter!!.favoritesListDao().updateFavoritesList(
                FavoritesListEntity(1, favoritesList)
            )
        }
        ListPresenter.mThreadManager.postTask(task)
        FavoriteFragment.mAdapter.notifyDataSetChanged()
    }
}