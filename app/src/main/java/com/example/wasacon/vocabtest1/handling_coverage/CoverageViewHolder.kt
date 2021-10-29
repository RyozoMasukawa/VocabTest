package com.example.wasacon.vocabtest1.handling_coverage

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.view.ContextMenu
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wasacon.vocabtest1.Dictionary
import com.example.wasacon.vocabtest1.MainActivity
import com.example.wasacon.vocabtest1.NoteActivity
import com.example.wasacon.vocabtest1.R
import io.realm.Realm
import kotlinx.android.synthetic.main.one_coverage.view.*




class CoverageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var dateText : TextView? = null
    var nameText : TextView? = null
    var category : Long? = 0L

    init {

        dateText = itemView.dateText
        nameText = itemView.nameText
    }
}