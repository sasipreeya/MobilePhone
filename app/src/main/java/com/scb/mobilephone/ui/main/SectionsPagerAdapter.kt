package com.scb.mobilephone.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scb.mobilephone.FavoriteFragment
import com.scb.mobilephone.ListFragment
import com.scb.mobilephone.MainActivity
import com.scb.mobilephone.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: MainActivity, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    lateinit var mListFragment: ListFragment

    override fun getItem(position: Int): Fragment {
        return when (position) {
            // pass params intent to fragment
            0 -> {
                mListFragment = ListFragment()
                mListFragment
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
        // Show 2 total pages.
        return TAB_TITLES.size
    }
}