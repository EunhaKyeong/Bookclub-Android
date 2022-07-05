package com.mangpo.bookclub.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookCategoryBottomSheetBinding

class BookCategoryBottomSheetFragment : BottomSheetDialogFragment() {
    interface MyDialogCallback {
        fun getCategory(category: String)
    }

    private lateinit var binding: FragmentBookCategoryBottomSheetBinding
    private lateinit var myDialogCallback: MyDialogCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookCategoryBottomSheetBinding.inflate(inflater, container, false)
        setMyEventListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.bookCategoryRg.clearCheck()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    private fun setMyEventListener() {
        binding.bookCategoryBtn.setOnClickListener {
            when (binding.bookCategoryRg.checkedRadioButtonId) {
                R.id.book_category_reading_rb -> {
                    myDialogCallback.getCategory("NOW")
                    dismiss()
                }
                R.id.book_category_read_complete_rb -> {
                    myDialogCallback.getCategory("AFTER")
                    dismiss()
                }
                R.id.book_category_read_before_rb -> {
                    myDialogCallback.getCategory("BEFORE")
                    dismiss()
                }
                else -> Toast.makeText(requireContext(), getString(R.string.msg_select_category), Toast.LENGTH_SHORT).show()
            }
        }

        binding.bookCategoryRg.setOnCheckedChangeListener { radioGroup, i ->
            binding.bookCategoryBtn.isEnabled = radioGroup.checkedRadioButtonId!=-1
        }
    }

    fun setMyDialogCallback(myDialogCallback: MyDialogCallback) {
        this.myDialogCallback = myDialogCallback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 23 / 100
        // 기기 높이 대비 비율 설정 부분!!
        // 위 수치는 기기 높이 대비 80%로 다이얼로그 높이를 설정
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}