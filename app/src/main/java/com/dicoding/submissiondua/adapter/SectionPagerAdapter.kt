package com.dicoding.submissiondua.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.submissiondua.ui.FollowFragment
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position + 1)
    }
}