package com.mangpo.bookclub.view.main.bookclub

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentInviteMemberBinding
import com.mangpo.bookclub.model.entities.InviteMemRequest
import com.mangpo.bookclub.utils.AuthUtils
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.dialog.InviteCompleteDialogFragment
import com.mangpo.bookclub.viewmodel.AuthViewModel
import com.mangpo.bookclub.viewmodel.ClubViewModel
import java.util.regex.Pattern

class InviteMemberFragment : BaseFragment<FragmentInviteMemberBinding>(FragmentInviteMemberBinding::inflate), TextWatcher {
    private val args: InviteMemberFragmentArgs by navArgs()
    private val authVm: AuthViewModel by viewModels()
    private val clubVm: ClubViewModel by viewModels()

    private lateinit var inviteCompleteDialog: InviteCompleteDialogFragment

    override fun initAfterBinding() {
        setMyEventListener()
        initDialog()
        observe()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (binding.inviteMemberEmailEt.hasFocus()) {   //이메일 EditText 에 포커스
            val emailPattern: Pattern = Patterns.EMAIL_ADDRESS

            if (p0!!.isBlank()) { //입력이 비어있을 때
                binding.inviteMemberErrorTv.visibility = View.INVISIBLE
                binding.inviteMemberCompleteTv.isEnabled = false
            } /*else if (!emailPattern.matcher(binding.inviteMemberEmailEt.text.toString()).matches()) {    //이메일 형식이 아닐 때
                binding.inviteMemberErrorTv.text = getString(R.string.error_not_email_pattern)
                binding.inviteMemberErrorTv.visibility = View.VISIBLE
                binding.inviteMemberCompleteTv.isEnabled = false
            }*/ else  //이메일 형식일 때
                authVm.validateDuplicate(p0.toString())
        } else if (binding.inviteMemberMessageEt.hasFocus())    //초대메시지 EditText 에 포커스
            binding.inviteMemberCompleteTv.isEnabled = (p0!!.isNotBlank() && binding.inviteMemberEmailEt.text.isNotBlank() && binding.inviteMemberErrorTv.visibility != View.VISIBLE)
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setMyEventListener() {
        binding.inviteMemberEmailEt.addTextChangedListener(this)
        binding.inviteMemberMessageEt.addTextChangedListener(this)

        binding.inviteMemberCompleteTv.setOnClickListener {
            findNavController().navigate(R.id.action_inviteMemberFragment_to_inviteCompleteDialogFragment)
        }

        binding.inviteMemberTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.inviteMemberCompleteTv.setOnClickListener {
            hideKeyboard()

            if (binding.inviteMemberEmailEt.text.toString()==AuthUtils.getEmail())
                showToast("자신의 이메일임!")
            else
                clubVm.inviteMember(bindData())
        }
    }

    private fun initDialog() {
        inviteCompleteDialog = InviteCompleteDialogFragment()
        inviteCompleteDialog.setMyCallbackListener(object : InviteCompleteDialogFragment.MyCallbackListener {
            override fun complete() {
                clear()
            }
        })
    }

    private fun bindData(): InviteMemRequest {
        return InviteMemRequest(
            binding.inviteMemberEmailEt.text.toString(),
            args.clubId,
            binding.inviteMemberMessageEt.text.toString()
        )
    }

    private fun clear() {
        binding.inviteMemberEmailEt.text.clear()
        binding.inviteMemberEmailEt.clearFocus()
        binding.inviteMemberErrorTv.visibility = View.INVISIBLE
        binding.inviteMemberMessageEt.text.clear()
        binding.inviteMemberMessageEt.clearFocus()
    }

    private fun observe() {
        authVm.validateCode.observe(this, Observer {
            if (it == 400) {
                binding.inviteMemberErrorTv.visibility = View.INVISIBLE
                binding.inviteMemberCompleteTv.isEnabled = binding.inviteMemberMessageEt.text.isNotBlank()
            } else {
                binding.inviteMemberErrorTv.text = getString(R.string.error_not_sign_user)
                binding.inviteMemberErrorTv.visibility = View.VISIBLE
                binding.inviteMemberCompleteTv.isEnabled = false
            }
        })

        clubVm.inviteResultCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("InviteMemberFragment", "inviteResultCode Observe!! -> $code")

            if (code!=null) {
                when (code) {
                    201 -> inviteCompleteDialog.show(requireActivity().supportFragmentManager, null)
                    400 -> showToast(getString(R.string.error_already_invited_member))
                    else -> showToast(getString(R.string.error_api))
                }
            }
        })
    }
}