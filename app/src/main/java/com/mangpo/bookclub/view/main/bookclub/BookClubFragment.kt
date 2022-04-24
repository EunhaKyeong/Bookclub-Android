package com.mangpo.bookclub.view.main.bookclub

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubBinding
import com.mangpo.bookclub.model.remote.ClubInfo
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

    private fun bind(clubInfo: ClubInfo) {
        binding.bookClubClubTitleTv.text = clubInfo.name
        binding.bookClubClubDescTv.text = clubInfo.description
        binding.bookClubTotalMemberTv.text = "total ${clubInfo.totalUser} members"
        binding.bookClubClubLevelTv.text = "Lv.${clubInfo.level}\t|\t${clubInfo.totalPosts} pages"

        when (clubInfo.level) {
            1 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level1_character)
            2 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level2_character)
            3 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level3_character)
            4 -> binding.bookClubCharacterIv.setImageResource(R.drawable.ic_level4_character)
        }

        if (isToday(clubInfo.createdDate))
            binding.bookClubCongratulationLayout.visibility = View.VISIBLE
        else
            binding.bookClubCongratulationLayout.visibility = View.GONE
    }

    private fun isToday(createdDate: String): Boolean {
        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val nowSeoul: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

        return nowSeoul.toLocalDate().isEqual(LocalDateTime.parse(createdDate.substring(0, 19), formatter1).toLocalDate())
    }

    private fun observe() {
        clubVm.clubInfo.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookClubFragment", "clubVm Observe!! ->$it")
            bind(it)
        })
    }
}