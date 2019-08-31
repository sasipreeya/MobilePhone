package com.scb.mobilephone.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scb.mobilephone.view.FavoriteFragment
import com.scb.mobilephone.view.ListFragment
import com.scb.mobilephone.view.MainActivity
import com.scb.mobilephone.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class SectionsPagerAdapter(private val context: MainActivity, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    lateinit var mListFragment: ListFragment
    lateinit var mFavoriteFragment: FavoriteFragment

    override fun getItem(position: Int): Fragment {
        return when (position) {
            // pass params intent to fragment
            0 -> {
                mListFragment = ListFragment()
                mListFragment
            }
            else -> {
                mFavoriteFragment = FavoriteFragment()
                mFavoriteFragment
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