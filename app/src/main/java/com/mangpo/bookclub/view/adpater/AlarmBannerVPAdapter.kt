package com.mangpo.bookclub.view.adpater

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlarmBannerVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private var fragments: ArrayList<Fragment> = arrayListOf()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun setFragments(fragments: ArrayList<Fragment>) {
        this.fragments = fragments
        notifyDataSetChanged()
    }
}