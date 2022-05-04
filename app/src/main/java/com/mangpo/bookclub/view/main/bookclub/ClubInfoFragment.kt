package com.mangpo.bookclub.view.main.bookclub

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.bookclub.R
import com.mangpo.bookclub.config.GlobalVariable
import com.mangpo.bookclub.databinding.FragmentClubInfoBinding
import com.mangpo.bookclub.model.entities.ClubInfoEntity
import com.mangpo.bookclub.utils.convertDpToPx
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.MemberRVAdapter

class ClubInfoFragment : BaseFragment<FragmentClubInfoBinding>(FragmentClubInfoBinding::inflate) {
    private val args: ClubInfoFragmentArgs by navArgs<ClubInfoFragmentArgs>()

    private lateinit var memberRVAdapter: MemberRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        setMyEventListener()
        bind(args.clubInfo)
    }

    private fun setMyEventListener() {
        binding.clubInfoTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.clubInfoAddClubBtn.setOnClickListener {
            findNavController().navigate(ClubInfoFragmentDirections.actionClubInfoFragmentToInviteMemberFragment(args.clubInfo.clubId))
        }
    }

    private fun initAdapter() {
        memberRVAdapter = MemberRVAdapter()
        memberRVAdapter.setMyClickListener(object : MemberRVAdapter.MyClickListener {
            override fun goMemberInfoView(memberId: Int) {
                if (memberId==args.clubInfo.presidentId)
                    findNavController().navigate(ClubInfoFragmentDirections.actionClubInfoFragmentToMemberInfoFragment(memberId, args.clubInfo.clubId, true))
                else
                    findNavController().navigate(ClubInfoFragmentDirections.actionClubInfoFragmentToMemberInfoFragment(memberId, args.clubInfo.clubId))
            }
        })

        binding.clubInfoRv.adapter = memberRVAdapter
    }

    private fun bind(club: ClubInfoEntity) {
        binding.clubInfoTb.title = club.clubName

        binding.clubInfoLevelTv.text = "Lv.${club.clubLevel}"
        when (club.clubLevel) {
            1 -> binding.clubInfoCharacterIv.setImageResource(R.drawable.ic_level1_character)
            2 -> binding.clubInfoCharacterIv.setImageResource(R.drawable.ic_level2_character)
            3 -> binding.clubInfoCharacterIv.setImageResource(R.drawable.ic_level3_character)
            4 -> binding.clubInfoCharacterIv.setImageResource(R.drawable.ic_level4_character)
        }

        binding.clubInfoMemberCntTv.text = "멤버 : ${club.totalMemberCnt} / 10"

        binding.clubInfoRecordTv.text = "작성 기록 수\t\t\t${club.totalMemoCnt} pages"
        binding.clubInfoBookTv.text = "추가한 책 수\t\t\t${club.totalBookCnt} books"
        binding.clubInfoCommentTv.text = "남긴 댓글 수\t\t\t${club.totalCommentCnt} replies"
        binding.clubInfoLikeTv.text = "총 좋아요 수\t\t\t${club.totalLikeCnt} likes"

        if (club.presidentId==GlobalVariable.userId) {
            binding.clubInfoAddClubBtn.visibility = View.VISIBLE
            setRVTopMargin(12)
        } else {
            binding.clubInfoAddClubBtn.visibility = View.GONE
            setRVTopMargin(17)
        }

        val presidentInfo = club.members.removeAt(club.members.indexOf(club.members.find { it.memberId==club.presidentId }))
        club.members.add(0, presidentInfo)
        memberRVAdapter.setData(club.members)
    }

    private fun setRVTopMargin(marginTop: Int) {
        (binding.clubInfoRv.layoutParams as ConstraintLayout.LayoutParams).apply {
            topMargin = convertDpToPx(requireContext(), marginTop)
        }
    }
}