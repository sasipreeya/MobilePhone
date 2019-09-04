package com.scb.mobilephone.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.scb.mobilephone.R
import com.scb.mobilephone.extensions.PriceHL
import com.scb.mobilephone.extensions.PriceLH
import com.scb.mobilephone.extensions.RatingHL
import com.scb.mobilephone.presenters.ListPresenter.Companion.favoriteItem
import com.scb.mobilephone.presenters.ListPresenter.Companion.mDataArray
import com.scb.mobilephone.presenters.SortPresenter
import com.scb.mobilephone.presenters.SortPresenter.Companion.sortPresenter
import com.scb.mobilephone.view.ui.main.SectionsPagerAdapter
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

        sortPresenter = SortPresenter()

        sortBtn.setOnClickListener {
            val listItems = arrayOf(PriceLH, PriceHL, RatingHL)
            val mBuilder = AlertDialog.Builder(this@MainActivity)
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
                val selectedItem = listItems[i]
                sortPresenter.sortDataList(mDataArray, selectedItem)
                sortPresenter.sortDataList(favoriteItem, selectedItem)
                dialogInterface.dismiss()
            }

            val mDialog = mBuilder.create()
            mDialog.show()
        }
    }
}