package com.scb.mobilephone.presenter

import com.scb.mobilephone.models.PhoneBean

interface SortInterface {

    interface SortPresenter {

        fun sortDataList(list: ArrayList<PhoneBean>, sort: String)

    }
}