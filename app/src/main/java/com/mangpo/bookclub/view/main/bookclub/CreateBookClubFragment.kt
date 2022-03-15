package com.mangpo.bookclub.view.main.bookclub

import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentCreateBookClubBinding
import com.mangpo.bookclub.view.BaseFragment

class CreateBookClubFragment : BaseFragment<FragmentCreateBookClubBinding>(FragmentCreateBookClubBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        binding.createBookClubTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.createBookClubCompleteTv.setOnClickListener {
            hideKeyboard()

            if (binding.createBookClubClubNameEt.text.isBlank() || binding.createBookClubClubIntroduceEt.text.isBlank())
                showToast(getString(R.string.msg_input_content))
            else
                findNavController().navigate(R.id.action_createBookClubFragment_to_bookClubFragment)
        }
    }
}