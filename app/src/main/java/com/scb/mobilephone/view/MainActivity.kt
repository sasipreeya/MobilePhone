package com.scb.mobilephone.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.scb.mobilephone.R
import com.scb.mobilephone.extensions.PriceHL
import com.scb.mobilephone.extensions.PriceLH
import com.scb.mobilephone.extensions.RatingHL
import com.scb.mobilephone.presenter.ListPresenter.Companion.favoriteItem
import com.scb.mobilephone.presenter.ListPresenter.Companion.mDataArray
import com.scb.mobilephone.ui.main.SectionsPagerAdapter
import com.scb.mobilephone.view.FavoriteFragment.Companion.favoritePresenter
import com.scb.mobilephone.view.ListFragment.Companion.listPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        sortBtn.setOnClickListener {
            val listItems = arrayOf(PriceLH, PriceHL, RatingHL)
            val mBuilder = AlertDialog.Builder(this@MainActivity)
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
                val selectedItem = listItems[i]
                listPresenter.sortPhonesList(mDataArray!!, selectedItem)
                favoritePresenter.sortFavoritesList(favoriteItem!!, selectedItem)
                Log.d("sorted", selectedItem)
                dialogInterface.dismiss()
            }

            val mDialog = mBuilder.create()
            mDialog.show()
        }
    }
}