package com.mangpo.bookclub.view.alarm

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.databinding.FragmentAlarmBinding
import com.mangpo.bookclub.model.remote.Invite
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.AlarmBannerVPAdapter
import com.mangpo.bookclub.viewmodel.ClubViewModel

class AlarmFragment : BaseFragment<FragmentAlarmBinding>(FragmentAlarmBinding::inflate) {
    private val clubVm: ClubViewModel by viewModels()

    private lateinit var adapter: AlarmBannerVPAdapter

    override fun initAfterBinding() {
        observe()

        clubVm.getInviteList()
    }

    private fun initAdapter(inviteList: List<Invite>) {
        val fragments: ArrayList<Fragment> = arrayListOf()

        if (inviteList.isNotEmpty())
            fragments.add(BookClubInviteBannerFragment(inviteList))
        fragments.addAll(arrayListOf(UserGuideBannerFragment(), ReviewBannerFragment()))

        adapter = AlarmBannerVPAdapter(this)
        adapter.setFragments(fragments)
        binding.alarmVp.adapter = adapter

        binding.alarmIndicator.setViewPager(binding.alarmVp)
        adapter.registerAdapterDataObserver(binding.alarmIndicator.adapterDataObserver);
    }

    private fun observe() {
        clubVm.inviteList.observe(viewLifecycleOwner, Observer {
            LogUtil.d("AlarmFragment", "inviteList Observe! -> $it")
            initAdapter(it)
        })
    }

    fun goInviteHistoryFragment(invite: List<Invite>) {
        val action = AlarmFragmentDirections.actionAlarmFragmentToInviteListFragment(invite.toTypedArray())
        findNavController().navigate(action)
    }
}