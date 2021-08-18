package com.ivansertic.magictournament.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.enums.ConstructedTypes
import com.ivansertic.magictournament.models.enums.LimitedTypes
import com.ivansertic.magictournament.models.enums.TournamentFormat

class TournamentAdditional : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_additional)

        addItemsToDropdown()
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