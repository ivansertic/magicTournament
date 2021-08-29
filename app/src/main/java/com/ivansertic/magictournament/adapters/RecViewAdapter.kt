package com.ivansertic.magictournament.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.Tournament
import java.time.format.DateTimeFormatter

class RecViewAdapter(private val tournamentList: ArrayList<Tournament>) :
    RecyclerView.Adapter<RecViewAdapter.ViewHolder>() {

    private lateinit var listener: onItemButtonClickListener
    private var visible = true
    class ViewHolder(ItemView: View, listener: onItemButtonClickListener) : RecyclerView.ViewHolder(ItemView) {

        val mapButton: MaterialButton = itemView.findViewById(R.id.itemShowMap)
        val applyButton: MaterialButton = itemView.findViewById(R.id.itemApply)
        val format: MaterialTextView = itemView.findViewById(R.id.itemFormat)
        val type: MaterialTextView = itemView.findViewById(R.id.itemType)
        val date: MaterialTextView = itemView.findViewById(R.id.itemDate)
        val country: MaterialTextView = itemView.findViewById(R.id.itemCountry)
        val city: MaterialTextView = itemView.findViewById(R.id.itemCity)
        val address: MaterialTextView = itemView.findViewById(R.id.itemAddress)
        val title: MaterialTextView = itemView.findViewById(R.id.itemTitle)
        val description: MaterialTextView = itemView.findViewById(R.id.itemDescription)
        val detailsButton: MaterialButton = itemView.findViewById(R.id.itemDetails)

        init {
            mapButton.setOnClickListener{
                listener.onShowMapClick(absoluteAdapterPosition)
            }

            applyButton.setOnClickListener{
                listener.onApplyClick(absoluteAdapterPosition)
            }

            detailsButton.setOnClickListener{
                listener.onDetailsClick(absoluteAdapterPosition,itemView.context)
            }
        }

        fun bind(tournament: Tournament, visible: Boolean){
            title.text = tournament.title
            city.text = tournament.tournamentLocation.city
            country.text = tournament.tournamentLocation.country
            address.text = tournament.tournamentLocation.address.split(",")[0]
            date.text = tournament.tournamentDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")).toString()
            format.text = tournament.type.toString()
            type.text = tournament.subType.toString()
            description.text = tournament.description

            if(!visible){
                applyButton.visibility = View.GONE
                detailsButton.visibility = View.VISIBLE
            }else{
                applyButton.visibility = View.VISIBLE
                detailsButton.visibility = View.GONE
            }
        }
    }

    interface onItemButtonClickListener{
        fun onApplyClick(position: Int)
        fun onShowMapClick(position: Int)
        fun onDetailsClick(position: Int, context: Context)
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
        holder.bind(tournamentList[position], visible)
    }

    override fun getItemCount(): Int {
        return tournamentList.size

    }

    fun getItem(position: Int): Tournament{
        return tournamentList[position]
    }

    fun removeApplied(position: Int){
        tournamentList.removeAt(position)
        notifyDataSetChanged()
    }

    fun changeData(array: ArrayList<Tournament>){
        tournamentList.clear()
        tournamentList.addAll(array)
        notifyDataSetChanged()
    }

    fun hideShowButtons(visible: Boolean){
        this.visible = visible
        notifyDataSetChanged()
    }
}