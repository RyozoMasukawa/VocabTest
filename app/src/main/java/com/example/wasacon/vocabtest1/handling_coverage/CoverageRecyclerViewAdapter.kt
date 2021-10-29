package com.example.wasacon.vocabtest1.handling_coverage

import android.content.Intent
import android.graphics.Color
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.wasacon.vocabtest1.*
import io.realm.Realm
import io.realm.RealmResults

class CoverageRecyclerViewAdapter(realmResults: RealmResults<Coverage>, mode : Int) : RecyclerView.Adapter<CoverageViewHolder>() {
    private val rResults : RealmResults<Coverage> = realmResults

    private val mode = mode

    private val TEST_MODE = 1

    private val EDIT_MODE = 2

    private val NOTE_MODE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_coverage, parent, false)
        val coverageViewHolder =
            CoverageViewHolder(view)
        return coverageViewHolder
    }

    override fun onBindViewHolder(holder: CoverageViewHolder, position: Int) {
        val coverage = rResults[position]
        holder.dateText?.text = DateFormat.format("yyyy/MM/dd kk:mm", coverage?.dateTime)
        holder.nameText?.text = coverage?.name.toString()
        holder.category = rResults[position]?.id

        holder.itemView.setOnLongClickListener {
            val pop = PopupMenu(holder.itemView.context, it)
            pop.inflate(R.menu.remove_context)

            val row_index = position
            if(row_index != null && row_index==position){
                holder.itemView.setBackgroundColor(Color.LTGRAY)
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE)
            }

            pop.setOnMenuItemClickListener {
                    item ->
                when(item.itemId)
                {

                    R.id.remove -> {
                        val realm = Realm.getDefaultInstance()
                        realm.executeTransaction {
                            realm.where(Dictionary::class.java)?.equalTo("category", holder.category)
                                ?.findAll()?.deleteAllFromRealm()
                            realm.where(Coverage::class.java)?.equalTo("id", holder.category)
                                ?.findAll()?.deleteFirstFromRealm()
                        }
                        notifyItemRemoved(position)
                    }

                    R.id.cancel -> {
                        holder.itemView.setBackgroundColor(Color.WHITE)
                    }
                }
                true
            }

            pop.setOnDismissListener {
                holder.itemView.setBackgroundColor(Color.WHITE)

            }

            pop.show()
            true
        }

        holder.itemView.setOnClickListener {
            when (mode) {
                TEST_MODE -> {
                    val intent = Intent(it.context, TestOptionActivity::class.java)
                        .putExtra("category", rResults[position]?.id)
                    it.context.startActivity(intent)
                }

                EDIT_MODE -> {
                    val intent = Intent(it.context, InputActivity::class.java)
                        .putExtra("category", rResults[position]?.id)
                    it.context.startActivity(intent)
                }

                NOTE_MODE -> {
                    val intent = Intent(it.context, NoteActivity::class.java)
                        .putExtra("category", rResults[position]?.id)
                    it.context.startActivity(intent)
                }
            }

            val row_index = position
            if(row_index != null && row_index==position){
                holder.itemView.setBackgroundColor(Color.LTGRAY)
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }

    override fun getItemCount(): Int {
        return rResults.size
    }

}