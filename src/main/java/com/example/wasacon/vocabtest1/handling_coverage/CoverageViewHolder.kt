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
        itemView.setOnLongClickListener {
            val pop = PopupMenu(itemView.context, it)
            pop.inflate(R.menu.remove_context)

            pop.setOnMenuItemClickListener {
                item ->
                when(item.itemId)
                {
                    R.id.remove -> {
                        val realm = Realm.getDefaultInstance()
                        realm.executeTransaction {
                            realm.where(Dictionary::class.java)?.equalTo("category", category)
                                ?.findAll()?.deleteAllFromRealm()
                            realm.where(Coverage::class.java)?.equalTo("id", category)
                                ?.findAll()?.deleteFirstFromRealm()
                        }
                        val intent = Intent(it.context, CoverageListActivity::class.java)
                        it.context.startActivity(intent)
                    }

                    R.id.cancel -> {

                    }
                }
                true
            }
            pop.show()
            true
        }
        dateText = itemView.dateText
        nameText = itemView.nameText
    }
}