package com.iamshekhargh.ekappaisabhi.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamshekhargh.ekappaisabhi.ADD_TASK_RESULT_OK
import com.iamshekhargh.ekappaisabhi.EDIT_TASK_RESULT_OK
import com.iamshekhargh.ekappaisabhi.models.Task
import com.iamshekhargh.ekappaisabhi.roomfiles.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by <<-- iamShekharGH -->>
 * on 02 April 2021
 * at 8:29 PM.
 */

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {
    val t = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: t?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportant = state.get<Boolean>("taskImportant") ?: t?.important ?: false
        set(value) {
            field = value
            state.set("taskImportant", value)
        }
    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClicked() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Task cannot be empty")
            return
        }
        if (t != null) {
            val updateTask = t.copy(name = taskName, important = taskImportant)
            updateTask(updateTask)
        } else {
            val newTask =
                Task(name = taskName, description = "dalenge baadme", important = taskImportant)
            createNewTask(newTask)
        }
    }

    private fun showInvalidInputMessage(s: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(s))
    }

    private fun createNewTask(newTask: Task) = viewModelScope.launch {
        taskDao.insert(newTask)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(updateTask: Task) = viewModelScope.launch {
        taskDao.update(updateTask)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()

    }

}