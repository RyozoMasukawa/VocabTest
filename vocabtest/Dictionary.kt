package com.example.wasacon.vocabtest

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Dictionary : RealmObject() {
    @PrimaryKey
    var id : Long = 0

    var word : String = ""

    var meaning : String = ""

    var category : Long = 0
}