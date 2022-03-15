package com.mangpo.bookclub.view.main.bookclub

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubSettingBinding
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.ClubVerSmallRVAdapter
import com.mangpo.bookclub.view.dialog.ActionDialogFragment

class BookClubSettingFragment :
    BaseFragment<FragmentBookClubSettingBinding>(FragmentBookClubSettingBinding::inflate) {
    private lateinit var clubVerPresRVAdapter: ClubVerSmallRVAdapter
    private lateinit var clubVerMemRVAdapter: ClubVerSmallRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        setMyEventListener()
    }

    private fun initAdapter() {
        clubVerPresRVAdapter = ClubVerSmallRVAdapter("PRES")
        clubVerPresRVAdapter.setMyClickListener(object : ClubVerSmallRVAdapter.MyClickListener {
            override fun delete(role: String) {
                val bundle: Bundle = Bundle()
                bundle.putString("title", getString(R.string.msg_delete_book_club))
                bundle.putString("desc", getString(R.string.msg_no_restore_after_delete))

                val actionDialog: ActionDialogFragment = ActionDialogFragment()
                actionDialog.arguments = bundle
                actionDialog.show(requireActivity().supportFragmentManager, null)
            }
        })
        binding.bookClubSettingCreateClubRv.adapter = clubVerPresRVAdapter

        clubVerMemRVAdapter = ClubVerSmallRVAdapter("MEM")
        clubVerMemRVAdapter.setMyClickListener(object : ClubVerSmallRVAdapter.MyClickListener {
            override fun delete(role: String) {
                val bundle: Bundle = Bundle()
                bundle.putString("title", getString(R.string.msg_resign_book_club))
                bundle.putString("desc", getString(R.string.msg_no_restore_after_resign))

                val actionDialog: ActionDialogFragment = ActionDialogFragment()
                actionDialog.arguments = bundle
                actionDialog.show(requireActivity().supportFragmentManager, null)
            }
        })
        binding.bookClubSettingSignInClubRv.adapter = clubVerMemRVAdapter
    }

    private fun setMyEventListener() {
        binding.bookClubSettingTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}