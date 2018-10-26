package com.example.dragonmaster.showmark

import android.os.AsyncTask
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.WeakReference

class DatabaseAsyncTask(var item: ShowItem/*, listener: Listener*/) : AsyncTask<Void, Void, Void>() {

    //val listener: WeakReference<Listener> = WeakReference(listener)
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun doInBackground(vararg params: Void?): Void? {
        database.child(Constants.FIREBASE_ITEM).push().setValue(item)
        return null
    }

    /*override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        val listener = listener.get()
        if (listener != null) {
            listener.onComplete()
        }
    }

    interface Listener {
        fun onComplete() //zrusit progress
    }*/
}