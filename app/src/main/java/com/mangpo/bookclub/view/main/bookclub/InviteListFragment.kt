package com.mangpo.bookclub.view.main.bookclub

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.bookclub.databinding.FragmentInviteListBinding
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.InviteHistoryRVAdapter

class InviteListFragment : BaseFragment<FragmentInviteListBinding>(FragmentInviteListBinding::inflate) {
    private val args: InviteListFragmentArgs by navArgs()

    private lateinit var adapter: InviteHistoryRVAdapter

    override fun initAfterBinding() {
        setMyEventListener()
        initAdapter()
    }

    private fun initAdapter() {
        adapter = InviteHistoryRVAdapter()
        adapter.setData(args.invites.toList())
        binding.inviteListRv.adapter = adapter
    }

    private fun setMyEventListener() {
        binding.inviteListTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}