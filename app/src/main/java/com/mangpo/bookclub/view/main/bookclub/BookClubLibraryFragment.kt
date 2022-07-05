package com.mangpo.bookclub.view.main.bookclub

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubLibraryBinding
import com.mangpo.bookclub.model.remote.UserResponse
import com.mangpo.bookclub.utils.convertDpToPx
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.BookRVAdapter

class BookClubLibraryFragment : BaseFragment<FragmentBookClubLibraryBinding>(FragmentBookClubLibraryBinding::inflate), TextWatcher {
    private val args: BookClubLibraryFragmentArgs by navArgs()
    private val subFilterColorStateList = intArrayOf(
        Color.parseColor("#303860"),    // chip checked color
        Color.parseColor("#EFF0F3")   // chip unchecked color
    )

    private lateinit var bookRVAdapter: BookRVAdapter

    override fun initAfterBinding() {
        setMyEventListener()
        initAdapter()

        binding.bookClubLibraryTitleTv.text = args.clubName

        val mainFilterColorStateList = intArrayOf(
            Color.parseColor("#FFFFFF"),    // chip checked color
            Color.parseColor("#00000000")   // chip unchecked color
        )
        chipBackgroundColorStates(binding.bookClubLibrarySearchChip, mainFilterColorStateList)
        chipBackgroundColorStates(binding.bookClubLibraryMemChip, mainFilterColorStateList)
        chipBackgroundColorStates(binding.bookClubLibrarySortChip, mainFilterColorStateList)

        chipBackgroundColorStates(binding.bookClubLibraryNewestChip, subFilterColorStateList)
        chipBackgroundColorStates(binding.bookClubLibraryOldestChip, subFilterColorStateList)
        chipBackgroundColorStates(binding.bookClubLibraryNameChip, subFilterColorStateList)

        addGenreChip(args.members.asList())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        bookRVAdapter.filter.filter(p0)
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun initAdapter() {
        bookRVAdapter = BookRVAdapter()
        bookRVAdapter.setData(args.books.toList())
        binding.bookClubLibraryRv.adapter = bookRVAdapter
    }

    private fun chipBackgroundColorStates(chip: Chip, colors: IntArray) {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )

        chip.chipBackgroundColor = ColorStateList(states, colors)
    }

    private fun setMyEventListener() {
        binding.bookClubLibraryBackIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.bookClubLibraryCg.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1)
                setRVMargin(18)
            else
                setRVMargin(64)
        }

        //검색
        binding.bookClubLibrarySearchChip.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.bookClubLibrarySearchEt.visibility = View.VISIBLE
            } else {
                binding.bookClubLibrarySearchEt.text.clear()
                bookRVAdapter.initData()
                binding.bookClubLibrarySearchEt.visibility = View.GONE
            }
        }

        //클럽원
        binding.bookClubLibraryMemChip.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                binding.bookClubLibraryMemberHsv.visibility = View.VISIBLE
            else
                binding.bookClubLibraryMemberHsv.visibility = View.GONE
        }

        //정렬
        binding.bookClubLibrarySortChip.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                binding.bookClubLibrarySortCg.visibility = View.VISIBLE
            else {
                binding.bookClubLibrarySortCg.clearCheck()
                bookRVAdapter.initData()
                binding.bookClubLibrarySortCg.visibility = View.GONE
            }
        }

        //정렬-최신순
        binding.bookClubLibraryNewestChip.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                bookRVAdapter.sortClubBooks("new")
            else if (!binding.bookClubLibraryOldestChip.isChecked && !binding.bookClubLibraryNameChip.isChecked)
                bookRVAdapter.initData()
        }

        //정렬-오래된순
        binding.bookClubLibraryOldestChip.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                bookRVAdapter.sortClubBooks("old")
            else if (!binding.bookClubLibraryNewestChip.isChecked && !binding.bookClubLibraryNameChip.isChecked)
                bookRVAdapter.initData()
        }

        //정렬-이름순
        binding.bookClubLibraryNameChip.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                bookRVAdapter.sortClubBooks("name")
            else if (!binding.bookClubLibraryNewestChip.isChecked && !binding.bookClubLibraryOldestChip.isChecked)
                bookRVAdapter.initData()
        }

        binding.bookClubLibrarySearchEt.addTextChangedListener(this)
    }

    private fun setRVMargin(margin: Int) {
        val horizontalMargin = convertDpToPx(requireContext(), 10)
        val topMargin = convertDpToPx(requireContext(), margin)

        binding.bookClubLibraryRv.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(horizontalMargin, topMargin, horizontalMargin, 0)
        }
    }

    private fun addGenreChip(members: List<UserResponse>) {
        binding.bookClubLibraryMemberCg.removeAllViews()

        for (member in members) {
            val chip: Chip = layoutInflater.inflate(
                R.layout.chip_member,
                binding.bookClubLibraryMemberCg,
                false
            ) as Chip

            chip.text = member.nickname
            chipBackgroundColorStates(chip, subFilterColorStateList)

            binding.bookClubLibraryMemberCg.addView(chip)
        }
    }
}