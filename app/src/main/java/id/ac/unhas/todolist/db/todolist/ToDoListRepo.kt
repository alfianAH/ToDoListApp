package id.ac.unhas.todolist.db.todolist

import android.app.Application
import androidx.lifecycle.LiveData
import id.ac.unhas.todolist.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ToDoListRepo (application: Application){
    private val toDoListDao: ToDoListDao?
    private var lists: LiveData<List<ToDoList>>? = null

    init {
        val db = AppDatabase.getInstance(application.applicationContext)
        toDoListDao = db?.toDoListDao()
        lists = toDoListDao?.getToDoList()
    }

    fun getList(): LiveData<List<ToDoList>>?{
        return lists
    }

    fun insertTitle(title: ToDoList) = runBlocking {
        this.launch(Dispatchers.IO) {
            toDoListDao?.insertTitle(title)
        }
    }

    fun insertNote(note: ToDoList) = runBlocking {
        this.launch(Dispatchers.IO){
            toDoListDao?.insertNote(note)
        }
    }

    fun delete(list: ToDoList) = runBlocking{
        this.launch(Dispatchers.IO){
            toDoListDao?.deleteList(list)
        }
    }

    fun update(list: ToDoList) = runBlocking {
        this.launch(Dispatchers.IO) {
            toDoListDao?.updateList(list)
        }
    }

}