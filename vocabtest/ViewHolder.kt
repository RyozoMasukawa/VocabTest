package com.example.wasacon.vocabtest

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.word.view.*

class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var wordText: TextView? = null
    var meaningText: TextView? = null
    var resultImage: ImageView? = null

    init {
        wordText = itemView.wordText
        meaningText = itemView.meaningText
        resultImage = itemView.resultImage
    }

}