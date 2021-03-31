package com.iamshekhargh.ekappaisabhi.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iamshekhargh.ekappaisabhi.R
import com.iamshekhargh.ekappaisabhi.databinding.FragmentPhirstBinding
import dagger.hilt.android.AndroidEntryPoint


//FragmentTaskList


@AndroidEntryPoint
class PhirstFrag : Fragment(R.layout.fragment_phirst), View.OnClickListener {
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
    }

    override fun onClick(v: View?) {
        Toast.makeText(requireContext(), "FAB Clicked", Toast.LENGTH_SHORT).show()
        v?.let { Snackbar.make(it, "FAB Clicked", Snackbar.LENGTH_LONG).show() }
    }

}
