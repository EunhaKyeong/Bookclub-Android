package com.mangpo.bookclub.view.alarm

import android.content.Intent
import android.net.Uri
import com.mangpo.bookclub.databinding.FragmentUserGuideBannerBinding
import com.mangpo.bookclub.view.BaseFragment

class UserGuideBannerFragment : BaseFragment<FragmentUserGuideBannerBinding>(FragmentUserGuideBannerBinding::inflate) {
    override fun initAfterBinding() {
        binding.userGuideTitleTv.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://coconut-cheese-fcb.notion.site/Our-page-ee16685dfdcf477aa0c713ccb90b1d83")))
        }

        binding.userGuideSubTv.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://coconut-cheese-fcb.notion.site/Our-page-ee16685dfdcf477aa0c713ccb90b1d83")))
        }
    }
}