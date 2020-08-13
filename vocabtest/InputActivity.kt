package com.example.wasacon.vocabtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_input.*

class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        i_manual.setOnClickListener {
            val i_edit = Intent(this, EditActivity::class.java)
            startActivity((i_edit))
        }
    }
}
