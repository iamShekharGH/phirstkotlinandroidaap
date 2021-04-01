package com.iamshekhargh.ekappaisabhi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iamshekhargh.ekappaisabhi.roomfiles.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * Created by <<-- iamShekharGH -->>
 * on 31 March 2021
 * at 4:26 PM.
 */
@HiltViewModel
class TasksViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)
//
//    private val taskFlow = searchQuery.flatMapLatest {
//        taskDao.getCuratedTask(it, false)
//    }

    private val tf = combine(searchQuery, sortOrder, hideCompleted) { query, order, completed ->
        Triple(query, order, completed)
    }.flatMapLatest { (query, order, completed) ->
        taskDao.getTask(query, order, completed)
    }
    val tasks = tf.asLiveData()
}

enum class SortOrder {
    BY_DATE, BY_NAME
}