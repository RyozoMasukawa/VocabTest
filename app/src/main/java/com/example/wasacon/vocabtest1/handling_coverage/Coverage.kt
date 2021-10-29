package com.example.wasacon.vocabtest1.handling_coverage

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Coverage : RealmObject() {
    @PrimaryKey
    var id : Long = 0

    var name : String = ""

    var subject : String = ""

    var dateTime : Date = Date()
}