package com.example.wasacon.vocabtest1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)
        val questions = intent.getIntExtra("questions", 1)
        val percentage = (100 * score.toDouble() / questions).roundToInt()
        scoreTxt.text = percentage.toString() + "%"

        finishBtn.setOnClickListener {
            finish()
        }

        detailBtn.setOnClickListener {
            val intentDetail = Intent(this, ResultListActivity::class.java)
                .putExtra("category", intent.getLongExtra("category", 0L))
            startActivity(intentDetail)
        }
    }
}
