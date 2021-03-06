package com.blackcatz.android.hnews.ui.landing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blackcatz.android.hnews.ui.landing.domain.ALL_STORIES
import com.blackcatz.android.hnews.ui.stories.StoriesFragment

/**
 * @author vinayagasundar
 */
class NavAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val list = ALL_STORIES

    override fun getItem(position: Int): Fragment {
        return StoriesFragment.create(list[position].type)
    }

    override fun getCount(): Int {
        return list.size
    }
}