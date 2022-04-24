package com.mangpo.bookclub.view.main.bookclub

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubSettingBinding
import com.mangpo.bookclub.model.entities.ClubShortEntity
import com.mangpo.bookclub.model.remote.Club
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.PrefsUtils
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.ClubVerSmallRVAdapter
import com.mangpo.bookclub.view.dialog.ActionDialogFragment
import com.mangpo.bookclub.viewmodel.ClubViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookClubSettingFragment :
    BaseFragment<FragmentBookClubSettingBinding>(FragmentBookClubSettingBinding::inflate) {
    private val clubVm: ClubViewModel by viewModels<ClubViewModel>()
    private val presidentClubs: ArrayList<ClubShortEntity> = arrayListOf()
    private val memberClubs: ArrayList<ClubShortEntity> = arrayListOf()

    private lateinit var clubVerPresRVAdapter: ClubVerSmallRVAdapter
    private lateinit var clubVerMemRVAdapter: ClubVerSmallRVAdapter

    override fun initAfterBinding() {
        observe()
        initAdapter()
        setMyEventListener()

        clubVm.getClubsByUser(PrefsUtils.getUserId())
    }

    private fun initAdapter() {
        clubVerPresRVAdapter = ClubVerSmallRVAdapter("PRES")
        clubVerPresRVAdapter.setMyClickListener(object : ClubVerSmallRVAdapter.MyClickListener {
            override fun delete(clubId: Int) {
                val bundle: Bundle = Bundle()
                bundle.putString("title", getString(R.string.msg_delete_book_club))
                bundle.putString("desc", getString(R.string.msg_no_restore_after_delete))

                val actionDialog: ActionDialogFragment = ActionDialogFragment()
                actionDialog.arguments = bundle
                actionDialog.show(requireActivity().supportFragmentManager, null)
                actionDialog.setMyDialogCallback(object : ActionDialogFragment.MyDialogCallback{
                    override fun delete() {
                        clubVm.deleteClub(clubId)
                    }
                })
            }
        })
        binding.bookClubSettingCreateClubRv.adapter = clubVerPresRVAdapter

        clubVerMemRVAdapter = ClubVerSmallRVAdapter("MEM")
        clubVerMemRVAdapter.setMyClickListener(object : ClubVerSmallRVAdapter.MyClickListener {
            override fun delete(clubId: Int) {
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

    private fun setClubData(clubs: ArrayList<Club>) {
        presidentClubs.clear()
        memberClubs.clear()

        for (club in clubs) {
            if (club.presidentId==PrefsUtils.getUserId())
                presidentClubs.add(ClubShortEntity(club.id, club.name, dateFormatting(club.createdDate)))
            else
                memberClubs.add(ClubShortEntity(club.id, club.name, dateFormatting(club.createdDate)))
        }
    }

    private fun dateFormatting(date: String): String {
        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy년 MM년 dd일 개설")
        val date: LocalDateTime = LocalDateTime.parse(date.substring(0, 19), formatter1)

        return  date.format(formatter2)
    }

    private fun bindClubInfo() {
        if (presidentClubs.isEmpty()) {
            binding.bookClubSettingCreateClubRv.visibility = View.INVISIBLE
            binding.bookClubSettingCreateNoClubView.visibility = View.VISIBLE
            binding.bookClubSettingCreateClubCharacterIv.visibility = View.VISIBLE
            binding.bookClubSettingNoCreateClubTv.visibility = View.VISIBLE
        } else {
            clubVerPresRVAdapter.setData(presidentClubs)
            binding.bookClubSettingCreateClubRv.visibility = View.VISIBLE
            binding.bookClubSettingCreateNoClubView.visibility = View.INVISIBLE
            binding.bookClubSettingCreateClubCharacterIv.visibility = View.INVISIBLE
            binding.bookClubSettingNoCreateClubTv.visibility = View.INVISIBLE
        }

        if (memberClubs.isEmpty()) {
            binding.bookClubSettingSignInClubRv.visibility = View.INVISIBLE
            binding.bookClubSettingSignInNoClubView.visibility = View.VISIBLE
            binding.bookClubSettingSignInClubCharacterIv.visibility = View.VISIBLE
            binding.bookClubSettingNoSignInClubTv.visibility = View.VISIBLE
        } else {
            clubVerMemRVAdapter.setData(memberClubs)
            binding.bookClubSettingSignInClubRv.visibility = View.VISIBLE
            binding.bookClubSettingSignInNoClubView.visibility = View.INVISIBLE
            binding.bookClubSettingSignInClubCharacterIv.visibility = View.INVISIBLE
            binding.bookClubSettingNoSignInClubTv.visibility = View.INVISIBLE
        }
    }

    private fun observe() {
        clubVm.clubList.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookClubSettingFragment", "clubList Observe!! -> $it")
            setClubData(it)
            bindClubInfo()
        })

        clubVm.deleteClubCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("BookClubSettingFragment", "deleteClubCode Observe!! -> $code")

            if (code!=null) {
                when (code) {
                    204 -> clubVm.getClubsByUser(PrefsUtils.getUserId())
                    else -> showToast(getString(R.string.error_api))
                }
            }
        })
    }
}