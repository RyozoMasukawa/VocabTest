//package com.example.wasacon.vocabtest1.read_sheet
//
//import android.util.Log
//import com.google.api.client.http.HttpTransport
//import com.google.api.client.json.JsonFactory
//import com.google.api.services.sheets.v4.Sheets
//import io.reactivex.Single
//import io.reactivex.Observable
//import io.reactivex.functions.Consumer
//
//class SheetsAPIDataSource(private val authManager: AuthenticationManager,
//                          private val transport : HttpTransport,
//                          private val jsonFactory: JsonFactory,
//                          private val APPLICATION_NAME : String
//) : SheetsDataSource {
//
//    private val sheetsAPI : Sheets
//        get() {
//            return Sheets.Builder(transport,
//                jsonFactory,
//                authManager.googleAccountCredential)
//                .setApplicationName("test")
//                .build()
//        }
//
//    override fun readSpreadSheet(spreadsheetId: String,
//                                 spreadsheetRange: String): Single<MutableList<Pair<Any, Any>>> {
//
//        /*val sheetsApi = Sheets.Builder(transport, jsonFactory, authManager.googleAccountCredential)
//            .setApplicationName(APPLICATION_NAME)
//            .build()*/
//
//        return Observable
//            .fromCallable {
//                Log.d("spread sheet reading", "started!")
//                val response = sheetsAPI.spreadsheets()
//                    .values()
//                    .get(spreadsheetId, spreadsheetRange)
//                    .execute()
//                response.getValues()
//            }.doOnNext(Consumer {
//                Log.d("Data stored", it.toString())
//            })
//            .map { it[0].zip(it[1]) }.flatMapIterable { it -> it }
//            .doOnNext(Consumer {
//                Log.d("Data stored", it.toString())
//            })
//            .toList()
//    }
//}