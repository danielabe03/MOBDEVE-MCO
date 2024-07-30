package com.mobdeve.s12.abe.daniel.mco3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s12.abe.daniel.mco3.R
import com.mobdeve.s12.abe.daniel.mco3.models.TVShow

class TVShowAdapter(private val onItemClick: (TVShow) -> Unit) : RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder>() {

    private var tvShows: List<TVShow> = emptyList()

    fun updateTVShows(tvShows: List<TVShow>) {
        this.tvShows = tvShows
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tv_show, parent, false)
        return TVShowViewHolder(itemView, onItemClick)
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        val tvShow = tvShows[position]
        holder.bind(tvShow)
    }

    override fun getItemCount(): Int = tvShows.size

    class TVShowViewHolder(itemView: View, private val onItemClick: (TVShow) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvShowName: TextView = itemView.findViewById(R.id.tvShowName)
        private val tvShowSummary: TextView = itemView.findViewById(R.id.tvShowSummary)
        private val tvShowImage: ImageView = itemView.findViewById(R.id.tvShowImage)

        fun bind(tvShow: TVShow) {
            tvShowName.text = tvShow.name
            tvShowSummary.text = tvShow.summary
            Glide.with(itemView.context).load(tvShow.image?.medium).into(tvShowImage)
            itemView.setOnClickListener { onItemClick(tvShow) }
        }
    }
}
