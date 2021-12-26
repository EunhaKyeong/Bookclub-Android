package com.mangpo.bookclub.view.write

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.databinding.FragmentWriteInitBinding
import com.mangpo.bookclub.view.SettingActivity
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.mangpo.bookclub.R
import com.mangpo.bookclub.model.CheckListModel
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.view.adapter.ChecklistContentRVAdapter
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.ChecklistViewModel
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteInitFragment : Fragment() {

    private lateinit var binding: FragmentWriteInitBinding

    private val postVm: PostViewModel by sharedViewModel()
    private val mainVm: MainViewModel by sharedViewModel()
    private val checklistVm: ChecklistViewModel by sharedViewModel()
    private val checklistContentAdapter: ChecklistContentRVAdapter = ChecklistContentRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WriteInitFragment", "onCreate")
    }

    private fun getTotalPostCnt() {
        CoroutineScope(Dispatchers.Main).launch {
            postVm.getTotalPostCnt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("WriteInitFragment", "onCreateView")

        binding = FragmentWriteInitBinding.inflate(layoutInflater, container, false)

        initAdapter()
        observe()

        //햄버거 메뉴 클릭 리스너 -> SettingActivity 화면 이동
        binding.hamburgerIb.setOnClickListener {
            val intent: Intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        //날짜 표시하기
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("LLLL dd.", Locale.ENGLISH)
        binding.dateTv.text = "The record for ${sdf.format(calendar.time)}"

        //기록 쓰러가기 클릭 리스너 -> RecordFragment 로 이동
        binding.goPostView.setOnClickListener {
            (requireParentFragment() as WriteFrameFragment).moveToRecord(false)
        }

        //체크리스트의 arrow_down 이미지뷰 클릭 리스너
        binding.arrowDownIv.setOnClickListener {
            //체크리스트 목록 뷰가 열려 있으면 -> 닫기
            if (binding.checklistHiddenRv.visibility == View.VISIBLE) {
                //체크리스트 고정뷰의 모든 코너를 둥글게
                binding.checklistView.setBackgroundResource(R.drawable.grey13_round_view)
                //애니메이션
                TransitionManager.beginDelayedTransition(
                    binding.baseCardView,
                    AutoTransition()
                )
                binding.checklistHiddenRv.visibility = View.GONE
            } else {    //체크리스트 목록 뷰가 닫혀 있으면 -> 열기
                //체크리스트 고정뷰의 위 모서리 코너를 둥글게
                binding.checklistView.setBackgroundResource(R.drawable.write_init_fragment_grey13_top_round_view)
                //애니메이션
                TransitionManager.beginDelayedTransition(
                    binding.baseCardView,
                    AutoTransition()
                )
                binding.checklistHiddenRv.visibility = View.VISIBLE
            }
        }

        //체크리스트 설정 이미지 클릭 리스너 -> ChecklistManagementActivity 화면으로 이동
        binding.checklistSettingIv.setOnClickListener {
            (requireActivity() as MainActivity).goChecklistManagement()
        }

        //프로필 관리 화면으로 이동하는 이미지 클릭 리스너 -> MyInfoActivity 화면으로 이동
        binding.goMyInfoIv.setOnClickListener {
            (requireActivity() as MainActivity).goMyInfo()
        }

        //독서 목표 관리 화면으로 이동하는 이미지 클릭 리스너 -> GoalManagementActivity 화면으로 이동
        binding.readingGoalNextIv.setOnClickListener {
            (requireActivity() as MainActivity).goGoalManagement()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("WriteInitFragment", "onResume")

        getUser()
        getTotalPostCnt()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("WriteInitFragment", "onDetach")
    }

    private fun initAdapter() {
        checklistContentAdapter.setMyChecklistContentRVAdapterListener(object :
            ChecklistContentRVAdapter.MyChecklistContentRVAdapterListener {
            override fun addChecklist(position: Int, content: String) {
                createChecklist(position, content)
            }

            override fun updateChecklist(position: Int, toDoId: Long, content: String) {
                val checklist =
                    CheckListModel(toDoId = toDoId, content = content, isComplete = false)
                modifyChecklist(position, checklist)
            }

            override fun completeChecklist(position: Int, checklist: CheckListModel) {
                checklist.isComplete = true
                modifyChecklist(position, checklist)
            }

        })

        //체크리스트 리사이클러뷰 어댑터 설정
        binding.checklistHiddenRv.setHasFixedSize(true)
        binding.checklistHiddenRv.adapter = checklistContentAdapter
    }

    private fun createChecklist(position: Int, content: String) {

        CoroutineScope(Dispatchers.Main).launch {
            val checklist = CheckListModel(content = content, isComplete = false)
            checklistVm.createChecklist(checklist)
//            checklistContentAdapter.notifyItemRangeChanged(position, 1)
        }
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.Main).launch {
            mainVm.getUser()
        }
    }

    private fun modifyChecklist(position: Int, checklist: CheckListModel) {
        CoroutineScope(Dispatchers.Main).launch {
            checklistVm.updateChecklist(position, checklist)

            if (checklist.isComplete) {
                checklistContentAdapter.removeChecklist(position)
            }
        }
    }

    private fun setUI(user: UserModel) {
        binding.writeInitTitleTv.text = "${user.nickname}님,\n오늘도 힘차게 기록해봅시다."

        if (user.goal == "") {
            binding.readingGoalTv.text = "목표를 설정해주세요."
        } else {
            binding.readingGoalTv.text = user.goal
        }
    }

    private fun setTotalPostCntUI(it: Int) {
        binding.totalPagesTv.text = "total $it pages"
    }

    private fun observe() {
        checklistVm.unCompletedChecklists.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("WriteInitFragment", "checklists observe!! -> $it")

            checklistContentAdapter.setChecklist(it)
            checklistContentAdapter.notifyDataSetChanged()

        })

        mainVm.user.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("WriteInitFragment", "user observe!! -> $it")
            setUI(it)
        })

        postVm.totalCnt.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("WriteInitFragment", "totalCnt observe!! -> $it")
            setTotalPostCntUI(it)
        })
    }

}