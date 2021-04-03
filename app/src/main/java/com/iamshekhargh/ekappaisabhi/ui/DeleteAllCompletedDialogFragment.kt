package com.iamshekhargh.ekappaisabhi.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by <<-- iamShekharGH -->>
 * on 03 April 2021
 * at 4:48 PM.
 */
@AndroidEntryPoint
class DeleteAllCompletedDialogFragment : DialogFragment() {

    private val viewModel: DeleteALlCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Do you really wanna delete all tasks?")
            .setNegativeButton("CANCEL", null)
            .setPositiveButton("OK") { _, _ ->
                viewModel.onConfirmClick()
            }
            .create()

}