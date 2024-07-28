package com.mobdeve.s12.abe.daniel.mco3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.abe.daniel.mco3.R
import com.mobdeve.s12.abe.daniel.mco3.models.Show

class ShowAdapter(private val shows: List<Show>, private val onDeleteClick: (Show) -> Unit) :
    RecyclerView.Adapter<ShowAdapter.ShowViewHolder>() {

    inner class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvShowName: TextView = itemView.findViewById(R.id.tvShowName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val tvComments: TextView = itemView.findViewById(R.id.tvComments)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(show: Show) {
            tvShowName.text = show.name
            tvStatus.text = "Status: ${show.status}"
            ratingBar.rating = show.rating
            tvComments.text = "Comments: ${show.comments ?: "None"}"
            btnDelete.setOnClickListener { onDeleteClick(show) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return ShowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    override fun getItemCount() = shows.size
}