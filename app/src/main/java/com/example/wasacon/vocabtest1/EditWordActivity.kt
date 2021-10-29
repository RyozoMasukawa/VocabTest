package com.example.wasacon.vocabtest1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit_word.*
import kotlinx.android.synthetic.main.word.*
import java.lang.StringBuilder

class EditWordActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_word)
        realm = Realm.getDefaultInstance()

        meaningEdit.visibility = View.GONE

        word.text = intent.getStringExtra("word")
        meaning.text = intent.getStringExtra("meaning")
        meaningEdit.setText(intent.getStringExtra("meaning"))

        confirmBtn.visibility = View.INVISIBLE
        cancelBtn.visibility = View.INVISIBLE

        confirmBtn.setOnClickListener {

            val w = word.text.toString()

            realm.executeTransaction {
                realm.where<Dictionary>()
                    .equalTo("word", w)
                    ?.findAll()
                    ?.deleteAllFromRealm()
                val maxId = realm.where<Dictionary>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1L
                val corrected = realm.createObject<Dictionary>(nextId)
                corrected.word = w
                corrected.meaning = convertMeaning(meaningEdit.text.toString())
                corrected.category = intent.getLongExtra("category", 0L)
            }

            val msg = "「" +  word.text.toString() + "」" + getString(R.string.edit_complete)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            finish()
        }

        cancelBtn.setOnClickListener {
            meaningEdit.visibility = View.INVISIBLE
            meaning.visibility = View.VISIBLE

            editBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            confirmBtn.visibility = View.INVISIBLE
            cancelBtn.visibility = View.INVISIBLE
        }

        deleteBtn.setOnClickListener {
            realm.executeTransaction {
                val dictionary = realm.where<Dictionary>()
                    .equalTo("word", word.text.toString())
                    ?.findAll()
                    ?.deleteAllFromRealm()
            }
            Toast.makeText(applicationContext, R.string.delete_complete, Toast.LENGTH_SHORT).show()
            finish()
        }

        editBtn.setOnClickListener {
            meaningEdit.visibility = View.VISIBLE
            meaning.visibility = View.INVISIBLE
            editBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
            confirmBtn.visibility = View.VISIBLE
            cancelBtn.visibility = View.VISIBLE
        }
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

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
