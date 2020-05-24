package id.ac.unhas.todolist.db.todolist

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface ToDoListDao {
    @Query("SELECT * FROM todolist")
    fun getToDoList(): LiveData<List<ToDoList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(list: ToDoList)

    @Delete
    suspend fun deleteList(list: ToDoList)

    @Update
    suspend fun updateList(list: ToDoList)
}