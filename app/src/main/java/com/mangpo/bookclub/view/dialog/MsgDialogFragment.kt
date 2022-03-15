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
import com.mangpo.bookclub.databinding.FragmentMsgDialogBinding
import com.mangpo.bookclub.utils.DialogFragmentUtils

class MsgDialogFragment : DialogFragment() {
    private val args: MsgDialogFragmentArgs by navArgs()

    private lateinit var binding: FragmentMsgDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMsgDialogBinding.inflate(inflater, container, false)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.msgDialogTv.text = args.msg

        binding.msgDialogConfirmTv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@MsgDialogFragment,
            0.74f,
            0.15f
        )
    }
}