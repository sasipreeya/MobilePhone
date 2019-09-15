package com.scb.mobilephone.presenters

import com.scb.mobilephone.extensions.PriceHL
import com.scb.mobilephone.extensions.PriceLH
import com.scb.mobilephone.extensions.RatingHL
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity

interface SortInterface {

    interface SortToView {

        fun submitPhonesList(phonesList: List<PhonesListEntity>)

        fun submitFavoritesList(favoriteList: List<FavoritesEntity>)

        fun getSortType(sortType: String)
    }

    interface SortPresenter {

        fun sortPhonesList(sortType: String, phonesList: List<PhonesListEntity>)

        fun sortFavoritesList(sortType: String, favoriteList: List<FavoritesEntity>)
    }
}

class SortList(private val view: SortInterface.SortToView) : SortInterface.SortPresenter {

    lateinit var sortedPhonesList: List<PhonesListEntity>
    lateinit var sortedFavoritesList: List<FavoritesEntity>

    override fun sortPhonesList(sortType: String, phonesList: List<PhonesListEntity>) {
        when (sortType) {
            PriceLH -> {
                sortedPhonesList = phonesList.sortedBy { it.price }
            }
            PriceHL -> {
                sortedPhonesList = phonesList.sortedByDescending { it.price }
            }
            RatingHL -> {
                sortedPhonesList = phonesList.sortedByDescending { it.rating }
            }
        }
        view.submitPhonesList(sortedPhonesList)
    }

    override fun sortFavoritesList(sortType: String, favoriteList: List<FavoritesEntity>) {
        when (sortType) {
            PriceLH -> {
                sortedFavoritesList = favoriteList.sortedBy { it.price }
            }
            PriceHL -> {
                sortedFavoritesList = favoriteList.sortedByDescending { it.price }
            }
            RatingHL -> {
                sortedFavoritesList = favoriteList.sortedByDescending { it.rating }
            }
        }
        view.submitFavoritesList(sortedFavoritesList)
    }
}
