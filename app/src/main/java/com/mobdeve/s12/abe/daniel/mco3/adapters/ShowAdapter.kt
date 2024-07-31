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
    private var shows: List<Show>,
    private val deleteShowCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return ShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val show = shows[position]
        holder.bind(show, deleteShowCallback)
    }

    override fun getItemCount(): Int {
        return shows.size
    }

    fun updateShows(newShows: List<Show>) {
        shows = newShows
        notifyDataSetChanged()
    }

    class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvShowName: TextView = itemView.findViewById(R.id.tvShowName)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val tvComments: TextView = itemView.findViewById(R.id.tvComments)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(show: Show, deleteShowCallback: (Show) -> Unit) {
            tvShowName.text = show.name
            tvStatus.text = "Status: ${show.status}"
            ratingBar.rating = show.rating
            tvComments.text = show.comment ?: "No comment"

            btnDelete.setOnClickListener {
                deleteShowCallback(show)
            }
        }
    }
}
