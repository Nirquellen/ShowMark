package com.example.dragonmaster.showmark

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.properties.Delegates

class ShowItemAdapter: RecyclerView.Adapter<ShowItemAdapter.ShowsViewHolder>() {

    var showItemList: MutableList<ShowItem> = mutableListOf()

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

    fun removeItemByObject(show: ShowItem){
        val position = showItemList.indexOfFirst { it.showName == show.showName }
        showItemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun update(show: ShowItem){
        val position = showItemList.indexOfFirst { it.showName == show.showName }
        if(position >= 0) {
            showItemList[position] = show
            notifyItemChanged(position)
        } else {
            showItemList.add(show)
            notifyDataSetChanged()
        }
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
