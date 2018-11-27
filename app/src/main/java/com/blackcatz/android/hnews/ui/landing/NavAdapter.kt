package com.blackcatz.android.hnews.ui.landing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.blackcatz.android.hnews.ui.landing.domain.ALL_STORIES
import com.blackcatz.android.hnews.ui.stories.StoriesFragment

/**
 * @author vinayagasundar
 */
class NavAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private val list = ALL_STORIES

    override fun getItem(position: Int): Fragment {
        return StoriesFragment()
    }

    override fun getCount(): Int {
        return list.size
    }
}