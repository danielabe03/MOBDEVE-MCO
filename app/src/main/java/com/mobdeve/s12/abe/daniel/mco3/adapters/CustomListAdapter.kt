package com.mobdeve.s12.abe.daniel.mco3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.abe.daniel.mco3.R
import com.mobdeve.s12.abe.daniel.mco3.models.CustomList

class CustomListAdapter(
    private val customLists: List<CustomList>,
    private val onCustomListClick: (CustomList) -> Unit,
    private val onDeleteCustomListClick: (CustomList) -> Unit
) : RecyclerView.Adapter<CustomListAdapter.CustomListViewHolder>() {

    class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listName: TextView = itemView.findViewById(R.id.tvCustomListName)
        val deleteButton: Button = itemView.findViewById(R.id.btnDeleteCustomList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_custom_list, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {
        val customList = customLists[position]
        holder.listName.text = customList.name
        holder.itemView.setOnClickListener { onCustomListClick(customList) }
        holder.deleteButton.setOnClickListener { onDeleteCustomListClick(customList) }
    }

    override fun getItemCount(): Int = customLists.size
}
