package com.ivansertic.magictournament.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.UserPair


class RecViewPopupAdapter(private val scoreList: HashMap<String, ArrayList<UserPair>>) :
    RecyclerView.Adapter<RecViewPopupAdapter.ViewHolder>() {
    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val playerOne: TextInputLayout = itemView.findViewById(R.id.playerOne)
        val playerOneScore: TextInputLayout = itemView.findViewById(R.id.playerOneScore)
        val playerTwo: TextInputLayout = itemView.findViewById(R.id.playerTwo)
        val playerTwoScore: TextInputLayout = itemView.findViewById(R.id.playerTwoScore)

        fun bind(pairs: ArrayList<UserPair>){
            playerOne.editText?.setText(pairs[0].username)
            playerOneScore.editText?.setText(pairs[0].score.toString())
            playerTwo.editText?.setText(pairs[1].username)
            playerTwoScore.editText?.setText(pairs[1].score.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popup_rec_view_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        scoreList["pair${position+1}"]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }
}