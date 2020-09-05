package com.example.wasacon.vocabtest1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wasacon.vocabtest1.handling_coverage.CoverageListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TEST_MODE = 1

    private val EDIT_MODE = 2

    private val NOTE_MODE = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputBtn.setOnClickListener {
            val input = Intent(this, CoverageListActivity::class.java)
                .putExtra("mode", EDIT_MODE)
            startActivity(input)
        }

        testBtn.setOnClickListener {
            val test = Intent(this, CoverageListActivity::class.java)
                .putExtra("mode", TEST_MODE)
            startActivity(test)
        }

        noteBtn.setOnClickListener {
            val note = Intent(this, CoverageListActivity::class.java)
                .putExtra("mode", NOTE_MODE)
            startActivity(note)
        }
    }

    override fun onBackPressed() {
    }
}
