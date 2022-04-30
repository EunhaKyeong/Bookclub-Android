package com.mangpo.bookclub.view.main.bookclub

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubBinding
import com.mangpo.bookclub.model.entities.ClubInfoEntity
import com.mangpo.bookclub.model.entities.ClubMemberDetail
import com.mangpo.bookclub.model.remote.ClubInfo
import com.mangpo.bookclub.model.remote.UserResponse
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.BookVerClubRVAdapter
import com.mangpo.bookclub.view.adpater.MemoVerClubRVAdapter
import com.mangpo.bookclub.viewmodel.ClubViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class BookClubFragment : BaseFragment<FragmentBookClubBinding>(FragmentBookClubBinding::inflate) {
    private val args: BookClubFragmentArgs by navArgs()
    private val clubVm: ClubViewModel by viewModels<ClubViewModel>()

    private lateinit var bookAdapter: BookVerClubRVAdapter
    private lateinit var memoAdapter: MemoVerClubRVAdapter
    private lateinit var club: ClubInfo

    override fun initAfterBinding() {
        setMyEventListener()
        initBookAdapter()
        initMemoAdapter()
        observe()

        clubVm.getClubInfoByClubId(args.clubId)
    }

    private fun setMyEventListener() {
        binding.bookClubSettingIv.setOnClickListener {
            findNavController().navigate(R.id.action_bookClubFragment_to_settingActivity)
        }

        binding.bookClubNextIv.setOnClickListener {
            val action = BookClubFragmentDirections.actionBookClubFragmentToClubInfoFragment(mappingToClubInfoEntity())
            findNavController().navigate(action)
        }

        binding.bookClubInviteBtn.setOnClickListener {
            findNavController().navigate(R.id.action_bookClubFragment_to_clubInfoFragment)
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

    private fun bind() {
        binding.bookClubClubTitleTv.text = club.name
        binding.bookClubClubDescTv.text = club.description
        binding.bookClubTotalMemberTv.text = "total ${club.totalUser} members"
        binding.bookClubClubLevelTv.text = "Lv.${club.level}\t|\t${club.totalPosts} pages"

        when (club.level) {
            1 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level1_character)
            2 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level2_character)
            3 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level3_character)
            4 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level4_character)
        }

        if (isToday(club.createdDate))
            binding.bookClubCongratulationLayout.visibility = View.VISIBLE
        else
            binding.bookClubCongratulationLayout.visibility = View.GONE

        if (club.bookAndUserDtos.isEmpty()) {
            binding.bookClubClubBooksBasedTv.text = getString(R.string.msg_no_memo_in_club)
            binding.bookClubBookRv.visibility = View.INVISIBLE
        } else {
            binding.bookClubClubBooksBasedTv.text = ""
            binding.bookClubBookRv.visibility = View.VISIBLE
        }

        if (club.trendingPosts.isEmpty()) {
            binding.bookClubTrendingMemoBasedTv.text = getString(R.string.msg_no_trending_memo)
            binding.bookClubMemoRv.visibility = View.INVISIBLE
        } else {
            binding.bookClubTrendingMemoBasedTv.text = ""
            binding.bookClubMemoRv.visibility = View.VISIBLE
        }
    }

    private fun isToday(createdDate: String): Boolean {
        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val nowSeoul: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

        return nowSeoul.toLocalDate().isEqual(LocalDateTime.parse(createdDate.substring(0, 19), formatter1).toLocalDate())
    }

    private fun mappingToClubInfoEntity(): ClubInfoEntity {
        val members: ArrayList<ClubMemberDetail> = extractMember(club.userResponseDtos)

        return ClubInfoEntity(
            clubId = club.id,
            clubName = club.name,
            clubLevel = club.level,
            totalMemberCnt = club.totalUser,
            totalMemoCnt = club.totalPosts,
            totalBookCnt = club.totalBooks,
            totalCommentCnt = club.totalComments,
            totalLikeCnt = club.totalLikes,
            presidentId = club.presidentId,
            members = members
        )
    }

    private fun extractMember(userResponses: ArrayList<UserResponse>): ArrayList<ClubMemberDetail> {
        val members: ArrayList<ClubMemberDetail> = arrayListOf()
        for (user in userResponses) {
            members.add(ClubMemberDetail(memberId = user.userId, nickname = user.nickname, profile = user.profileImgLocation!!, introduce = user.introduce!!))
        }

        return members
    }

    private fun observe() {
        clubVm.clubInfo.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookClubFragment", "clubVm Observe!! ->$it")

            club = it
            bind()
        })
    }
}