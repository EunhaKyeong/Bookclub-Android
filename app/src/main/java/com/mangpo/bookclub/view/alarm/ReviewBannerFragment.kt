package com.mangpo.bookclub.view.alarm

import android.content.Intent
import android.net.Uri
import com.mangpo.bookclub.databinding.FragmentReviewBannerBinding
import com.mangpo.bookclub.view.BaseFragment

class ReviewBannerFragment : BaseFragment<FragmentReviewBannerBinding>(FragmentReviewBannerBinding::inflate) {
    override fun initAfterBinding() {
        binding.reviewBannerGoReviewTv.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mangpo.bookclub")))
        }

        binding.reviewBannerTitleTv.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mangpo.bookclub")))
        }
    }
}