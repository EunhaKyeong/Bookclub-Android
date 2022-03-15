package com.mangpo.bookclub.view.main.bookclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubBinding
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.BookVerClubRVAdapter
import com.mangpo.bookclub.view.adpater.MemoVerClubRVAdapter

class BookClubFragment : BaseFragment<FragmentBookClubBinding>(FragmentBookClubBinding::inflate) {
    private lateinit var bookAdapter: BookVerClubRVAdapter
    private lateinit var memoAdapter: MemoVerClubRVAdapter

    override fun initAfterBinding() {
        setMyEventListener()
        initBookAdapter()
        initMemoAdapter()
    }

    private fun setMyEventListener() {
        binding.bookClubSettingIv.setOnClickListener {
            findNavController().navigate(R.id.action_bookClubFragment_to_settingActivity)
        }

        binding.bookClubNextIv.setOnClickListener {
            val action = BookClubFragmentDirections.actionBookClubFragmentToClubInfoFragment("PRES", false)    //PRES: 클럽장, MEM: 클럽원
            findNavController().navigate(action)
        }

        binding.bookClubInviteBtn.setOnClickListener {
            val action = BookClubFragmentDirections.actionBookClubFragmentToClubInfoFragment("PRES", true)    //PRES: 클럽장, MEM: 클럽원
            findNavController().navigate(action)
        }

        binding.bookClubMoreTv.setOnClickListener {
            findNavController().navigate(R.id.action_bookClubFragment_to_bookClubLibraryFragment)
        }
    }

    private fun initBookAdapter() {
        bookAdapter = BookVerClubRVAdapter()
        binding.bookClubBookRv.adapter = bookAdapter
    }

    private fun initMemoAdapter() {
        memoAdapter = MemoVerClubRVAdapter()
        binding.bookClubMemoRv.adapter = memoAdapter
    }
}