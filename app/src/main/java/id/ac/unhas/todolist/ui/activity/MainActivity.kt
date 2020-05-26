package id.ac.unhas.todolist.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.ac.unhas.todolist.R
import id.ac.unhas.todolist.db.todolist.ToDoList
import id.ac.unhas.todolist.ui.adapter.ToDoListAdapter
import id.ac.unhas.todolist.ui.view_model.ToDoListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var toDoListViewModel: ToDoListViewModel
    private lateinit var toDoListAdapter: ToDoListAdapter
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton = findViewById(R.id.fab)

        listRV.layoutManager = LinearLayoutManager(this)
        toDoListAdapter =
            ToDoListAdapter(this) { toDoList, i ->
                showAlertMenu(toDoList)
            }
        listRV.adapter = toDoListAdapter

        toDoListViewModel = ViewModelProvider(this).get(ToDoListViewModel::class.java)

        toDoListViewModel.getLists()?.observe(this, Observer {
            toDoListAdapter.setLists(it)
        })

        floatingActionButton.setOnClickListener{
            addList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        searchList(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_list -> sortList()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addList(){
        val addIntent = Intent(this, AddListActivity::class.java)
        startActivity(addIntent)
    }

    private fun searchList(menu: Menu?){
        val item = menu?.findItem(R.id.search_list)

        val searchView = item?.actionView as androidx.appcompat.widget.SearchView?
        searchView?.isSubmitButtonEnabled = true

        searchView?.setOnQueryTextListener(
            object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null){
                        getItemsFromDb(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText != null){
                        getItemsFromDb(newText)
                    }
                    return true
                }
            }
        )
    }

    private fun getItemsFromDb(searchText: String){
        var searchText = searchText
        searchText = "%$searchText%"

        toDoListViewModel.searchResult(searchText)?.observe(this, Observer {
            toDoListAdapter.setLists(it)
        })
    }

    private fun sortList(){

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
                    val alert = AlertDialog.Builder(this)
                    alert.setTitle("Delete task?")
                    alert.setMessage("Are you sure?")
                    alert.setPositiveButton("Yes"){dialog, _ ->
                        toDoListViewModel.deleteList(toDoList)
                        dialog.dismiss()
                    }

                    alert.setNegativeButton("No"){dialog, _ ->
                        dialog.dismiss()
                    }

                    alert.show()
                }
            }
        }
        builder.show()
    }

    private fun updateList(toDoList: ToDoList){
        val addIntent = Intent(this, UpdateListActivity::class.java)
            .putExtra("EXTRA_LIST", toDoList)
            .putExtra(UpdateListActivity.EXTRA_TITLE_UPDATE, toDoList.title)
            .putExtra(UpdateListActivity.EXTRA_DATE_UPDATE, toDoList.dueDate)
            .putExtra(UpdateListActivity.EXTRA_TIME_UPDATE, toDoList.dueHour)
            .putExtra(UpdateListActivity.EXTRA_NOTE_UPDATE, toDoList.note)

        startActivity(addIntent)
    }
}
