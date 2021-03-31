package com.iamshekhargh.ekappaisabhi.dependencyInjection

import android.app.Application
import androidx.room.Room
import com.iamshekhargh.ekappaisabhi.roomfiles.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Created by <<-- iamShekharGH -->>
 * on 31 March 2021
 * at 3:49 PM.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDb(app: Application, callback: TaskDatabase.Kallbak) =
        Room.databaseBuilder(app, TaskDatabase::class.java, "taskDB")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.getTaskDao()

    @Provides
    @Singleton
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob())


}