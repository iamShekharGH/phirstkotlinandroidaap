package com.iamshekhargh.ekappaisabhi.ui

import androidx.lifecycle.ViewModel
import com.iamshekhargh.ekappaisabhi.roomfiles.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by <<-- iamShekharGH -->>
 * on 03 April 2021
 * at 5:07 PM.
 */
@HiltViewModel
class DeleteALlCompletedViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val scope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick() = scope.launch {
        taskDao.deleteAllCompletedTask()
    }
}