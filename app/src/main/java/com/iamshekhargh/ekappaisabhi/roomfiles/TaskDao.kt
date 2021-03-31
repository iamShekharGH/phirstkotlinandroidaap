package com.iamshekhargh.ekappaisabhi.roomfiles

import androidx.room.*
import com.iamshekhargh.ekappaisabhi.models.Task
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
}