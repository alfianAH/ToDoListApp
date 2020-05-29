package id.ac.unhas.todolist.ui

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

// Converter is for changing date data type to int
// so sql can sort it
class Converter {
    companion object{
        // Change date data type to int
        fun dateToInt(time: ZonedDateTime): Int{
            return time.toInstant().epochSecond.toInt()
        }

        // Change string Date to int
        fun stringDateToInt(date: String): Int{
            val formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy")
            return LocalDate.parse(date, formatter).toEpochDay().toInt()
        }

        // Change string Time to int
        fun stringTimeToInt(time: String): Int{
            return LocalTime.parse(time).getLong(ChronoField.MILLI_OF_DAY).toInt()
        }
    }
}