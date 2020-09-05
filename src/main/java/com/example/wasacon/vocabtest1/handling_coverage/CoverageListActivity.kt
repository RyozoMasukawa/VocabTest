package com.example.wasacon.vocabtest1.handling_coverage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wasacon.vocabtest1.EditCategoryActivity
import com.example.wasacon.vocabtest1.MainActivity
import com.example.wasacon.vocabtest1.R
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_coverage_list.*
import kotlinx.android.synthetic.main.content_note.*

class CoverageListActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    private lateinit var crvAdapter: CoverageRecyclerViewAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var realmResults : RealmResults<Coverage>

    private val EDIT_MODE = 2

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coverage_list)
        setSupportActionBar(toolbar)

        realm = Realm.getDefaultInstance()

        val mode = intent.getIntExtra("mode", 0)

        if (mode != EDIT_MODE) {
            add.visibility = View.INVISIBLE
        }
        add.setOnClickListener {
            val i_edit = Intent(this, EditCategoryActivity::class.java)
            startActivity((i_edit))
        }
    }

    override fun onStart() {
        super.onStart()

        val subjectSet : MutableSet<String> = createSubjectSet()

        val adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            subjectSet.toTypedArray()
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        layoutManager = LinearLayoutManager(this)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinnerParent = parent as Spinner
                val subject = spinnerParent.selectedItem as String
                realmResults =  realm.where(Coverage::class.java)
                    .equalTo("subject", subject)
                    .findAll()

                recyclerView.layoutManager = layoutManager
                crvAdapter =
                    CoverageRecyclerViewAdapter(
                        realmResults,
                        intent.getIntExtra("mode", 0)
                    )
                recyclerView.adapter = crvAdapter
                recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun createSubjectSet() : MutableSet<String> {
        val coverages = realm.where<Coverage>()
            .findAll()
        val ret : MutableSet<String> = mutableSetOf()

        for (coverage in coverages) {
            ret.add(coverage.subject)
        }

        return ret
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val backToMain = Intent(this, MainActivity::class.java)
        startActivity(backToMain)
    }

}
