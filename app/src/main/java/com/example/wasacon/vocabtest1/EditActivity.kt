package com.example.wasacon.vocabtest1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import java.lang.StringBuilder

class EditActivity : AppCompatActivity() {
    private lateinit var realm : Realm

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()

        checkImg.visibility = View.INVISIBLE

        warningTxt.visibility = View.INVISIBLE

        val handler = Handler()

        val switchVisibility = object : Runnable {
            override fun run() {
                checkImg.visibility = View.INVISIBLE
                saveBtn.visibility = View.VISIBLE
                warningTxt.visibility = View.INVISIBLE
            }
        }


        saveBtn.setOnClickListener {
            if (!wordEdit.text.isNullOrEmpty() && !meaningEdit.text.isNullOrEmpty()) {

                realm.executeTransaction {
                    val maxId = realm.where<Dictionary>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val dictionary = realm.createObject<Dictionary>(nextId)
                    val category = intent.getLongExtra("category", 0)
                    dictionary.word = wordEdit.text.toString()
                    dictionary.meaning = meaningEdit.text.toString()
                    dictionary.category = category
                }

                checkImg.visibility = View.VISIBLE
                saveBtn.visibility = View.INVISIBLE
                warningTxt.visibility = View.VISIBLE

                warningTxt.setTextColor(R.color.colorClear)
                warningTxt.setText(R.string.save_complete)

                handler.postDelayed(switchVisibility, 600)

                wordEdit.text.clear()
                meaningEdit.text.clear()
            } else if (wordEdit.text.isNullOrEmpty() && meaningEdit.text.isNullOrEmpty()) {
                warningTxt.visibility = View.VISIBLE
                warningTxt.setTextColor(R.color.colorAccent)
                warningTxt.setText(R.string.null_wm_err)
            } else if (meaningEdit.text.isNullOrEmpty()) {
                warningTxt.visibility = View.VISIBLE
                warningTxt.setTextColor(R.color.colorAccent)
                warningTxt.setText(R.string.null_meaning_err)
            } else {
                warningTxt.setText(R.string.null_word_err)
                warningTxt.setTextColor(R.color.colorAccent)
                warningTxt.visibility = View.VISIBLE
            }
        }

        completeBtn.setOnClickListener {
            val i_complete = Intent(this, MainActivity::class.java)
            startActivity(i_complete)
        }

        checkBtn.setOnClickListener {
            val i_check = Intent(this, NoteActivity::class.java)
                .putExtra("category", intent.getLongExtra("category", 0L))
            startActivity(i_check)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun convertMeaning(meaning : String) : String{
        val buf = StringBuilder()

        var no = 0

        var i = 0

        val meanings : MutableSet<String> = mutableSetOf()

        var tmp = StringBuilder()

        while (i < meaning.length) {
            while (i < meaning.length && !meaning[i].isLetter()) {
                i++
            }

            if (i == meaning.length) {
                break
            }

            while (i < meaning.length && meaning[i].isLetter()) {
                tmp.append(meaning[i])
                i++
            }

            meanings.add(tmp.toString())
            tmp.clear()
        }

        for (meaning in meanings) {
            buf.append((++no).toString() + "." + meaning + "\n")
        }

        return buf.toString()
    }

}
