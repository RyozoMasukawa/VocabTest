package com.example.wasacon.vocabtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_test.*
import java.lang.StringBuilder

class TestActivity : AppCompatActivity() {
    private val RANGE = 3
    private lateinit var realm: Realm

    private lateinit var options : Array<Button>

    private lateinit var question : String

    private lateinit var wordSet : MutableSet<String>

    private lateinit var wordMap : MutableMap<String, MutableSet<String>>

    //private lateinit var resultMap : MutableMap<String, Boolean>

    private lateinit var wordArray : Array<String>

    private var numQuestions = 0

    private var score = 0

    private var correctIndex : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        realm = Realm.getDefaultInstance()

        resetTestResults()

        options = arrayOf(opt1, opt2, opt3, opt4)

        wordMap = getMapOfEachWords()

        var num = intent.getIntExtra("num", 0)

        if (num == 0) {
            num = wordSet.size
        }

        numQuestions = num

        initialize()

        imageView.visibility = View.GONE

        val handler = Handler()

        val nextQuestion = object : Runnable {
            override fun run() {
                wordSet.remove(question)
                num--
                if (num == 0) {
                    intentResult()
                    finish()
                }
                initialize()
                imageView.visibility = View.GONE
            }
        }

        for (i in 0..RANGE) {
            options[i].setOnClickListener {
                if (i == correctIndex) {
                    val correctRealmResults = realm.where<Dictionary>()
                        .equalTo("word", q.text.toString())
                        .findAll()

                    realm.executeTransaction {
                        for (result in correctRealmResults) {
                            result.isCorrect = true
                        }
                    }

                    imageView.setImageResource(R.drawable.mark_maru)
                    imageView.visibility = View.VISIBLE
                    score++
                    handler.postDelayed(nextQuestion, 300)
                } else {
                    val inCorrectRealmResults = realm.where<Dictionary>()
                        .equalTo("word", q.text.toString())
                        .findAll()
                    realm.executeTransaction {
                        for (result in inCorrectRealmResults) {
                            result.isCorrect = false
                        }
                    }
                    imageView.setImageResource(R.drawable.mark_batsu)
                    imageView.visibility = View.VISIBLE
                    handler.postDelayed(nextQuestion, 300)
                }
            }
        }
    }

    fun initialize() {
        val qArray = wordSet.toTypedArray()
        if (qArray.size != 0) {
            question = qArray[(0..(qArray.size - 1)).random()]
        }

        q.text = question

        val meaning = meaningString(wordMap[question] as MutableSet<String>)

        correctIndex = (0..RANGE).random()

        options[correctIndex].setText(meaning)

        val incorrectList = mutableListOf<String>()

        for (word in wordArray) {
            if (!word.equals(question)) {
                incorrectList.add(word)
            }
        }

        incorrectList.shuffle()

        var i = 0

        //TODO バグ　データ一個だけしか入力されてない時 arrayIndexOutOfBoundException
        //TODO　解決策 : デフォルトの単語データ用意する
        for (opt: Button in options) {
            if (opt != options[correctIndex]) {
                val m = meaningString(wordMap[incorrectList[i]] as MutableSet<String>)
                opt.text = m
            }
            i++
        }
    }



    private fun getMapOfEachWords() : MutableMap<String, MutableSet<String>> {
        val realmResult = realm.where(Dictionary::class.java)
            .findAll()

        wordSet = mutableSetOf()

        for (result in realmResult) {
            wordSet.add(result.word)
        }

        wordArray = wordSet.toTypedArray()

        val rmap : MutableMap<String, MutableSet<String>> = mutableMapOf()

        lateinit var meanings : RealmResults<Dictionary>
        lateinit var meaningSet : MutableSet<String>

        for (word in wordSet) {
            meanings = realm.where(Dictionary::class.java)
                .equalTo("word", word)
                .findAll()

            meaningSet = mutableSetOf()

            for (result in meanings) {
                meaningSet.add(result.meaning)
            }
            rmap.put(word, meaningSet)
        }

        return rmap
    }

    private fun meaningString(list : MutableSet<String>) : String {
        val buf = StringBuilder()

        var i = 0

        for (s in list) {
            if (i < list.size - 1) {
                buf.append(s + ", ")
            } else {
                buf.append(s)
            }
            i++
        }

        return buf.toString()
    }

    private fun intentResult() {
        /*var i = 0

        val resultArray : Array<Boolean?> = arrayOfNulls<Boolean>(resultMap.keys.size)

        for (key in resultMap.keys) {
            if (i < resultMap.keys.size) {
                resultArray[i] = resultMap[key]
            } else {
                break
            }
        }*/

        val intent = Intent(this, ResultActivity::class.java)
            .putExtra("score", score)
            .putExtra("questions", numQuestions)
            /*
            .putExtra("words", resultMap.keys.toTypedArray())
            .putExtra("results", resultArray)*/
        startActivity(intent)
    }

    private fun resetTestResults() {
        val realmResults = realm.where<Dictionary>()
            .findAll()
        realm.executeTransaction {
            for (result in realmResults) {
                result.isCorrect = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
