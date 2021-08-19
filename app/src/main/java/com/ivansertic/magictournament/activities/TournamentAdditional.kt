package com.ivansertic.magictournament.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.enums.ConstructedTypes
import com.ivansertic.magictournament.models.enums.LimitedTypes
import com.ivansertic.magictournament.models.enums.TournamentFormat
import java.text.SimpleDateFormat
import java.util.*

class TournamentAdditional : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_additional)

        addItemsToDropdown()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        val datePickerText: TextInputEditText = findViewById(R.id.tfDate)
        val timePickerText: TextInputEditText = findViewById(R.id.tfTime)

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        datePickerText.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag")
        }

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Time")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val simpleDateFormat = SimpleDateFormat("dd.MM.YYYY.")
            datePickerText.setText(simpleDateFormat.format(datePicker.selection))
        }

        timePickerText.setOnClickListener {
            timePicker.show(supportFragmentManager, "tag");
        }

        timePicker.addOnPositiveButtonClickListener {
            timePickerText.setText("${timePicker.hour}:${timePicker.minute}")
        }
    }

    private fun addItemsToDropdown() {
        val dropDownMenuFormat: TextInputLayout = findViewById(R.id.menuFormat)
        val menuFormatType: TextInputLayout = findViewById(R.id.menuFormatType)

        val itemsFormat = TournamentFormat.values()

        val itemAdapter = ArrayAdapter(this,R.layout.list_item,itemsFormat)
        (dropDownMenuFormat.editText as? AutoCompleteTextView)?.setAdapter(itemAdapter)
        (dropDownMenuFormat.editText as? AutoCompleteTextView)?.setText(itemsFormat[1].toString(),false)

        val itemsFormatType = ConstructedTypes.values()
        val itemsAdapterFormatType = ArrayAdapter(this, R.layout.list_item, itemsFormatType)
        (menuFormatType.editText as? AutoCompleteTextView)?.setAdapter(itemsAdapterFormatType)
        (menuFormatType.editText as? AutoCompleteTextView)?.setText(itemsFormatType[0].toString(),false)

        setOnSelectListener((dropDownMenuFormat.editText as? AutoCompleteTextView),(menuFormatType.editText as? AutoCompleteTextView))
    }

    private fun setOnSelectListener(format: AutoCompleteTextView?, formatTypes: AutoCompleteTextView?) {
       format?.setOnItemClickListener { parent, view, position, id ->
           if(parent.adapter.getItem(position) == TournamentFormat.LIMITED){
               formatTypes?.setAdapter(ArrayAdapter(this, R.layout.list_item,LimitedTypes.values()))
               formatTypes?.setText(LimitedTypes.DRAFT.toString(),false)
           }else{
               formatTypes?.setAdapter(ArrayAdapter(this, R.layout.list_item,ConstructedTypes.values()))
               formatTypes?.setText(ConstructedTypes.MODERN.toString(),false)
           }
       }
    }
}