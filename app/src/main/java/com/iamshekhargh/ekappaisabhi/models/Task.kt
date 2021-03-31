package com.iamshekhargh.ekappaisabhi.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

/**
 * Created by <<-- iamShekharGH -->>
 * on 30 March 2021
 * at 9:41 PM.
 */
@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val name: String,
    val description: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0

) : Parcelable {
    val formattedDate: String get() = DateFormat.getDateTimeInstance().format(created)
}