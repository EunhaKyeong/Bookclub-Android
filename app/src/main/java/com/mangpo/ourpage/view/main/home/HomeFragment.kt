package com.mangpo.ourpage.view.main.home

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentHomeBinding
import com.mangpo.ourpage.model.remote.Todo
import com.mangpo.ourpage.model.remote.UserResponse
import com.mangpo.ourpage.utils.PrefsUtils
import com.mangpo.ourpage.utils.fadeIn
import com.mangpo.ourpage.utils.fadeOut
import com.mangpo.ourpage.utils.isNetworkAvailable
import com.mangpo.ourpage.view.BaseFragment
import com.mangpo.ourpage.view.adpater.ChecklistContentRVAdapter
import com.mangpo.ourpage.viewmodel.TodoViewModel
import com.mangpo.ourpage.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val userVm: UserViewModel by viewModels<UserViewModel>()
    private val todoVm: TodoViewModel by activityViewModels<TodoViewModel>()

    private lateinit var checklistContentRVAdapter: ChecklistContentRVAdapter

    override fun initAfterBinding() {
        initChecklistAdapter()
        setMyEventClickListener()
        observe()

        //날짜 표시하기
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("LLLL dd.", Locale.ENGLISH)
        binding.homeDateTv.text = "The record for ${sdf.format(calendar.time)}"

        if (!isNetworkAvailable(requireContext()))
            Snackbar.make(requireView(), getString(R.string.error_check_network), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.action_retry)) {
                getInfo()
            }
        else
            getInfo()
    }

    private fun getInfo() {
        userVm.getUserInfo()
        userVm.getTotalMemoCnt()
        todoVm.getTodos()
    }

    private fun setMyEventClickListener() {
        binding.homeSettingIv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingActivity)
        }

        binding.homeMyInfoArrowIv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_infoActivity)
        }

        binding.homeGoalBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToGoalManagementActivity(Gson().toJson(userVm.getUser()))
            findNavController().navigate(action)
        }

        //체크리스트뷰 아래 화살표 클릭 리스너 -> 체크리스트 열기/닫기
        binding.homeChecklistOpenIv.setOnClickListener {
            if (binding.homeChecklistRv.visibility == View.GONE) {  //체크리스트 열기
                binding.homeChecklistTv.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_top_8_null_null)

                binding.homeChecklistRv.fadeIn()
            } else {    //체크리스트 닫기
                binding.homeChecklistTv.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_8_null_null)

                binding.homeChecklistRv.fadeOut()
            }
        }

        binding.homeChecklistSettingIv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_checklistManagementActivity)
        }

        binding.homeMemoBtn.setOnClickListener {
            PrefsUtils.setTempRecord("")
            val action = HomeFragmentDirections.actionHomeFragmentToRecordFragment("CREATE", null, null)
            findNavController().navigate(action)
        }
    }

    private fun initChecklistAdapter() {
        checklistContentRVAdapter = ChecklistContentRVAdapter()

        checklistContentRVAdapter.setMyChecklistContentRVAdapterListener(object : ChecklistContentRVAdapter.MyChecklistContentRVAdapterListener {
            override fun addChecklist() {
                val action = HomeFragmentDirections.actionHomeFragmentToChecklistDialogFragment(null)
                findNavController().navigate(action)
            }

            override fun updateChecklist(position: Int, checklist: Todo) {
                val action = HomeFragmentDirections.actionHomeFragmentToChecklistDialogFragment(Gson().toJson(checklist))
                findNavController().navigate(action)
            }

            override fun completeChecklist(position: Int, checklist: Todo) {
                if (!isNetworkAvailable(requireContext()))
                    showNetworkSnackBar()
                else
                    todoVm.updateTodo(checklist.toDoId, checklist.content, true)
            }

            override fun deleteChecklist(position: Int, checklistId: Int) {
                if (!isNetworkAvailable(requireContext()))
                    showNetworkSnackBar()
                else
                    todoVm.deleteTodo(checklistId)
            }

        })

        binding.homeChecklistRv.adapter = checklistContentRVAdapter
    }

    private fun bindUserInfo(user: UserResponse) {
        binding.homeWelcomeTv.text = "${user.nickname}님,\n오늘도 힘차게 기록해봅시다."

        if (user.goal!=null)
            binding.homeGoalBtn.text = user.goal

    }

    private fun observe() {
        userVm.getUserCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            Log.d("HomeFragment", "getUserCode Observe! getUserCode -> $code")

            if (code!=null) {
                when (code) {
                    200 -> bindUserInfo(userVm.getUser()!!)
                    else -> Snackbar.make(requireView(), getString(R.string.error_api), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.action_retry)) {
                        userVm.getUserInfo()
                    }
                }
            }
        })

        userVm.totalMemoCnt.observe(viewLifecycleOwner, Observer {
            Log.d("HomeFragment", "totalMemoCnt Observe! totalMemoCnt -> $it")

            if (it==-1)
                binding.homeTotalPagesTv.text = "total     pages"
            else
                binding.homeTotalPagesTv.text = "total $it pages"
        })

        todoVm.unCompleteTodos.observe(viewLifecycleOwner, Observer {
            Log.d("HomeFragment", "todos Observe! todos -> $it")

            checklistContentRVAdapter.setTodos(it as ArrayList<Todo>)
        })

        todoVm.completeTodoCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            Log.d("ChecklistDialogFragment", "completeTodoCode Observe! completeTodoCode -> $code")

            if (code!=null) {
                when (code) {
                    204 -> {
                        showToast(getString(R.string.msg_complete_checklist))
                        todoVm.getTodos()
                    }
                    else -> Snackbar.make(requireView(), getString(R.string.error_api), Snackbar.LENGTH_SHORT)
                }
            }
        })

        todoVm.deleteTodoCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            Log.d("ChecklistDialogFragment", "deleteTodoCode Observe! deleteTodoCode -> $code")

            if (code!=null) {
                when (code) {
                    204 -> todoVm.getTodos()
                    else -> Snackbar.make(requireView(), getString(R.string.error_api), Snackbar.LENGTH_SHORT)
                }
            }
        })
    }
}