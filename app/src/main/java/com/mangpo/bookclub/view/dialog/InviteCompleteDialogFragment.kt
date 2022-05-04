package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.mangpo.bookclub.databinding.FragmentInviteCompleteDialogBinding
import com.mangpo.bookclub.utils.DialogFragmentUtils

class InviteCompleteDialogFragment : DialogFragment() {
    interface MyCallbackListener {
        fun complete()
    }

    private lateinit var binding: FragmentInviteCompleteDialogBinding
    private lateinit var myCallbackListener: MyCallbackListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInviteCompleteDialogBinding.inflate(inflater, container, false)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.inviteCompleteCompleteTv.setOnClickListener {
            myCallbackListener.complete()
            dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@InviteCompleteDialogFragment,
            0.71f,
            0.23f
        )
    }

    fun setMyCallbackListener(myCallbackListener: MyCallbackListener) {
        this.myCallbackListener = myCallbackListener
    }
}