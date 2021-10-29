package com.example.wasacon.vocabtest1.read_sheet

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<P : BasePresenter> : AppCompatActivity() {

    lateinit var presenter: P

    //abstract fun initDependencies()
}