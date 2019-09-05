package com.scb.mobilephone.view.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scb.mobilephone.R
import com.scb.mobilephone.view.FavoriteFragment
import com.scb.mobilephone.view.ListFragment
import com.scb.mobilephone.view.MainActivity

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class SectionsPagerAdapter(private val context: MainActivity, private val fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {

    fun updateListFragment() {
        fragmentManager.fragments.forEach {
            if (it is ListFragment) {
                it.updateFragment()
            }
        }
    }

    fun updateFavoriteFragment() {
        fragmentManager.fragments.forEach {
            if (it is FavoriteFragment) {
                it.updateFragment()
            }
        }
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ListFragment()
            }
            else -> {
                FavoriteFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}