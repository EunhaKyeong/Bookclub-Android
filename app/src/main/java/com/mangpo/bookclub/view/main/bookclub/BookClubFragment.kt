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
import com.mangpo.bookclub.model.domain.RecordDetail
import com.mangpo.bookclub.model.remote.ClubInfo
import com.mangpo.bookclub.model.remote.TrendingPost
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
            findNavController().navigate(BookClubFragmentDirections.actionBookClubFragmentToBookClubLibraryFragment(club.bookAndUserDtos.toTypedArray(), club.userResponseDtos.toTypedArray(), club.clubResponseDto.name))
        }
    }

    private fun initBookAdapter() {
        bookAdapter = BookVerClubRVAdapter()
        binding.bookClubBookRv.adapter = bookAdapter
    }

    private fun initMemoAdapter() {
        memoAdapter = MemoVerClubRVAdapter()
        memoAdapter.setMyClickListener(object : MemoVerClubRVAdapter.MyClickListener {
            override fun goPostDetail(post: TrendingPost) {
                findNavController().navigate(BookClubFragmentDirections.actionBookClubFragmentToRecordDetailFragment(mappingToRecord(post)))
            }
        })
        binding.bookClubMemoRv.adapter = memoAdapter
    }

    private fun bind() {
        binding.bookClubClubTitleTv.text = club.clubResponseDto.name
        binding.bookClubClubDescTv.text = club.clubResponseDto.description
        binding.bookClubTotalMemberTv.text = "total ${club.totalUser} members"
        binding.bookClubClubLevelTv.text = "Lv.${club.clubResponseDto.level}\t|\t${club.totalPosts} pages"

        when (club.clubResponseDto.level) {
            1 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level1_character)
            2 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level2_character)
            3 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level3_character)
            4 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level4_character)
        }

        if (isToday(club.clubResponseDto.createdDate))
            binding.bookClubCongratulationLayout.visibility = View.VISIBLE
        else
            binding.bookClubCongratulationLayout.visibility = View.GONE

        if (club.bookAndUserDtos.isEmpty()) {
            binding.bookClubClubBooksBasedTv.text = getString(R.string.msg_no_memo_in_club)
            binding.bookClubBookRv.visibility = View.INVISIBLE
        } else {
            binding.bookClubClubBooksBasedTv.text = ""
            binding.bookClubBookRv.visibility = View.VISIBLE
            bookAdapter.setData(club.bookAndUserDtos)
        }

        if (club.trendingPostDtos.isEmpty()) {
            binding.bookClubTrendingMemoBasedTv.text = getString(R.string.msg_no_trending_memo)
            binding.bookClubMemoRv.visibility = View.INVISIBLE
        } else {
            binding.bookClubTrendingMemoBasedTv.text = ""
            binding.bookClubMemoRv.visibility = View.VISIBLE
            memoAdapter.setData(club.trendingPostDtos)
        }

        clubVm.setBookImg(club.bookAndUserDtos)
    }

    private fun isToday(createdDate: String): Boolean {
        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val nowSeoul: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

        return nowSeoul.toLocalDate().isEqual(LocalDateTime.parse(createdDate.substring(0, 19), formatter1).toLocalDate())
    }

    private fun mappingToClubInfoEntity(): ClubInfoEntity {
        val members: ArrayList<ClubMemberDetail> = extractMember(club.userResponseDtos)

        return ClubInfoEntity(
            clubId = club.clubResponseDto.id,
            clubName = club.clubResponseDto.name,
            clubLevel = club.clubResponseDto.level,
            totalMemberCnt = club.totalUser,
            totalMemoCnt = club.totalPosts,
            totalBookCnt = club.totalBooks,
            totalCommentCnt = club.totalComments,
            totalLikeCnt = club.totalLikes,
            presidentId = club.clubResponseDto.presidentId,
            members = members
        )
    }

    private fun mappingToRecord(trendingPost: TrendingPost): RecordDetail = RecordDetail(
        recordId = trendingPost.postResponseDto.postId,
        date = trendingPost.postResponseDto.modifiedDate,
        bookName = trendingPost.bookName,
        writer = trendingPost.nickname,
        scope = "PUBLIC",   //여기 개노답인데
        title = trendingPost.postResponseDto.title,
        content = trendingPost.postResponseDto.content,
        photos = trendingPost.postResponseDto.postImgLocations,
        location = trendingPost.postResponseDto.location,
        readTime = trendingPost.postResponseDto.readTime,
        hyperlinks = trendingPost.postResponseDto.linkResponseDtos,
        likes = trendingPost.postResponseDto.likedList,
        comments = trendingPost.postResponseDto.commentsDto,
        clubList = arrayListOf()    //여기 개노답인데
    )

    private fun extractMember(userResponses: ArrayList<UserResponse>): ArrayList<ClubMemberDetail> {
        val members: ArrayList<ClubMemberDetail> = arrayListOf()
        for (user in userResponses) {
            members.add(ClubMemberDetail(memberId = user.userId, nickname = user.nickname, profile = user.profileImgLocation!!, introduce = user.introduce!!))
        }

        return members
    }

    private fun observe() {
        clubVm.clubInfo.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookClubFragment", "clubVm Observe!! -> $it")

            club = it
            bind()
        })

        clubVm.books.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookClubFragment", "books Observe!! -> $it")
            bookAdapter.setData(it)
        })
    }
}