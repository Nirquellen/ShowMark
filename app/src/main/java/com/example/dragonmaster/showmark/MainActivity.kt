package com.example.dragonmaster.showmark

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ShowItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        recyclerView = findViewById(R.id.item_list)

        fab.setOnClickListener { view ->
            addNewItemDialog()
        }


        dbHandler = DBHandler(this)
        adapter = ShowItemAdapter(dbHandler.allShows())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val swipeHandler = object : SwipeToChangeEpisodeCallback(this) {
            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                return
            }

            override fun plusEpisode(viewHolder: RecyclerView.ViewHolder) {
                val item: ShowItem = adapter.getItemAt(viewHolder.adapterPosition)
                ++item.currentEpisode
                dbHandler.updateShow(item)
                adapter.editItem(viewHolder.adapterPosition, item)
            }

            override fun minusEpisode(viewHolder: RecyclerView.ViewHolder) {
                val item: ShowItem = adapter.getItemAt(viewHolder.adapterPosition)
                --item.currentEpisode
                dbHandler.updateShow(item)
                adapter.editItem(viewHolder.adapterPosition, item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun addNewItemDialog() {
        val dialog = Dialog(this)
        dialog.setTitle("Save Show To Database")
        dialog.setContentView(R.layout.add_dialog)

        val save: Button = dialog.findViewById(R.id.save_button)
        val name: EditText = dialog.findViewById(R.id.name_edit_text)
        val episodes: EditText = dialog.findViewById(R.id.episodes_edit_text)
        save.setOnClickListener {
            val showItem = ShowItem(name.text.toString(), Integer.parseInt(episodes.text.toString()))
            if (dbHandler.addShow(showItem)) {
                adapter.addItem(showItem)
                Toast.makeText(this, "Show saved", Toast.LENGTH_SHORT).show()
                dialog.hide()
            }
        }
        dialog.show()
    }
}
