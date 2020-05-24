package id.ac.unhas.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.unhas.todolist.db.todolist.ToDoList
import id.ac.unhas.todolist.ui.ToDoListAdapter
import id.ac.unhas.todolist.ui.ToDoListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var toDoListViewModel: ToDoListViewModel
    private lateinit var toDoListAdapter: ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listRV.layoutManager = LinearLayoutManager(this)
        toDoListAdapter = ToDoListAdapter(this){
            toDoList, i -> showAlertMenu(toDoList)
        }
        listRV.adapter = toDoListAdapter

        toDoListViewModel = ViewModelProvider(this).get(ToDoListViewModel::class.java)

        toDoListViewModel.getLists()?.observe(this, Observer {
            toDoListAdapter.setLists(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addMenu -> addList()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addList(){

    }

    private fun showAlertMenu(toDoList: ToDoList){
        val items = arrayOf("Edit", "Delete")

        val builder = AlertDialog.Builder(this)
        builder.setItems(items){ dialog, which ->
            when(which){
                0 -> {
                    updateList(toDoList)
                }
                1 -> {
                    toDoListViewModel.deleteList(toDoList)
                }
            }
        }
        builder.show()
    }

    private fun updateList(toDoList: ToDoList){

    }
}
