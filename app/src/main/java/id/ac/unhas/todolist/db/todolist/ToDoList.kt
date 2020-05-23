package id.ac.unhas.todolist.db.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "todolist")
data class ToDoList (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "created_date")
    val createdDate: Date? = null,

    @ColumnInfo(name = "updated_date")
    val updatedDate: Date? = null,

    @ColumnInfo(name = "due_date")
    val dueDate: Date? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "note")
    val note: String
)