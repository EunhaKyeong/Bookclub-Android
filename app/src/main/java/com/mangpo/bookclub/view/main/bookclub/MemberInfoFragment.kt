package com.mangpo.bookclub.view.main.bookclub

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentMemberInfoBinding
import com.mangpo.bookclub.model.remote.Member
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.viewmodel.ClubViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MemberInfoFragment : BaseFragment<FragmentMemberInfoBinding>(FragmentMemberInfoBinding::inflate) {
    private val args: MemberInfoFragmentArgs by navArgs()
    private val clubVm: ClubViewModel by viewModels()

    override fun initAfterBinding() {
        setMyEventListener()
        observe()

        clubVm.getClubUserInfo(args.clubId, args.memberId)
    }

    private fun setMyEventListener() {
        binding.memberInfoTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addGenreChip(genres: List<String>) {
        for (genre in genres) {
            val chip: Chip =
                layoutInflater.inflate(R.layout.chip_genre_my_info, binding.memberInfoGenreCg, false) as Chip
            chip.text = genre
            binding.memberInfoGenreCg.addView(chip)
        }
    }

    private fun bind(member: Member) {
        if (member.userResponseDto.profileImgLocation==null)
            binding.memberInfoMemberIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_default_profile))
        else
            Glide.with(requireView()).load(member.userResponseDto.profileImgLocation).circleCrop().into(binding.memberInfoMemberIv)

        if (args.isPresident) {
            binding.memberInfoPresTv.visibility = View.VISIBLE
            binding.memberInfoMemTv.visibility = View.INVISIBLE
        } else {
            binding.memberInfoPresTv.visibility = View.INVISIBLE
            binding.memberInfoMemTv.visibility = View.VISIBLE
        }

        binding.memberInfoSignDateTv.text = dateFormatting(member.invitedDate)
        binding.memberInfoNicknameTv.text = member.userResponseDto.nickname
        binding.memberInfoEmailTv.text = member.userResponseDto.email
        binding.memberInfoRecordTv.text = "작성 기록 수    ${member.totalPosts} pages"
        binding.memberInfoBookTv.text = "추가한 책 수    ${member.totalBooks} books"
        binding.memberInfoCommentTv.text = "남긴 댓글 수    ${member.totalComments} replies"
        binding.memberInfoIntroduceTv.text = member.userResponseDto.introduce
        addGenreChip(member.userResponseDto.genres)
        binding.memberInfoReadingStyleTv.text = member.userResponseDto.style
    }

    private fun dateFormatting(date: String): String {
        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatter2 = DateTimeFormatter.ofPattern("yy.MM.dd 가입")
        val date: LocalDateTime = LocalDateTime.parse(date.substring(0, 19), formatter1)

        return  date.format(formatter2)
    }

    private fun observe() {
        clubVm.memberInfo.observe(viewLifecycleOwner, Observer {
            LogUtil.d("MemberInfoFragment", "memberInfo Observe!! -> $it")

            if (it==null)
                showToast(getString(R.string.error_api))
            else
                bind(it)
        })
    }
}