package id.ac.unhas.todolist.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.unhas.todolist.db.todolist.ToDoList
import id.ac.unhas.todolist.db.todolist.ToDoListRepo

class ToDoListViewModel (application: Application): AndroidViewModel(application){
    private var toDoListRepo = ToDoListRepo(application)
    private var lists: LiveData<List<ToDoList>>? = toDoListRepo.getLists()

    fun getLists(): LiveData<List<ToDoList>>?{
        return lists
    }

    fun insertList(list: ToDoList){
        toDoListRepo.insertList(list)
    }

    fun deleteList(list: ToDoList){
        toDoListRepo.deleteList(list)
    }

    fun updateList(list: ToDoList){
        toDoListRepo.updateList(list)
    }
}