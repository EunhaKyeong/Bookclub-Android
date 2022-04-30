package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.mangpo.bookclub.databinding.FragmentMsgDescDialogBinding
import com.mangpo.bookclub.utils.DialogFragmentUtils

class MsgDescDialogFragment : DialogFragment() {
    private val args: MsgDescDialogFragmentArgs by navArgs()

    private lateinit var binding: FragmentMsgDescDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMsgDescDialogBinding.inflate(inflater, container, false)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.msgDescMsgTv.text = args.title
        binding.msgDescDescTv.text = args.desc

        binding.msgDescConfirmTv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@MsgDescDialogFragment,
            0.74f,
            0.13f
        )
    }
}