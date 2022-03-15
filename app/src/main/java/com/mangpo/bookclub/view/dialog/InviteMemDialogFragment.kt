package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentInviteMemDialogBinding
import com.mangpo.bookclub.utils.DialogFragmentUtils

class InviteMemDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentInviteMemDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInviteMemDialogBinding.inflate(inflater, container, false)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setMyEventListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@InviteMemDialogFragment,
            0.74f,
            0.22f
        )
    }

    private fun setMyEventListener() {
        binding.inviteMemCancelTv.setOnClickListener {
            dismiss()
        }

        binding.inviteMemInviteTv.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.action_inviteMemDialogFragment_to_inviteCompleteDialogFragment)
        }
    }
}