package com.mangpo.bookclub.view.alarm

import com.mangpo.bookclub.databinding.FragmentBookClubInviteBannerBinding
import com.mangpo.bookclub.model.remote.Invite
import com.mangpo.bookclub.view.BaseFragment

class BookClubInviteBannerFragment(private val invite: List<Invite>) : BaseFragment<FragmentBookClubInviteBannerBinding>(FragmentBookClubInviteBannerBinding::inflate) {
    override fun initAfterBinding() {
        binding.root.setOnClickListener {
            (requireParentFragment() as AlarmFragment).goInviteHistoryFragment(invite)
        }

        binding.bookClubInviteBannerWhereClubTv.text = "${invite.size}개의 북클럽"
    }
}