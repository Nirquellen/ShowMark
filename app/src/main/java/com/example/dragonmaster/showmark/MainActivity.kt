package com.example.dragonmaster.showmark

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ShowItemAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        recyclerView = findViewById(R.id.item_list)

        fab.setOnClickListener { view ->
            addNewItemDialog()
        }

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        adapter = ShowItemAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val swipeHandler = object : SwipeToChangeEpisodeCallback(this) {
            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                return
            }

            override fun plusEpisode(viewHolder: RecyclerView.ViewHolder) {
                val item: ShowItem = adapter.getItemAt(viewHolder.adapterPosition)
                ++item.currentEpisode
                DatabaseAsyncTask(item).execute()

            }

            override fun minusEpisode(viewHolder: RecyclerView.ViewHolder) {
                val item: ShowItem = adapter.getItemAt(viewHolder.adapterPosition)
                --item.currentEpisode
                DatabaseAsyncTask(item).execute()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_ITEM)

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(child in dataSnapshot.children) {
                    val show: ShowItem = child.getValue(ShowItem::class.java)!!
                    adapter.update(show)
                }
            }

        })
    }

    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val nameEditText = EditText(this)

        alert.setMessage("Add Show Name")
        alert.setTitle("Enter a new show")

        alert.setView(nameEditText)

        alert.setPositiveButton("Done") { dialog, positiveButton ->
            val showItem = ShowItem()
            showItem.showName = nameEditText.text.toString()

            DatabaseAsyncTask(showItem).execute()
            dialog.dismiss()
            Toast.makeText(this, "Show saved", Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }
}
