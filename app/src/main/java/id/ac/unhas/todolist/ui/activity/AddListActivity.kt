package id.ac.unhas.todolist.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import id.ac.unhas.todolist.R
import id.ac.unhas.todolist.db.todolist.ToDoList
import id.ac.unhas.todolist.ui.view_model.ToDoListViewModel
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

class AddListActivity : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextNote: EditText
    private lateinit var editTextTime: EditText
    private lateinit var btnSave: Button
    private lateinit var toDoListViewModel: ToDoListViewModel
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_list)
        if(supportActionBar != null){
            supportActionBar?.title = "Add a Task"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Back Button

        editTextTitle = findViewById(R.id.title_content)
        editTextDate = findViewById(R.id.due_date_content)
        editTextNote = findViewById(R.id.note_content)
        editTextTime = findViewById(R.id.due_time_content)
        btnSave = findViewById(R.id.btn_save)
        toDoListViewModel = ViewModelProvider(this).get(ToDoListViewModel::class.java)

        editTextDate.setOnClickListener{
            setDueDate()
        }

        editTextTime.setOnClickListener {
            setDueTime()
        }

        btnSave.setOnClickListener{
            saveList()
        }
    }

    // Back Button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun setDueDate(){
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        // Date picker dialog
        val datePicker = DatePickerDialog(
            this@AddListActivity,
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

    private fun saveList(){
        val current = ZonedDateTime.now()
        val millis = current.toInstant().epochSecond

        val createdDate = millis.toInt()
        val title = editTextTitle.text.toString().trim()
        val dueDate = editTextDate.text.toString().trim()
        val dueHour = editTextTime.text.toString().trim()
        val note = editTextNote.text.toString().trim()

        toDoListViewModel.insertList(
            ToDoList(
                createdDate = createdDate,
                title = title,
                dueDate = dueDate,
                dueHour = dueHour,
                note = note
            )
        )

        finish()
    }
}
