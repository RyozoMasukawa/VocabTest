package com.example.wasacon.vocabtest

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmQuery
import io.realm.RealmResults
import java.lang.StringBuilder

class ResultRecyclerViewAdapter(realmResults: RealmResults<Dictionary>) : RecyclerView.Adapter<ViewHolder>() {
    private val rResults : RealmResults<Dictionary> = realmResults
    private val wordSet: MutableSet<String?> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CustomRecyclerViewAdapter(rResults).onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return rResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = rResults[position]

        if (wordSet.add(result?.word)) {
            holder.wordText?.text = result?.word

            val rQuery : RealmQuery<Dictionary> = rResults.where()
            //TestActivityの呼び出しの度にフィールドとなっている番号更新、

            val dict = rQuery.equalTo("word", result?.word).findAll()

            val buf = StringBuilder()

            val booleanList = mutableListOf<Boolean>()

            var i = 0

            for (d in dict) {
                if (i < dict.size) {
                    buf.append(d.meaning + ", ")
                    booleanList.add(d.isCorrect)
                }
            }

            if (booleanList.all { it == true }) {
                holder.resultImage?.setImageResource(R.drawable.mark_maru)
            } else {
                holder.resultImage?.setImageResource(R.drawable.mark_batsu)
            }


            holder.meaningText?.text = convertMeaning(buf.toString())

            holder.itemView.setBackgroundColor(if (wordSet.size % 2 == 0) Color.LTGRAY else Color.WHITE)
        } else {
            holder.wordText?.visibility = View.GONE
            holder.meaningText?.visibility = View.GONE
            holder.itemView.visibility = View.GONE
        }
    }

    private fun convertMeaning(meaning : String) : String{
        val buf = StringBuilder()

        var no = 0

        var i = 0

        val meanings : MutableSet<String> = mutableSetOf()

        var tmp = StringBuilder()

        while (i < meaning.length) {
            while (i < meaning.length && !meaning[i].isLetter()) {
                i++
            }

            if (i == meaning.length) {
                break
            }

            while (i < meaning.length && meaning[i].isLetter()) {
                tmp.append(meaning[i])
                i++
            }

            meanings.add(tmp.toString())
            tmp.clear()
        }

        for (meaning in meanings) {
            buf.append((++no).toString() + "." + meaning + "\n")
        }

        return buf.toString()
    }
}