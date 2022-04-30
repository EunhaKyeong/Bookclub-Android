package com.mangpo.bookclub.view.main.bookclub

import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentInviteMemberBinding
import com.mangpo.bookclub.view.BaseFragment

class InviteMemberFragment : BaseFragment<FragmentInviteMemberBinding>(FragmentInviteMemberBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        binding.inviteMemberCompleteTv.setOnClickListener {
            findNavController().navigate(R.id.action_inviteMemberFragment_to_inviteCompleteDialogFragment)
        }

        binding.inviteMemberTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}