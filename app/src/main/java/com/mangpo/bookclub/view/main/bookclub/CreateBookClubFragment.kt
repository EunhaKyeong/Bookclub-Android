package com.mangpo.bookclub.view.main.bookclub

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentCreateBookClubBinding
import com.mangpo.bookclub.model.entities.CreateClubEntity
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.dialog.LoadingDialogFragment
import com.mangpo.bookclub.viewmodel.ClubViewModel

class CreateBookClubFragment : BaseFragment<FragmentCreateBookClubBinding>(FragmentCreateBookClubBinding::inflate) {
    private val clubVm: ClubViewModel by viewModels<ClubViewModel>()
    private val loadingDialogFragment: LoadingDialogFragment = LoadingDialogFragment()

    override fun initAfterBinding() {
        setMyEventListener()
        observe()
    }

    private fun setMyEventListener() {
        binding.createBookClubTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.createBookClubCompleteTv.setOnClickListener {
            hideKeyboard()

            if (binding.createBookClubClubNameEt.text.isBlank() || binding.createBookClubClubIntroduceEt.text.isBlank())
                showToast(getString(R.string.msg_input_content))
            else {
                val newClub: CreateClubEntity = CreateClubEntity(binding.createBookClubClubNameEt.text.toString(), binding.createBookClubClubIntroduceEt.text.toString())
                clubVm.createClub(newClub)
                loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
            }
        }
    }

    private fun observe() {
        clubVm.createClubCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("CreateBookClubFragment", "createClubCode Observe! -> $code")

            if (code!=null) {
                loadingDialogFragment.dismiss()

                when (code) {
                    201 -> {
                        val action = CreateBookClubFragmentDirections.actionCreateBookClubFragmentToClubCreationSuccessDialogFragment(clubVm.getNewClubId())
                        findNavController().navigate(action)
                    }
                    400 -> showToast("이미 존재하는 클럽 이름입니다.")
                    else -> showToast(getString(R.string.error_api))
                }
            }
        })
    }
}