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

class ShowAdapter(
    private val shows: List<Show>,
    private val onDeleteClick: (Show) -> Unit
) : RecyclerView.Adapter<ShowAdapter.ShowViewHolder>() {

    class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showName: TextView = itemView.findViewById(R.id.tvShowName)
        val showStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val comments: TextView = itemView.findViewById(R.id.tvComments)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show, parent, false)
        return ShowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val show = shows[position]
        holder.showName.text = show.name
        holder.showStatus.text = "Status: ${show.status}"
        holder.ratingBar.rating = show.rating
        holder.comments.text = show.comments ?: "No comments"
        holder.deleteButton.setOnClickListener {
            onDeleteClick(show)
        }
    }

    override fun getItemCount(): Int = shows.size
}
