//package com.example.wasacon.vocabtest1.read_sheet
//
//import com.google.api.services.sheets.v4.model.Spreadsheet
//import io.reactivex.Observable
//import io.reactivex.Single
//
//class SheetsRepository(private val sheetsAPIDataSource: SheetsAPIDataSource) {
//    fun readSpreadSheet(spreadsheetId : String,
//                        spreadsheetRange : String): Single<MutableList<Pair<Any, Any>>> {
//        return sheetsAPIDataSource.readSpreadSheet(spreadsheetId, spreadsheetRange)
//    }
//}