package id.ac.unhas.todolist.db.todolist

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "todolist")
data class ToDoList (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "created_date")
    var createdDate: String? = null,

    @ColumnInfo(name = "updated_date")
    var updatedDate: String? = null,

    @ColumnInfo(name = "due_date")
    var dueDate: String? = null,

    @ColumnInfo(name = "due_hour")
    var dueHour: String? = null,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "note")
    var note: String,

    @ColumnInfo(name = "is_finished")
    var isFinished: Boolean? = null
) : Parcelable