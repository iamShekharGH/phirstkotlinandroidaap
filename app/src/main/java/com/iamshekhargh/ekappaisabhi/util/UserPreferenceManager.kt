package com.iamshekhargh.ekappaisabhi.util

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by <<-- iamShekharGH -->>
 * on 01 April 2021
 * at 10:44 PM.
 */

private const val TAG = "UserPreferenceManager"

data class FilterPrefs(val sortOrder: SortOrder, val hideCompleted: Boolean)
enum class SortOrder { BY_DATE, BY_NAME }

@Singleton
class UserPreferenceManager @Inject constructor(@ApplicationContext c: Context) {

    private val datastore = c.createDataStore("user_preferences")

    private object PrefKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completedk")
    }


    val prefFlow = datastore.data
        .catch { e ->
            if (e is IOException) {
                Log.i(TAG, ": Error reading preferences $e")
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { pref ->
            val sortOrder = SortOrder.valueOf(pref[PrefKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name)
            val hideCompleted = pref[PrefKeys.HIDE_COMPLETED] ?: false

            FilterPrefs(sortOrder, hideCompleted)
        }

    suspend fun updateSortOrder(s: SortOrder) {
        datastore.edit { p ->
            p[PrefKeys.SORT_ORDER] = s.name
        }
    }

    suspend fun updateHideCompleted(hc: Boolean) {
        datastore.edit { p ->
            p[PrefKeys.HIDE_COMPLETED] = hc
        }
    }
}

//    val datastore = c.createDataStore("user_preferences")
//    val datastore = c.applicationContext.dataStoreFile("user_preferences")
