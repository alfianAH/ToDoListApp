package id.ac.unhas.todolist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.unhas.todolist.db.todolist.ToDoList
import id.ac.unhas.todolist.db.todolist.ToDoListRepo
import java.util.*

class ToDoListViewModel (application: Application): AndroidViewModel(application){
    private var toDoListRepo = ToDoListRepo(application)
    private var lists: LiveData<List<ToDoList>>? = toDoListRepo.getLists()

    fun getLists(): LiveData<List<ToDoList>>?{
        return lists
    }

    fun insertTitle(title: ToDoList){
        toDoListRepo.insertTitle(title)
    }

    fun insertNote(note: ToDoList){
        toDoListRepo.insertNote(note)
    }

    fun insertDueDate(dueDate: Date){
        toDoListRepo.insertDueDate(dueDate)
    }

    fun deleteList(list: ToDoList){
        toDoListRepo.delete(list)
    }

    fun updateList(list: ToDoList){
        toDoListRepo.update(list)
    }
}