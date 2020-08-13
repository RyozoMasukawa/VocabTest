package com.example.wasacon.vocabtest

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmQuery
import io.realm.RealmResults
import java.lang.StringBuilder

class CustomRecyclerViewAdapter(realmResults : RealmResults<Dictionary>) : RecyclerView.Adapter<ViewHolder>() {
    private val rResults : RealmResults<Dictionary> = realmResults

    private val wordSet: MutableSet<String?> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.word, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return rResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = rResults[position]

        if (wordSet.add(result?.word)) {
            holder.wordText?.setText(result?.word)
            val buf = StringBuilder()
            buf.append("1." + result?.meaning)

            val meaningSet : MutableSet<String?> = mutableSetOf(result?.meaning)

            val rQuery : RealmQuery<Dictionary> = rResults.where()

            val dict = rQuery.equalTo("word", result?.word).findAll()

            for (d in dict) {
                var i = 2
                if (meaningSet.add(d.meaning)) {
                    buf.append("\n" + i + "." + d.meaning)
                    i += 1
                }
            }

            holder.meaningText?.setText(buf.toString())
            holder.itemView.setBackgroundColor(if (wordSet.size % 2 == 0) Color.LTGRAY else Color.WHITE)
        } else {
            holder.wordText?.setVisibility(View.GONE)
            holder.meaningText?.setVisibility(View.GONE)
            holder.itemView.setVisibility(View.GONE)
        }
    }
}