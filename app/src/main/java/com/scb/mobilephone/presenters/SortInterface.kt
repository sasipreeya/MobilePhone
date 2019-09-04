package com.scb.mobilephone.presenters

import com.scb.mobilephone.models.PhoneBean

interface SortInterface {

    interface SortPresenter {

        fun sortDataList(list: ArrayList<PhoneBean>, sort: String)

        fun updatePhonesList(phonesList: ArrayList<PhoneBean>)

        fun updateFavoritesList(favoritesList: ArrayList<PhoneBean>)
    }
}