package com.iamshekhargh.ekappaisabhi.ui

import androidx.lifecycle.*
import com.iamshekhargh.ekappaisabhi.ADD_TASK_RESULT_OK
import com.iamshekhargh.ekappaisabhi.EDIT_TASK_RESULT_OK
import com.iamshekhargh.ekappaisabhi.models.Task
import com.iamshekhargh.ekappaisabhi.roomfiles.TaskDao
import com.iamshekhargh.ekappaisabhi.util.SortOrder
import com.iamshekhargh.ekappaisabhi.util.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by <<-- iamShekharGH -->>
 * on 31 March 2021
 * at 4:26 PM.
 */
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val upm: UserPreferenceManager,
    private val state: SavedStateHandle
) : ViewModel() {

    //    val searchQuery = MutableStateFlow("")
    val searchQuery = state.getLiveData("searchQuery", "")


    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)
    val preferenceFlow = upm.prefFlow
//
//    private val taskFlow = searchQuery.flatMapLatest {
//        taskDao.getCuratedTask(it, false)
//    }

    private val taskEventChannel = Channel<TasksEvent>()
    val tasksEvent = taskEventChannel.receiveAsFlow()

    private val tf =
        combine(searchQuery.asFlow(), sortOrder, hideCompleted) { query, order, completed ->
            Triple(query, order, completed)
        }.flatMapLatest { (query, order, completed) ->
            taskDao.getTask(query, order, completed)
        }

    private val taskFlow = combine(searchQuery.asFlow(), preferenceFlow) { query, prefFlow ->
        Pair(query, prefFlow)
    }.flatMapLatest { (query, prefFlow) ->
        taskDao.getTask(query, prefFlow.sortOrder, prefFlow.hideCompleted)
    }
    val tasks = taskFlow.asLiveData()

    fun onSortOrderSelected(s: SortOrder) = viewModelScope.launch {
        upm.updateSortOrder(s)
    }

    fun onHideCompleted(b: Boolean) = viewModelScope.launch {
        upm.updateHideCompleted(b)
    }

    fun onTaskSelected(t: Task) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.NavigateToEditTaskScreen(t))
    }

    fun onTaskChecked(t: Task, checked: Boolean) = viewModelScope.launch {
        taskDao.update(t.copy(completed = checked))
    }

    fun onTaskSwiped(t: Task) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(t))
        taskDao.delete(t)
    }

    fun onUndoDelete(t: Task) = viewModelScope.launch {
        taskDao.insert(t)
    }

    fun onAddNewTaskClicked() = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun addEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskResultMessage("Task Added")
            EDIT_TASK_RESULT_OK -> showTaskResultMessage("Task Changed")

        }
    }

    private fun showTaskResultMessage(s: String) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.ShowTaskResultMessage(s))
    }

    fun onDeleteAllCompletedTasks() = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.NavigateToDeleteAllCompleted)
    }


    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val t: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val t: Task) : TasksEvent()
        data class ShowTaskResultMessage(val s: String) : TasksEvent()
        object NavigateToDeleteAllCompleted : TasksEvent()
    }
}

