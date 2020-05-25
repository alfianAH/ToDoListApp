package id.ac.unhas.todolist.db.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todolist")
data class ToDoList (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "created_date")
    val createdDate: String? = null,

    @ColumnInfo(name = "updated_date")
    val updatedDate: String? = null,

    @ColumnInfo(name = "due_date")
    val dueDate: String? = null,

    @ColumnInfo(name = "due_hour")
    val dueHour: String? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "note")
    val note: String,

    @ColumnInfo(name = "is_finished")
    val isFinished: Boolean? = null
)