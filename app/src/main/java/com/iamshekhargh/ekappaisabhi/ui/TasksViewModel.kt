package com.iamshekhargh.ekappaisabhi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iamshekhargh.ekappaisabhi.roomfiles.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by <<-- iamShekharGH -->>
 * on 31 March 2021
 * at 4:26 PM.
 */
@HiltViewModel
class TasksViewModel @Inject constructor(val taskDao: TaskDao) : ViewModel() {
    val tasks = taskDao.getAllTask().asLiveData()
}