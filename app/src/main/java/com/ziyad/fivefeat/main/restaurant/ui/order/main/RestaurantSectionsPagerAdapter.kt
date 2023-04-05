package com.ziyad.fivefeat.main.restaurant.ui.order.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ziyad.fivefeat.main.user.ui.order.main.TAB_TITLES

class RestaurantSectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        when (position) {
            0 -> {
                return RestaurantPlaceholderFragment.newInstance("QUEUE || READY")
            }
            1 -> {
                return RestaurantPlaceholderFragment.newInstance("DONE")
            }
        }
        return RestaurantPlaceholderFragment.newInstance("QUEUE")
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}