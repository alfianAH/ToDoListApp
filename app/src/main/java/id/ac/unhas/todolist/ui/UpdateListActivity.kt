package id.ac.unhas.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import id.ac.unhas.todolist.R

class UpdateListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_list)
        if(supportActionBar != null){
            supportActionBar?.title = "Update a Task"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Back Button
    }

    // Back Button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}
