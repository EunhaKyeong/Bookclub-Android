package com.mangpo.ourpage.view.main.home

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.ActivityChecklistManagementBinding
import com.mangpo.ourpage.utils.isNetworkAvailable
import com.mangpo.ourpage.view.BaseActivity
import com.mangpo.ourpage.view.adpater.ChecklistHeaderRVAdapter
import com.mangpo.ourpage.viewmodel.TodoViewModel

class ChecklistManagementActivity : BaseActivity<ActivityChecklistManagementBinding>(ActivityChecklistManagementBinding::inflate) {
    private val todoVm: TodoViewModel by viewModels<TodoViewModel>()

    private lateinit var checklistHeaderRVAdapter: ChecklistHeaderRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        observe()

        todoVm.getTodos()

        binding.checklistManagementTb.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observe() {
        todoVm.completeTodos.observe(this, Observer {
            Log.d("ChecklistManagementActivity", "completeTodos Observe! completeTodos -> $it")

            if (it.isEmpty())
                binding.checklistManagementRv.visibility = View.GONE
            else {
                binding.checklistManagementRv.visibility = View.VISIBLE
                checklistHeaderRVAdapter.setChecklist(it)
            }
        })

        todoVm.deleteTodoCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()
            Log.d("ChecklistManagementActivity", "deleteTodoCode Observe! deleteTodoCode -> $code")

            if (code!=null) {
                when (code) {
                    204 -> todoVm.getTodos()
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })
    }

    private fun initAdapter() {
        checklistHeaderRVAdapter = ChecklistHeaderRVAdapter()
        checklistHeaderRVAdapter.setMyClickListener(object : ChecklistHeaderRVAdapter.MyClickListener {
            override fun delete(toDoId: Int) {
                if (isNetworkAvailable(applicationContext))
                    todoVm.deleteTodo(toDoId)
                else
                    showSnackBar(getString(R.string.error_check_network))
            }
        })
        binding.checklistManagementRv.adapter = checklistHeaderRVAdapter
    }
}