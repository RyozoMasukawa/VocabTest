package com.example.wasacon.vocabtest1.read_sheet
import android.util.Log
import com.example.wasacon.vocabtest1.read_sheet.ReadSpreadSheetContract.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class ReadSpreadsheetPresenter(private val view : ReadSpreadSheetContract.View,
                               private val authenticationManager: AuthenticationManager,
                               private val sheetsRepository : SheetsRepository,
                               spreadSheetId: String) : Presenter {

    private val sSheetId = spreadSheetId
    private lateinit var readSpreadSheetDisposable : Disposable
    private var dictMap : MutableMap<String, String> = mutableMapOf()

    override fun startAuthentication() {
        view.launchAuthentication(authenticationManager.googleSignInClient)
    }

    override fun init() {
        startAuthentication()
        view.setRealm(dictMap)
    }

    override fun loginFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loginSuccessful() {
        //view.showName(authenticationManager.getLastSignedAccount()?.displayName!!)
        authenticationManager.setUpGoogleAccountCredential()
        startReadingSpreadsheet(sSheetId, range)
    }

    override fun dispose() {
        readSpreadSheetDisposable.dispose()
    }

    private fun startReadingSpreadsheet(spreadSheetId : String, range : String) {
        dictMap.clear()

        readSpreadSheetDisposable =
            sheetsRepository.readSpreadSheet(spreadSheetId, range)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { view.showError("Error!") }
                .subscribe(Consumer {
                    val pairs = it
                    //pairs.map { dictMap[it.first.toString()] = dictMap[it.second.toString()]}
                    Log.d("Sheet :", it.toString())
                })
    }

    companion object {
        val range = "シート1"
    }
}