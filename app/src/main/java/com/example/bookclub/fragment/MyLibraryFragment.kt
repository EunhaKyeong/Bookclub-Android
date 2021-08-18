package com.example.bookclub.fragment

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.viewpager2.widget.ViewPager2
import com.example.bookclub.R
import com.example.bookclub.adapter.MyLibraryPagerAdapter
import com.example.bookclub.databinding.FragmentMyLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MyLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMyLibraryBinding
    private var searchFlag = 0
    private var clubFlag = 0
    private var sortFlag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)

        //viewPager adapter 설정
        binding.viewPager.adapter = MyLibraryPagerAdapter()

        //tablayout이랑 viewPager 연결
        TabLayoutMediator(binding.readTypeTabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.reading)
                1 -> tab.text = getString(R.string.read_complete)
                else -> tab.text = getString(R.string.want_to_read)
            }
        }.attach()

        // Paging 완료되면 호출
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)

                filterClear()
                when(position) {
                    0 -> setVisibilityClubButton(View.VISIBLE)
                    1 -> setVisibilityClubButton(View.VISIBLE)
                    2 -> setVisibilityClubButton(View.GONE)
                }
            }
        })

        binding.searchButton.setOnClickListener {
            if (searchFlag==0) {
                searchFlag = 1
                clubFlag = 0
                sortFlag = 0
            } else {
                binding.filterRadioGroup.clearCheck()
                searchFlag = 0
            }
            //Log.d("현재 플래그", "검색: ${searchFlag}, 북클럽: ${clubFlag}, 정렬: ${sortFlag}")
        }

        binding.clubButton.setOnClickListener {
            if (clubFlag==0) {
                searchFlag = 0
                clubFlag = 1
                sortFlag = 0
            } else {
                binding.filterRadioGroup.clearCheck()
                clubFlag = 0
            }
            Log.d("현재 플래그", "검색: ${searchFlag}, 북클럽: ${clubFlag}, 정렬: ${sortFlag}")
        }

        binding.sortButton.setOnClickListener {
            if (sortFlag==0) {
                searchFlag = 0
                clubFlag = 0
                sortFlag = 1
            } else {
                binding.filterRadioGroup.clearCheck()
                sortFlag = 0
            }
        }

        //val sp: Float = 10 / resources.displayMetrics.scaledDensity
        //val dp: Float = 5 / resources.displayMetrics.density
        //Log.d("dp", dp.toString())

        return binding.root
    }

    private fun filterClear() {
        searchFlag = 0
        clubFlag = 0
        sortFlag = 0
        binding.filterRadioGroup.clearCheck()
    }

    private fun setVisibilityClubButton(visibility: Int) {
        binding.clubButton.visibility = visibility
    }
}