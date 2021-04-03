package com.iamshekhargh.ekappaisabhi.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iamshekhargh.ekappaisabhi.R
import com.iamshekhargh.ekappaisabhi.databinding.FragmentPhirstBinding
import com.iamshekhargh.ekappaisabhi.models.Task
import com.iamshekhargh.ekappaisabhi.onTextEntered
import com.iamshekhargh.ekappaisabhi.util.SortOrder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


//FragmentTaskList


@AndroidEntryPoint
class PhirstFrag : Fragment(R.layout.fragment_phirst), View.OnClickListener,
    TasksRVAdapter.OnItemClickListener {
    private val TAG = "PhirstFrag"
    private val viewModel: TasksViewModel by viewModels()
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentPhirstBinding.bind(view)
        val taskAdapter = TasksRVAdapter(this)

        b.apply {
            phirstfragRecyclerviewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val t = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(t)
                }

            }).attachToRecyclerView(phirstfragRecyclerviewTasks)

            phirstfragRecyclerviewFab.setOnClickListener(this@PhirstFrag)
        }

        setFragmentResultListener("add_edit_request_key") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.addEditResult(result)

        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "text", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDelete(event.t)
                            }.show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        val action = PhirstFragDirections.actionPhirstFragToAddEditTaskFragment(
                            null,
                            "New Task"
                        )
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val action =
                            PhirstFragDirections.actionPhirstFragToAddEditTaskFragment(
                                event.t,
                                "Edit Task"
                            )
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.ShowTaskResultMessage -> {
                        Snackbar.make(requireView(), event.s, Snackbar.LENGTH_LONG).show()
                    }
                    TasksViewModel.TasksEvent.NavigateToDeleteAllCompleted -> {
                        val action =
                            PhirstFragDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                }

            }
        }
        setHasOptionsMenu(true)
    }

    override fun onClick(v: View?) {
        viewModel.onAddNewTaskClicked()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar_menu_xml, menu)
        val sitem = menu.findItem(R.id.menuitem_search)
        searchView = sitem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            sitem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }
        searchView.onTextEntered {
            Log.i(TAG, "onCreateOptionsMenu: $it")
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.menuitem_hide_completed_tasks).isChecked =
                viewModel.preferenceFlow.first().hideCompleted
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.manuitem_delete_all -> {
                viewModel.onDeleteAllCompletedTasks()
                true
            }
            R.id.menuitem_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
//                viewModel.hideCompleted.value = item.isChecked
                viewModel.onHideCompleted(item.isChecked)
                true
            }
            R.id.menuitem_sort_by_date_created -> {
//                viewModel.sortOrder.value = SortOrder.BY_DATE
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.menuitem_sort_by_name -> {
//                viewModel.sortOrder.value = SortOrder.BY_NAME
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onItemClicked(t: Task) {
        viewModel.onTaskSelected(t)
    }

    override fun onCheckBoxClicked(t: Task, isChecked: Boolean) {
        viewModel.onTaskChecked(t, isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}
