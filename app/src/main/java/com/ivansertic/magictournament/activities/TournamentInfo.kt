package com.ivansertic.magictournament.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.enums.ConstructedTypes
import com.ivansertic.magictournament.models.enums.LimitedTypes
import com.ivansertic.magictournament.models.enums.TournamentFormat
import com.ivansertic.magictournament.viewmodels.RegisterViewModel
import com.ivansertic.magictournament.viewmodels.TournamentInfoViewModel
import java.io.FileDescriptor
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class TournamentInfo : AppCompatActivity() {
    private lateinit var dropDownMenuFormat: TextInputLayout
    private lateinit var menuFormatType: TextInputLayout
    private lateinit var tournamentDescription: TextInputLayout
    private lateinit var datePickerLayout: TextInputLayout
    private lateinit var timePickerLayout: TextInputLayout
    private lateinit var tournamentTitle: TextInputLayout
    private lateinit var tournamentInfoViewModel: TournamentInfoViewModel
    private lateinit var cancelButton: MaterialButton
    private lateinit var submitButton: MaterialButton
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_info)

        this.dropDownMenuFormat = findViewById(R.id.menuFormat)
        this.menuFormatType = findViewById(R.id.menuFormatType)
        this.tournamentDescription = findViewById(R.id.tournamentDescription)
        this.datePickerLayout = findViewById(R.id.inputLayoutDate)
        this.timePickerLayout = findViewById(R.id.inputLayoutTime)
        this.tournamentTitle = findViewById(R.id.tfTitle)

        this.cancelButton = findViewById(R.id.tACancel)
        this.submitButton = findViewById(R.id.tASubmit)

        this.tournamentInfoViewModel =
            ViewModelProvider(this).get(TournamentInfoViewModel::class.java)

        this.sharedPreferences = getSharedPreferences("Tournament", Context.MODE_PRIVATE)

        addItemsToDropdown()
        setOnClickListeners()

    }

    private fun setOnClickListeners() {

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        this.datePickerLayout.editText?.setOnClickListener {
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
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy.")
            this.datePickerLayout.editText?.setText(simpleDateFormat.format(datePicker.selection))
        }

        timePickerLayout.editText?.setOnClickListener {
            timePicker.show(supportFragmentManager, "tag");
        }

        timePicker.addOnPositiveButtonClickListener {
            timePickerLayout.editText?.setText("${timePicker.hour}:${timePicker.minute}")
        }

        cancelButton.setOnClickListener {
            this.finish()
        }

        submitButton.setOnClickListener {
            tournamentInfoViewModel.checkData(tournamentTitle, tournamentDescription)
            tournamentInfoViewModel.checkDate(datePickerLayout, timePickerLayout)
            tournamentInfoViewModel.status.observe(this, { status ->
                status?.let {

                    if (status == false) {
                        tournamentInfoViewModel.status.value = null
                        Toast.makeText(this, "Please check the data!", Toast.LENGTH_LONG).show()

                    } else {
                        tournamentInfoViewModel.status.value = null
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()

                        editor.putString("tournamentTitle",tournamentTitle.editText?.text.toString())
                        editor.putString("tournamentDescription",tournamentDescription.editText?.text.toString())
                        editor.putString("tournamentDate",datePickerLayout.editText?.text.toString())
                        editor.putString("tournamentTime",timePickerLayout.editText?.text.toString())
                        editor.putString("tournamentType",dropDownMenuFormat.editText?.text.toString())
                        editor.putString("tournamentSubType", menuFormatType.editText?.text.toString())

                        editor.apply()

                        startActivity(Intent(this, TournamentLocation::class.java))
                    }
                }
            })
        }


    }

    private fun addItemsToDropdown() {
        val itemsFormat = TournamentFormat.values()

        val itemAdapter = ArrayAdapter(this, R.layout.list_item, itemsFormat)
        (dropDownMenuFormat.editText as? AutoCompleteTextView)?.setAdapter(itemAdapter)
        (dropDownMenuFormat.editText as? AutoCompleteTextView)?.setText(
            itemsFormat[1].toString(),
            false
        )

        val itemsFormatType = ConstructedTypes.values()
        val itemsAdapterFormatType = ArrayAdapter(this, R.layout.list_item, itemsFormatType)
        (menuFormatType.editText as? AutoCompleteTextView)?.setAdapter(itemsAdapterFormatType)
        (menuFormatType.editText as? AutoCompleteTextView)?.setText(
            itemsFormatType[0].toString(),
            false
        )

        setOnSelectListener(
            (dropDownMenuFormat.editText as? AutoCompleteTextView),
            (menuFormatType.editText as? AutoCompleteTextView)
        )
    }

    private fun setOnSelectListener(
        format: AutoCompleteTextView?,
        formatTypes: AutoCompleteTextView?
    ) {
        format?.setOnItemClickListener { parent, view, position, id ->
            if (parent.adapter.getItem(position) == TournamentFormat.LIMITED) {
                formatTypes?.setAdapter(
                    ArrayAdapter(
                        this,
                        R.layout.list_item,
                        LimitedTypes.values()
                    )
                )
                formatTypes?.setText(LimitedTypes.DRAFT.toString(), false)
            } else {
                formatTypes?.setAdapter(
                    ArrayAdapter(
                        this,
                        R.layout.list_item,
                        ConstructedTypes.values()
                    )
                )
                formatTypes?.setText(ConstructedTypes.MODERN.toString(), false)
            }
        }
    }
}