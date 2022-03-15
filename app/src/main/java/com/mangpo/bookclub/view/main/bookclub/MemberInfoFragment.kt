package com.mangpo.bookclub.view.main.bookclub

import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentMemberInfoBinding
import com.mangpo.bookclub.view.BaseFragment

class MemberInfoFragment : BaseFragment<FragmentMemberInfoBinding>(FragmentMemberInfoBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()

        addGenreChip(listOf("소설", "시", "희곡", "소설", "시", "희곡"))
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
}