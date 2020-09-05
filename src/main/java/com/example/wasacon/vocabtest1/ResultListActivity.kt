package com.example.wasacon.vocabtest1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.Sort

import kotlinx.android.synthetic.main.activity_result_list.*
import kotlinx.android.synthetic.main.content_note.*

class ResultListActivity : AppCompatActivity() {
    private lateinit var realm : Realm
    private lateinit var adapter: ResultRecyclerViewAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_list)
        setSupportActionBar(toolbar)

        realm = Realm.getDefaultInstance()

        returnBtn.setOnClickListener { view ->
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val realmResults =  realm.where(Dictionary::class.java)
            .equalTo("category", intent.getLongExtra("category", 0L))
            .findAll()
            .sort("word", Sort.ASCENDING)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = ResultRecyclerViewAdapter(realmResults)
        recyclerView.adapter = this.adapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}
