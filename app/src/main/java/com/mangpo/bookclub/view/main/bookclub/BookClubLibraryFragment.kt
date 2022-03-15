package com.mangpo.bookclub.view.main.bookclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubLibraryBinding
import com.mangpo.bookclub.view.BaseFragment

class BookClubLibraryFragment : BaseFragment<FragmentBookClubLibraryBinding>(FragmentBookClubLibraryBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        binding.bookClubLibraryBackIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}