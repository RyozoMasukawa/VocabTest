package com.example.wasacon.vocabtest1

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import java.lang.StringBuilder

class ResultRecyclerViewAdapter(realmResults: RealmResults<Dictionary>, wordSet: MutableSet<String>) : RecyclerView.Adapter<ViewHolder>() {
    private val rResults : RealmResults<Dictionary> = realmResults
    private val wordSet: MutableSet<String> = wordSet
    private val wordMap : Map<String, String> = createWordMap()

    private val allowedCharSet : Set<Char> = setOf('-', '(', ')', '（', '）', ' ', '　')

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CustomRecyclerViewAdapter(rResults, 0L).onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return wordSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wordArray = wordSet.toTypedArray()
        val word = wordArray[position]

        holder.wordText?.text = word

        holder.meaningText?.text = wordMap[word]

        val dict = rResults.where()
            .equalTo("word", word).findAll()

        val booleanList = mutableListOf<Boolean>()

        for (d in dict) {
            booleanList.add(d.isCorrect)
        }

        if (booleanList.all { it }) {
            holder.resultImage?.setImageResource(R.drawable.mark_maru)
        } else {
            holder.resultImage?.setImageResource(R.drawable.mark_batsu)
        }


        /*holder.itemView.setOnClickListener{
            val intent = Intent(it.context, EditWordActivity::class.java)
                .putExtra("word", holder.wordText?.text)
                .putExtra("meaning", holder.meaningText?.text)
            it.context.startActivity(intent)
        }*/

        val result = rResults[position]
    }

    private fun convertMeaning(meaning : String) : String{
        val buf = StringBuilder()

        //var no = 0

        var i = 0

        val meanings : MutableSet<String> = mutableSetOf()

        var tmp = StringBuilder()

        while (i < meaning.length) {
            while (i < meaning.length &&
                !(meaning[i].isLetterOrDigit() || meaning[i] == '-' || meaning[i] == '(' || meaning[i] == '（' || meaning[i] == ')' || meaning[i] == '）' || meaning[i] == ' ' || meaning[i] == '　')) {
                i++
            }

            if (i == meaning.length) {
                break
            }

            while (i < meaning.length &&
                (meaning[i].isLetterOrDigit() || meaning[i] == '-' || meaning[i] == '(' || meaning[i] == '（' || meaning[i] == ')' || meaning[i] == '）' || meaning[i] == ' ' || meaning[i] == '　')) {
                tmp.append(meaning[i])
                i++
            }

            meanings.add(tmp.toString())
            tmp.clear()
        }

        for (meaning in meanings) {
            buf.append(meaning + "\n")
        }

        return buf.toString()
    }

    private fun createWordMap() : MutableMap<String, String> {
        val wordMap : MutableMap<String, String> = mutableMapOf()

        for (result in rResults) {

            val dict = rResults.where()
                .equalTo("word", result?.word)
                .findAll()

            val buf = StringBuilder()

            for (d in dict) {
                buf.append(d.meaning + ", ")
            }
            wordMap[result.word] = convertMeaning(buf.toString())
        }
        return wordMap
    }
}