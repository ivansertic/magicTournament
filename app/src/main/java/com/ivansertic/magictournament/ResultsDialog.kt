package com.ivansertic.magictournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivansertic.magictournament.adapters.RecViewPopupAdapter

class ResultsDialog: DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecViewPopupAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView: View = inflater.inflate(R.layout.popup_results, container,false)

        recyclerView = mainView.findViewById(R.id.recycleViewPopup)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val testArray = ArrayList<String>()

        for(i in 0..3){
            testArray.add("a${i}")
        }

        recyclerViewAdapter = RecViewPopupAdapter(testArray)
        recyclerView.adapter = recyclerViewAdapter

        return mainView
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width,height)
    }
}