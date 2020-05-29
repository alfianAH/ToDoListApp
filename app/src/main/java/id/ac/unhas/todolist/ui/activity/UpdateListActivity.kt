package id.ac.unhas.todolist.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import id.ac.unhas.todolist.R
import id.ac.unhas.todolist.db.todolist.ToDoList
import id.ac.unhas.todolist.ui.Converter
import id.ac.unhas.todolist.ui.view_model.ToDoListViewModel
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class UpdateListActivity : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextNote: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: Button
    private lateinit var chkBoxIsFinished: CheckBox
    private lateinit var toDoListViewModel: ToDoListViewModel
    private lateinit var toDoList: ToDoList
    private var calendar = Calendar.getInstance()

    companion object{
        const val EXTRA_TITLE_UPDATE = "TITLE"
        const val EXTRA_DATE_UPDATE = "date-month-year"
        const val EXTRA_TIME_UPDATE = "hour:minutes"
        const val EXTRA_NOTE_UPDATE = "NOTE"
        const val EXTRA_IS_FINISHED_UPDATE = "false"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_list)
        // Change the menu title
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
        chkBoxIsFinished = findViewById(R.id.checkbox_is_finished)
        toDoListViewModel = ViewModelProvider(this).get(ToDoListViewModel::class.java)

        getExtra() // Get data from MainActivity

        editTextDate.setOnClickListener{
            setDueDate() // Date Picker dialog
        }

        editTextTime.setOnClickListener {
            setDueTime() // Time picker dialog
        }

        btnUpdate.setOnClickListener{
            updateList(toDoList) // update list
        }

        btnCancel.setOnClickListener{
            finish() // Cancel back to home
        }
    }

    // Back Button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    // Get Data from MainActivity
    private fun getExtra(){
        toDoList = intent.getParcelableExtra("EXTRA_LIST")!!
        editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE_UPDATE))
        editTextDate.setText(intent.getStringExtra(EXTRA_DATE_UPDATE))
        editTextTime.setText(intent.getStringExtra(EXTRA_TIME_UPDATE))
        editTextNote.setText(intent.getStringExtra(EXTRA_NOTE_UPDATE))
        chkBoxIsFinished.isChecked = intent.getBooleanExtra(EXTRA_IS_FINISHED_UPDATE, false)
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

    // Update list when button is clicked
    private fun updateList(toDoList: ToDoList){
        // if check box is checked, delete list
        // else, update database
        if(chkBoxIsFinished.isChecked){
            toDoListViewModel.deleteList(toDoList)
        } else  {
            // Get current time (UTC+8)
            val current = ZonedDateTime.now(ZoneId.of("+8"))
            val updatedDate = Converter.dateToInt(current)

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

            // Set the value of database
            toDoList.updatedDate = updatedDate
            toDoList.title = editTextTitle.text.toString().trim()
            toDoList.dueDate = dueDate
            toDoList.dueHour = dueHour
            toDoList.strDueDate = strDueDate
            toDoList.strDueHour = strDueHour
            toDoList.note = editTextNote.text.toString().trim()
            toDoList.isFinished = chkBoxIsFinished.isChecked

            // Update the value
            toDoListViewModel.updateList(toDoList)
        }

        finish()
    }
}
