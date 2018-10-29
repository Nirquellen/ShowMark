package com.example.dragonmaster.showmark

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ShowItemAdapter(var showItemList: MutableList<ShowItem>): RecyclerView.Adapter<ShowItemAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.show_item, parent, false)
        return ShowsViewHolder(view)
    }

    override fun getItemCount() = showItemList.size

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bindShow(showItemList[position])
        getItemAt(position)
    }

    fun getItemAt(pos: Int) = showItemList[pos]

    fun addItem(showItem: ShowItem) {
        showItemList.add(showItem)
        notifyItemInserted(itemCount - 1)
    }

    fun editItem(position: Int, showItem: ShowItem) {
        showItemList[position] = showItem
        notifyItemChanged(position)
    }

    class ShowsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.show_name)
        val episodes: TextView = itemView.findViewById(R.id.episodes)

        fun bindShow(show: ShowItem) {
            name.text = show.showName
            episodes.text = show.currentEpisode.toString() + "/" + show.allEpisodes
        }

    }

}
