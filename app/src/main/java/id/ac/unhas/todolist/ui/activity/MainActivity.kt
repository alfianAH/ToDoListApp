package id.ac.unhas.todolist.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
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

        // Set layout Manager of Recycler View
        listRV.layoutManager = LinearLayoutManager(this)
        toDoListAdapter =
            ToDoListAdapter(this) { toDoList, i ->
                showAlertMenu(toDoList)
            }
        // Set adapter of Recycler View
        listRV.adapter = toDoListAdapter

        toDoListViewModel = ViewModelProvider(this).get(ToDoListViewModel::class.java)

        // Get all to do lists
        toDoListViewModel.getLists()?.observe(this, Observer {
            toDoListAdapter.setLists(it)
        })

        // Add List
        floatingActionButton.setOnClickListener{
            addList()
        }
    }

    // Create menu layout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        searchList(menu) // Search bar
        return super.onCreateOptionsMenu(menu)
    }

    // When item in menu is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_list -> sortList()
        }
        return super.onOptionsItemSelected(item)
    }

    // Add List: Move to AddListActivity
    private fun addList(){
        val addIntent = Intent(this, AddListActivity::class.java)
        startActivity(addIntent)
    }

    // Search Bar processing
    private fun searchList(menu: Menu?){
        // Get the search icon
        val item = menu?.findItem(R.id.search_list)

        val searchView = item?.actionView as androidx.appcompat.widget.SearchView?
        searchView?.isSubmitButtonEnabled = true

        // Process input on search bar
        searchView?.setOnQueryTextListener(
            object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null){
                        getItemsFromDb(query) // Get items from db
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText != null){
                        getItemsFromDb(newText) // Get items from db
                    }
                    return true
                }
            }
        )
    }

    // Get items from db
    private fun getItemsFromDb(searchText: String){
        var searchText = searchText
        searchText = "%$searchText%" // % is for query

        // Get all to do lists with searchText
        toDoListViewModel.searchResult(searchText)?.observe(this, Observer {
            toDoListAdapter.setLists(it)
        })
    }

    // Sort list by due date or created date
    // ascending or descending
    private fun sortList(){
        val items = arrayOf("Due Date", "Created Date")

        val builder = AlertDialog.Builder(this)
        val alert = AlertDialog.Builder(this)
        builder.setTitle("Sort by ...")
            .setItems(items){dialog, which ->
            when(which){
                0 -> {
                    alert.setTitle(items[which])
                        .setPositiveButton("Ascending"){dialog, _ ->
                            // Get list sorted by due date ascending
                            toDoListViewModel.getLists()?.observe(this, Observer {
                                toDoListAdapter.setLists(it)
                            })
                            dialog.dismiss()
                        }
                        .setNegativeButton("Descending"){dialog, _ ->
                            // Get list sorted by due date descending
                            toDoListViewModel.sortByDueDateDescending()?.observe(this, Observer {
                                toDoListAdapter.setLists(it)
                            })
                            dialog.dismiss()
                        }
                        .show()
                }
                1 -> {
                    alert.setTitle(items[which])
                        .setPositiveButton("Ascending"){dialog, _ ->
                            // Get list sorted by created date ascending
                            toDoListViewModel.sortByCreatedDateAscending()?.observe(this, Observer {
                                toDoListAdapter.setLists(it)
                            })
                            dialog.dismiss()
                        }
                        .setNegativeButton("Descending"){dialog, _ ->
                            // Get list sorted by created date descending
                            toDoListViewModel.sortByCreatedDateDescending()?.observe(this, Observer {
                                toDoListAdapter.setLists(it)
                            })
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
        builder.show()
    }

    // Show alert menu when list is clicked
    private fun showAlertMenu(toDoList: ToDoList){
        val items = arrayOf("Details", "Edit", "Delete")

        val builder = AlertDialog.Builder(this)
        val alert = AlertDialog.Builder(this)
        builder.setItems(items){ dialog, which ->
            when(which){
                0 -> {
                    listDetails(alert, toDoList)
                }
                1 -> {
                    updateList(toDoList)
                }
                2 -> {
                    // Delete task
                    alert.setTitle("Delete task?")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes"){dialog, _ ->
                            toDoListViewModel.deleteList(toDoList)
                            dialog.dismiss()
                        }
                        .setNegativeButton("No"){dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
        builder.show()
    }

    // Show detail of the list
    private fun listDetails(alert: AlertDialog.Builder, toDoList: ToDoList){
        val inflater = layoutInflater
        // Get the layout
        val dialogView = inflater.inflate(R.layout.item_details, null)

        val title: TextView = dialogView.findViewById(R.id.title)
        val createdDate: TextView = dialogView.findViewById(R.id.created_date_content)
        val dueTime: TextView = dialogView.findViewById(R.id.due_time_content)
        val note: TextView = dialogView.findViewById(R.id.note_content)

        // Set the text
        title.text = toDoList.title
        createdDate.text = toDoList.strCreatedDate
        dueTime.text = "${toDoList.strDueDate}, ${toDoList.strDueHour}"
        note.text = toDoList.note

        // Show Alert Dialog
        alert.setView(dialogView)
            .setNeutralButton("OK"){dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Update List by sending data from MainActivity to UpdateListActivity
    private fun updateList(toDoList: ToDoList){
        val addIntent = Intent(this, UpdateListActivity::class.java)
            .putExtra("EXTRA_LIST", toDoList)
            .putExtra(UpdateListActivity.EXTRA_TITLE_UPDATE, toDoList.title)
            .putExtra(UpdateListActivity.EXTRA_DATE_UPDATE, toDoList.strDueDate)
            .putExtra(UpdateListActivity.EXTRA_TIME_UPDATE, toDoList.strDueHour)
            .putExtra(UpdateListActivity.EXTRA_NOTE_UPDATE, toDoList.note)
            .putExtra(UpdateListActivity.EXTRA_IS_FINISHED_UPDATE, toDoList.isFinished)

        startActivity(addIntent)
    }
}
