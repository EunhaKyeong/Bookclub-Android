package com.mangpo.bookclub.view.main.bookclub

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubMainBinding
import com.mangpo.bookclub.model.entities.ClubEntity
import com.mangpo.bookclub.model.entities.ClubMember
import com.mangpo.bookclub.model.remote.Club
import com.mangpo.bookclub.utils.ClubViewType
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.PrefsUtils
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.ClubVerBigRVAdapter
import com.mangpo.bookclub.viewmodel.ClubViewModel

class BookClubMainFragment : BaseFragment<FragmentBookClubMainBinding>(FragmentBookClubMainBinding::inflate) {
    private lateinit var clubVerBigAdapter: ClubVerBigRVAdapter

    private val clubVm: ClubViewModel by viewModels<ClubViewModel>()
    private val clubs: ArrayList<ClubEntity> = arrayListOf(ClubEntity(), ClubEntity(), ClubEntity())

    override fun initAfterBinding() {
        observe()
        initAdapter()
        setMyEventListener()

        clubVm.getClubsByUser(PrefsUtils.getUserId())
    }

    private fun initAdapter() {
        clubVerBigAdapter = ClubVerBigRVAdapter()
        clubVerBigAdapter.setMyClickListener(object : ClubVerBigRVAdapter.MyClickListener {
            override fun goClubDetailView(clubId: Int) {
                val action = BookClubMainFragmentDirections.actionInitFragmentToBookClubFragment(clubId)
                findNavController().navigate(action)
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
            if (isOver()) {   //클럽 3개에 모두 가입한 사람이면 MsgDialogFragment 띄우기
                val action = BookClubMainFragmentDirections.actionInitFragmentToMsgDialogFragment(getString(R.string.msg_club_exceed))
                findNavController().navigate(action)
            } else    //가입할 수 있으면 북클럽 생성 화면으로
                findNavController().navigate(R.id.action_initFragment_to_createBookClubFragment)
        }
    }

    private fun isOver(): Boolean {
        for (club in clubs) {
            if (club.clubId==null)
                return false
        }

        return true
    }

    private fun mappingClub(clubs: ArrayList<Club>) {
        clubs.forEachIndexed{ index, club ->
            this.clubs[index].viewType = ClubViewType.CLUB
            this.clubs[index].clubId = club.id
            this.clubs[index].name = club.name
            this.clubs[index].description = club.description
            this.clubs[index].lastUpdatedDate = club.modifiedDate
            this.clubs[index].level = club.level
            this.clubs[index].president = ClubMember(memberId = club.presidentId)
        }

        clubVerBigAdapter.setClubs(this.clubs)
    }

    private fun observe() {
        clubVm.clubList.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookClubMainFragment", "clubList Observe! -> $it")
            mappingClub(it)
        })
    }
}