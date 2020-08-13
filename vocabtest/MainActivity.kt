package com.example.wasacon.vocabtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputBtn.setOnClickListener {
            val input = Intent(this, InputActivity::class.java)
            startActivity(input)
        }

        testBtn.setOnClickListener {
            val test = Intent(this, TestOptionActivity::class.java)
            startActivity(test)
        }

        noteBtn.setOnClickListener {
            val note = Intent(this, NoteActivity::class.java)
            startActivity(note)
        }
    }

}
