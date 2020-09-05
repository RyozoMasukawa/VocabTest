package com.example.wasacon.vocabtest1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import java.lang.StringBuilder

class CustomRecyclerViewAdapter(realmResults : RealmResults<Dictionary>,
                                private val category: Long) : RecyclerView.Adapter<ViewHolder>() {
    private val rResults : RealmResults<Dictionary> = realmResults

    private val wordSet: MutableSet<String?> = mutableSetOf()

    private val wordMap : Map<String, String> = createWordMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.word, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return wordMap.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wordArray = wordSet.toTypedArray()

        holder.resultImage?.visibility = View.GONE

        val word = wordArray[position]

        holder.wordText?.text = word

        holder.meaningText?.text = wordMap[word]

        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, EditWordActivity::class.java)
                .putExtra("word", holder.wordText?.text)
                .putExtra("meaning", holder.meaningText?.text)
                .putExtra("category", category)
            it.context.startActivity(intent)
        }
    }

    private fun convertMeaning(meaning : String) : String{
        val buf = StringBuilder()

        var no = 0

        var i = 0

        val meanings : MutableSet<String> = mutableSetOf()

        var tmp = StringBuilder()

        while (i < meaning.length) {
            while (i < meaning.length &&
                !(meaning[i].isLetter() /*|| meaning[i] == '(' || meaning[i] == '（' || meaning[i] == ')' || meaning[i] == '）'*/)) {
                i++
            }

            if (i == meaning.length) {
                break
            }

            while (i < meaning.length &&
                (meaning[i].isLetter() /*|| meaning[i] == '(' || meaning[i] == '（' || meaning[i] == ')' || meaning[i] == '）'*/)) {
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

    private fun createWordMap() : MutableMap<String, String> {
        val wordMap : MutableMap<String, String> = mutableMapOf()

        for (result in rResults) {
            if (wordSet.add(result?.word)) {
                val dict = rResults.where()
                    .equalTo("word", result?.word).findAll()

                val buf = StringBuilder()

                for (d in dict) {
                    buf.append(d.meaning + ", ")
                }
                wordMap[result.word] = convertMeaning(buf.toString())
            }
        }
        return wordMap
    }
}