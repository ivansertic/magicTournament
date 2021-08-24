package com.ivansertic.magictournament.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.Tournament

class RecViewAdapter(private val tournamentList: List<Tournament>) :
    RecyclerView.Adapter<RecViewAdapter.ViewHolder>() {
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rcycler_view_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = tournamentList[position]

    }

    override fun getItemCount(): Int {
        return tournamentList.size

    }
}