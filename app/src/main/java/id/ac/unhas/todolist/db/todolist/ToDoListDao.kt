package id.ac.unhas.todolist.db.todolist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoListDao {
    @Query("SELECT * FROM todolist")
    fun getToDoList(): LiveData<List<ToDoList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTitle(title: ToDoList)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: ToDoList)

    @Delete
    suspend fun deleteList(list: ToDoList)

    @Update
    suspend fun updateList(list: ToDoList)
}