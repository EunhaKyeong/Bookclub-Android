package com.mangpo.bookclub.view.main.bookclub

import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubMainBinding
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.ClubVerBigRVAdapter

class BookClubMainFragment : BaseFragment<FragmentBookClubMainBinding>(FragmentBookClubMainBinding::inflate) {
    private lateinit var clubVerBigAdapter: ClubVerBigRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        setMyEventListener()
    }

    private fun initAdapter() {
        clubVerBigAdapter = ClubVerBigRVAdapter()
        clubVerBigAdapter.setMyClickListener(object : ClubVerBigRVAdapter.MyClickListener {
            override fun goClubDetailView() {
                findNavController().navigate(R.id.action_initFragment_to_bookClubFragment)
            }
        })
        binding.bookClubMainClubRv.adapter = clubVerBigAdapter
    }

    private fun setMyEventListener() {
        binding.bookClubMainTb.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_initFragment_to_settingActivity)
        }

        binding.bookClubMainSettingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_initFragment_to_bookClubSettingFragment)
        }

        binding.bookClubMainAddBtn.setOnClickListener {
            findNavController().navigate(R.id.action_initFragment_to_createBookClubFragment)
        }
    }
}