package com.example.wasacon.vocabtest1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_option.*

class TestOptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_option)

        val category = intent.getLongExtra("category", 0L)

        all.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
                .putExtra("category", category)
            startActivity(intent)
        }

        ten.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
                .putExtra("category", category)
                .putExtra("numQuestions", 10)
            startActivity(intent)
        }

        twenty.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
                .putExtra("category", category)
                .putExtra("numQuestions", 20)
            startActivity(intent)
        }

        thirty.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
                .putExtra("category", category)
                .putExtra("numQuestions", 30)
            startActivity(intent)
        }

        fifty.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
                .putExtra("category", category)
                .putExtra("numQuestions", 50)
            startActivity(intent)
        }

        hundred.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
                .putExtra("category", category)
                .putExtra("numQuestions", 100)
            startActivity(intent)
        }
    }
}
