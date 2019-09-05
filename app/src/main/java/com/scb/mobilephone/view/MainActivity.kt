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
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.presenters.MainPresenter
import com.scb.mobilephone.presenters.interfaces.MainInterface
import com.scb.mobilephone.view.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mainPresenter: MainInterface.SortPresenter
    lateinit var phonesList: ArrayList<PhoneBean>
    lateinit var favoritesList: List<FavoritesEntity>

    lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = this.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        mainPresenter = MainPresenter()
        mainPresenter.setupTreadManager()
        mainPresenter.setupDatabase(this)

        sortBtn.setOnClickListener {
            val task = Runnable {
                phonesList = mainPresenter.getPhones()
                favoritesList = mainPresenter.getFavorites()
            }
            mainPresenter.postTask(task)

            val listItems = arrayOf(PriceLH, PriceHL, RatingHL)
            val mBuilder = AlertDialog.Builder(this@MainActivity)
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
                val selectedItem = listItems[i]
                mainPresenter.sortPhonesList(phonesList, selectedItem)
                mainPresenter.sortFavoritesList(favoritesList, selectedItem)
                dialogInterface.dismiss()
            }
            val mDialog = mBuilder.create()
            mDialog.show()
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        sectionsPagerAdapter.updateListFragment()
                    }
                    1 -> {
                        sectionsPagerAdapter.updateFavoriteFragment()
                    }
                }
            }
        })
    }
}