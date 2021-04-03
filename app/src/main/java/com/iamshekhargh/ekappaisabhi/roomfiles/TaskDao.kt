package com.iamshekhargh.ekappaisabhi.roomfiles

import androidx.room.*
import com.iamshekhargh.ekappaisabhi.models.Task
import com.iamshekhargh.ekappaisabhi.util.SortOrder
import kotlinx.coroutines.flow.Flow

/**
 * Created by <<-- iamShekharGH -->>
 * on 31 March 2021
 * at 1:45 PM.
 */
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: Task)

    @Delete
    suspend fun delete(t: Task)

    @Update
    suspend fun update(t: Task)

    @Query("SELECT * FROM task_table")
    fun getAllTask(): Flow<List<Task>>

    fun getTask(q: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getTasksSortedByName(q, hideCompleted)
            SortOrder.BY_DATE -> getTasksSortedByDate(q, hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :q || '%' ORDER BY important DESC, name")
    fun getTasksSortedByName(q: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :q || '%' ORDER BY important DESC, created")
    fun getTasksSortedByDate(q: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteAllCompletedTask()

}