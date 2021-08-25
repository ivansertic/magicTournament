package com.ivansertic.magictournament.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.Tournament

class RecViewAdapter(private val tournamentList: List<Tournament>) :
    RecyclerView.Adapter<RecViewAdapter.ViewHolder>() {


    private lateinit var listener: onItemButtonClickListener
    class ViewHolder(ItemView: View, listener: onItemButtonClickListener) : RecyclerView.ViewHolder(ItemView) {

        val mapButton: MaterialButton = itemView.findViewById(R.id.itemShowMap)
        val applyButton: MaterialButton = itemView.findViewById(R.id.itemApply)

        init {
            mapButton.setOnClickListener{
                listener.onShowMapClick(absoluteAdapterPosition)
            }

            applyButton.setOnClickListener{
                listener.onApplyClick(absoluteAdapterPosition)
            }
        }
    }

    interface onItemButtonClickListener{
        fun onApplyClick(position: Int)
        fun onShowMapClick(position: Int)
    }

    fun setOnItemButtonClickListeners(listener: onItemButtonClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rcycler_view_item, parent, false)

        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = tournamentList[position]

    }

    override fun getItemCount(): Int {
        return tournamentList.size

    }

    private fun getItem(position: Int): Tournament{
        return tournamentList[position]
    }
}