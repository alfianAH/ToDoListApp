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
import id.ac.unhas.todolist.ui.Converter
import id.ac.unhas.todolist.ui.view_model.ToDoListViewModel
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
        // Change the menu title
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
            setDueDate() // Date Picker Dialog
        }

        editTextTime.setOnClickListener {
            setDueTime() // Time Picker Dialog
        }

        btnSave.setOnClickListener{
            saveList() // save list
        }
    }

    // Back Button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    // Set DatePickerDialog to simplify the input
    private fun setDueDate(){
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        // Date picker dialog
        val dateListener = DatePickerDialog.OnDateSetListener{ view, year, month, date ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, date)
            editTextDate.setText(SimpleDateFormat("EEE, MMM dd, yyyy").format(calendar.time))
        }

        DatePickerDialog(this, dateListener, year, month, date).show()
    }

    // Set TimePickerDialog to simplify the input
    private fun setDueTime(){
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // TimePickerDialog
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            editTextTime.setText(SimpleDateFormat("HH:mm").format(calendar.time))
        }

        TimePickerDialog(this, timeSetListener, hour, minute, true).show()
    }

    // Save List when button is clicked
    private fun saveList(){
        // Get current time (UTC+8)
        val current = ZonedDateTime.now(ZoneId.of("+8"))
        val formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy, HH:mm:ss")

        val strCreatedDate = current.format(formatter) // string of created date
        val createdDate = Converter.dateToInt(current) // Integer of created date

        var dueDate: Int? = null
        var dueHour: Int? = null
        var strDueDate: String? = ""
        var strDueHour: String? = ""

        // Check if there is text in edit text date
        if(editTextDate.text.isNotEmpty()) {
            strDueDate = editTextDate.text.toString().trim()
            dueDate = Converter.stringDateToInt(strDueDate) // Convert it to int
        }

        // Check if there is text in edit text time
        if(editTextTime.text.isNotEmpty()) {
            strDueHour = editTextTime.text.toString().trim()
            dueHour = Converter.stringTimeToInt(strDueHour) // Convert it to int
        }

        val title = editTextTitle.text.toString().trim()
        val note = editTextNote.text.toString().trim()

        // Insert list
        toDoListViewModel.insertList(
            ToDoList(
                createdDate = createdDate,
                strCreatedDate = strCreatedDate,
                title = title,
                dueDate = dueDate,
                dueHour = dueHour,
                strDueDate = strDueDate,
                strDueHour = strDueHour,
                note = note,
                isFinished = false
            )
        )

        finish()
    }
}
