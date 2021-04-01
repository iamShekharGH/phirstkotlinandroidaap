package com.iamshekhargh.ekappaisabhi.roomfiles

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iamshekhargh.ekappaisabhi.models.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by <<-- iamShekharGH -->>
 * on 31 March 2021
 * at 3:41 PM.
 */
@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao

    class Kallbak @Inject constructor(
        private val database: Provider<TaskDatabase>,
        private val coroutineScope: CoroutineScope
    ) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().getTaskDao()
            // here we can add dummy data.
            coroutineScope.launch {
                dao.insert(
                    Task(
                        "Phinish Dis",
                        "following coding in flow tutorialz",
                        important = true
                    )
                )
                dao.insert(Task("watch Black clover", "uda vo hawa me", important = false))
                dao.insert(Task("make omelet", "with fresh eggs", important = true))
                dao.insert(Task("watch One Piece", "latest episode", important = true))
                dao.insert(Task("have a life", "or dont", important = false))
                dao.insert(Task("order pizza", "from dominos", important = true, completed = true))
            }
        }
    }
}