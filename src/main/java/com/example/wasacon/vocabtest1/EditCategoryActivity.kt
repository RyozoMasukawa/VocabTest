package com.example.wasacon.vocabtest1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wasacon.vocabtest1.handling_coverage.Coverage
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit_category.*
import java.util.*

class EditCategoryActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)
        realm = Realm.getDefaultInstance()

        warningTxt.setText(R.string.step1)

        var isStep1 : Boolean = true

        nameEdit.visibility = View.INVISIBLE

        okBtn.setOnClickListener {
            if (isStep1) {
                if (subjectEdit.text.isNullOrEmpty()) {
                    warningTxt.setTextColor(getColor(R.color.colorAccent))
                } else {
                    warningTxt.setText(R.string.step2)
                    warningTxt.setTextColor(getColor(R.color.colorPrimary))
                    subjectEdit.visibility = View.INVISIBLE
                    isStep1 = false
                    nameEdit.visibility = View.VISIBLE
                }
            } else {
                if (nameEdit.text.isNullOrEmpty()) {
                    warningTxt.setTextColor(getColor(R.color.colorAccent))
                } else {
                    var nextId = 0L
                    realm.executeTransaction {
                        val maxId = realm.where<Coverage>().max("id")
                        nextId = (maxId?.toLong() ?: 0L) + 1L
                        val coverage = realm.createObject<Coverage>(nextId)
                        coverage.subject = subjectEdit.text.toString()
                        coverage.name = nameEdit.text.toString()
                        coverage.dateTime = Date()
                    }
                    val intent = Intent(this, InputActivity::class.java)
                        .putExtra("category", nextId)
                    startActivity(intent)
                }
            }
        }

    }
}
