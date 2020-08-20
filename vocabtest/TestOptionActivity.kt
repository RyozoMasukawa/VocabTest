package com.example.wasacon.vocabtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_option.*

class TestOptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_option)

        all.setOnClickListener {
            intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }
    }
}
