package com.ivansertic.magictournament.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivansertic.magictournament.R


class RecViewPopupAdapter(private val scoreList: ArrayList<String>) :
    RecyclerView.Adapter<RecViewPopupAdapter.ViewHolder>() {
    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popup_rec_view_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = scoreList[position]
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }
}