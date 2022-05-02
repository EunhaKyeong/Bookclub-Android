package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentClubCreationSuccessDialogBinding
import com.mangpo.bookclub.utils.DialogFragmentUtils

class ClubCreationSuccessDialogFragment: DialogFragment() {
    private val args: ClubCreationSuccessDialogFragmentArgs by navArgs()
    private lateinit var binding: FragmentClubCreationSuccessDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubCreationSuccessDialogBinding.inflate(inflater, container, false)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        //이미지뷰에 gif 파일 넣기
        Glide.with(this).load(R.raw.flipbook).into(binding.clubCreationSuccessFlipBookIv)

        binding.clubCreationSuccessNextTv.setOnClickListener {
            val action = ClubCreationSuccessDialogFragmentDirections.actionClubCreationSuccessDialogFragmentToBookClubFragment(args.clubId)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@ClubCreationSuccessDialogFragment,
            0.84f,
            0.29f
        )
    }

}