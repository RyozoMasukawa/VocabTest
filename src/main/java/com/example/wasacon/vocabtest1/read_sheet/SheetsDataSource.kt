package com.example.wasacon.vocabtest1.read_sheet

import com.google.api.services.sheets.v4.model.Spreadsheet
import java.util.*
import io.reactivex.Observable
import io.reactivex.Single

interface SheetsDataSource {
    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Single<MutableList<Pair<Any, Any>>>
}