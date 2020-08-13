package com.example.wasacon.vocabtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {
    private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()

        saveBtn.setOnClickListener {
            if (!wordEdit.text.isNullOrEmpty() && !meaningEdit.text.isNullOrEmpty()) {

                realm.executeTransaction {
                    val maxId = realm.where<Dictionary>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val dictionary = realm.createObject<Dictionary>(nextId)
                    dictionary.word = wordEdit.text.toString()
                    dictionary.meaning = meaningEdit.text.toString()
                }

            } else if (wordEdit.text.isNullOrEmpty() && meaningEdit.text.isNullOrEmpty()) {
                Toast.makeText(applicationContext, R.string.null_wm_err, Toast.LENGTH_SHORT).show()
            } else if (meaningEdit.text.isNullOrEmpty()) {
                Toast.makeText(applicationContext, R.string.null_word_err, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, R.string.null_meaning_err, Toast.LENGTH_SHORT).show()
            }

            wordEdit.text.clear()
            meaningEdit.text.clear()
        }

        completeBtn.setOnClickListener {
            finish()
        }

        checkBtn.setOnClickListener {
            val i_check = Intent(this, NoteActivity::class.java)
            startActivity(i_check)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
