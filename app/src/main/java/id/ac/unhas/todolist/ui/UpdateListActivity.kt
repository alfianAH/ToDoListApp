package id.ac.unhas.todolist.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import id.ac.unhas.todolist.R
import id.ac.unhas.todolist.db.todolist.ToDoList
import java.text.SimpleDateFormat
import java.util.*

class UpdateListActivity : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextNote: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: Button
    private lateinit var toDoListViewModel: ToDoListViewModel
    private lateinit var toDoList: ToDoList
    private var calendar = Calendar.getInstance()

    companion object{
        const val EXTRA_TITLE_UPDATE = "TITLE"
        const val EXTRA_DATE_UPDATE = "date-month-year"
        const val EXTRA_TIME_UPDATE = "hour:minutes"
        const val EXTRA_NOTE_UPDATE = "NOTE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_list)
        if(supportActionBar != null){
            supportActionBar?.title = "Update a Task"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Back Button

        editTextTitle = findViewById(R.id.title_content)
        editTextDate = findViewById(R.id.due_date_content)
        editTextTime = findViewById(R.id.due_time_content)
        editTextNote = findViewById(R.id.note_content)
        btnUpdate = findViewById(R.id.btn_update)
        btnCancel = findViewById(R.id.btn_cancel)
        toDoListViewModel = ViewModelProvider(this).get(ToDoListViewModel::class.java)

        getExtra()

        editTextDate.setOnClickListener{
            setDueDate()
        }

        editTextTime.setOnClickListener {
            setDueTime()
        }

        btnUpdate.setOnClickListener{
            updateList(toDoList)
        }

        btnCancel.setOnClickListener{
            finish()
        }
    }

    // Back Button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun getExtra(){
        toDoList = intent.getParcelableExtra("EXTRA_LIST")!!
        editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE_UPDATE))
        editTextDate.setText(intent.getStringExtra(EXTRA_DATE_UPDATE))
        editTextTime.setText(intent.getStringExtra(EXTRA_TIME_UPDATE))
        editTextNote.setText(intent.getStringExtra(EXTRA_NOTE_UPDATE))
    }

    private fun setDueDate(){
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        // Date picker dialog
        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener{
                    view, year, month, date ->
                editTextDate.setText("" + date + "-" + (month+1) + "-" + year)
            }, year, month, date)

        datePicker.show()
    }

    private fun setDueTime(){
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            editTextTime.setText(SimpleDateFormat("HH:mm").format(calendar.time))
        }

        TimePickerDialog(this, timeSetListener, hour, minute, true).show()
    }

    private fun updateList(toDoList: ToDoList){
        toDoList.title = editTextTitle.text.toString().trim()
        toDoList.dueDate = editTextDate.text.toString().trim()
        toDoList.dueHour = editTextTime.text.toString().trim()
        toDoList.note = editTextNote.text.toString().trim()

        toDoListViewModel.updateList(toDoList)

        finish()
    }
}
