package com.iamshekhargh.ekappaisabhi.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iamshekhargh.ekappaisabhi.R
import com.iamshekhargh.ekappaisabhi.databinding.FragmentPhirstBinding
import com.iamshekhargh.ekappaisabhi.onTextEntered
import dagger.hilt.android.AndroidEntryPoint


//FragmentTaskList


@AndroidEntryPoint
class PhirstFrag : Fragment(R.layout.fragment_phirst), View.OnClickListener {
    private val TAG = "PhirstFrag"
    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentPhirstBinding.bind(view)
        val taskAdapter = TasksRVAdapter()

        b.apply {
            phirstfragRecyclerviewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            phirstfragRecyclerviewFab.setOnClickListener(this@PhirstFrag)
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
        setHasOptionsMenu(true)
    }

    override fun onClick(v: View?) {
        Toast.makeText(requireContext(), "FAB Clicked", Toast.LENGTH_SHORT).show()
        v?.let { Snackbar.make(it, "FAB Clicked", Snackbar.LENGTH_LONG).show() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar_menu_xml, menu)
        val sitem = menu.findItem(R.id.menuitem_search)
        val sv = sitem.actionView as SearchView
        sv.onTextEntered {
            Log.i(TAG, "onCreateOptionsMenu: $it")
            viewModel.searchQuery.value = it
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.manuitem_delete_all -> {
                true
            }
            R.id.menuitem_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.hideCompleted.value = item.isChecked
                true
            }
            R.id.menuitem_sort_by_date_created -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.menuitem_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
