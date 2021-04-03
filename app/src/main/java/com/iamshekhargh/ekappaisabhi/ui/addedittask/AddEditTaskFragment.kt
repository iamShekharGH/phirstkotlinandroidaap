package com.iamshekhargh.ekappaisabhi.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.iamshekhargh.ekappaisabhi.R
import com.iamshekhargh.ekappaisabhi.databinding.FragmentAddEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by <<-- iamShekharGH -->>
 * on 02 April 2021
 * at 8:22 PM.
 */
@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentAddEditTaskBinding.bind(view)
        b.apply {
            addeditfragEtTask.setText(viewModel.taskName)
            addeditfragCbImportant.isChecked = viewModel.taskImportant
            addeditfragCbImportant.jumpDrawablesToCurrentState() // to skip checkbox animation
            addeditfragTvTime.isVisible = viewModel.t != null
            addeditfragTvTime.text = "Created : ${viewModel.t?.formattedDate}"

            addeditfragEtTask.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            addeditfragCbImportant.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.taskImportant = isChecked
            }

            addeditfragFab.setOnClickListener { viewModel.onSaveClicked() }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addEditTaskEvent.collect { e ->
                when (e) {
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        b.addeditfragEtTask.clearFocus()
                        setFragmentResult(
                            "add_edit_request_key",
                            bundleOf("add_edit_result" to e.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), e.msg, Snackbar.LENGTH_LONG).show()
                    }
                }

            }
        }
    }
}