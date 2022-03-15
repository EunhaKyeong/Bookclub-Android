package com.mangpo.bookclub.view.main.bookclub

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentClubInfoBinding
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.MemberRVAdapter

class ClubInfoFragment : BaseFragment<FragmentClubInfoBinding>(FragmentClubInfoBinding::inflate) {
    private val args: ClubInfoFragmentArgs by navArgs<ClubInfoFragmentArgs>()

    private lateinit var memberRVAdapter: MemberRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        setMyEventListener()

        if (args.isFirst)
            findNavController().navigate(R.id.action_clubInfoFragment_to_inviteMemDialogFragment)
    }

    private fun setMyEventListener() {
        binding.clubInfoTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.clubInfoAddClubBtn.setOnClickListener {
            findNavController().navigate(R.id.action_clubInfoFragment_to_inviteMemDialogFragment)
        }
    }

    private fun initAdapter() {
        memberRVAdapter = MemberRVAdapter()
        memberRVAdapter.setMyClickListener(object : MemberRVAdapter.MyClickListener {
            override fun goMemberInfoView() {
                findNavController().navigate(R.id.action_clubInfoFragment_to_memberInfoFragment)
            }
        })

        binding.clubInfoRv.adapter = memberRVAdapter
    }
}