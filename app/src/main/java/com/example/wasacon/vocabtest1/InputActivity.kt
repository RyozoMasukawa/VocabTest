package com.example.wasacon.vocabtest1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.wasacon.vocabtest1.read_sheet.*
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.Scopes
//import com.google.android.gms.common.api.Scope
//
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
//import com.google.api.client.http.javanet.NetHttpTransport
//import com.google.api.client.json.jackson2.JacksonFactory
//import com.google.api.client.util.ExponentialBackOff
//import com.google.api.services.sheets.v4.SheetsScopes
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_input.*
import java.util.*

class InputActivity : BaseActivity<ReadSpreadSheetContract.Presenter>(), ReadSpreadSheetContract.View{
    private lateinit var realm : Realm
    private lateinit var spreadSheetId : String

    private lateinit var dictMap : MutableMap<String, String>

    private val allowedCharSet : Set<Char> = setOf('-', ' ', '　', '(', ')', '（', '）', '〜', ',', '、', '/')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        realm = Realm.getDefaultInstance()

        sheetIdEdit.visibility = View.INVISIBLE

        i_manual.setOnClickListener {
            val i_edit = Intent(this, EditActivity::class.java)
                .putExtra("category", intent.getLongExtra("category", 0L))
            startActivity((i_edit))
        }

        var isInputBySheetButtonOncePressed : Boolean = false

        i_s_sheet.setOnClickListener {
            if (!isInputBySheetButtonOncePressed) {
                sheetIdEdit.visibility = View.VISIBLE
                i_manual.visibility = View.INVISIBLE
                isInputBySheetButtonOncePressed = true
                i_s_sheet.setText(R.string.check)
            } else {
                if (!sheetIdEdit.text.isNullOrEmpty()) {
                    dictMap = mutableMapOf()
                    inputMany(sheetIdEdit.text.toString())
                    setRealm(dictMap)
                    val backToMain = Intent(this, MainActivity::class.java)
                    startActivity(backToMain)
                    //spreadSheetId = urlToId(sheetIdEdit.text.toString())
                    //initDependencies()
                    //presenter.init()
                }
            }
            //readSheetData()
        }
    }

    /*
    override fun initDependencies() {
        val signInOptions : GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
                .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
        val googleAccountCredential = GoogleAccountCredential
            .usingOAuth2(this, Arrays.asList(*AuthenticationManager.SCOPES))
            .setBackOff(ExponentialBackOff())
        val authManager =
            AuthenticationManager(
                lazyOf(this),
                googleSignInClient,
                googleAccountCredential)
        val sheetsAPIDataSource =
            SheetsAPIDataSource(authManager,
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                getString(R.string.app_name))
        val sheetsRepository = SheetsRepository(sheetsAPIDataSource)
        presenter = ReadSpreadsheetPresenter(this, authManager, sheetsRepository, spreadSheetId)
    }*/

    override fun showError(error: String) {
        Log.d("OnError", error)
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    /*override fun launchAuthentication(client: GoogleSignInClient) {
        startActivityForResult(client.signInIntent, RQ_GOOGLE_SIGN_IN)
    }*/

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.loginSuccessful()
                presenter.dispose()
            } else {
                presenter.loginFailed()
            }
            val backToMain = Intent(this, MainActivity::class.java)
            startActivity(backToMain)
        }
    }*/

    override fun setRealm(dictMap: MutableMap<String, String>) {
        val category = intent.getLongExtra("category", 0)
        for (key in dictMap.keys) {
            if (dictMap[key] != null) {
                realm.executeTransaction {
                    val maxId = realm.where<Dictionary>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val dictionary = realm.createObject<Dictionary>(nextId)
                    dictionary.word = key
                    dictionary.meaning = dictMap[key].toString()
                    dictionary.category = category
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun showName(username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun urlToId(url : String) : String {
        val rv = url.substringAfter("https://docs.google.com/spreadsheets/d/").substringBefore("/")
        Toast.makeText(this, rv, Toast.LENGTH_SHORT).show()
        return rv
    }



    private fun inputMany(sheet : String) {
        var isWord : Boolean = true
        val word = StringBuilder()
        val meaning = StringBuilder()

        for (c in sheet) {
            if (c == '\t') {
                isWord = false
            } else if (c == '\n') {
                dictMap[word.toString()] = meaning.toString()
                isWord = true
                word.clear()
                meaning.clear()
            } else {
                if (isWord) {
                    word.append(c)
                } else {
                    meaning.append(c)
                }
            }
        }
    }

    companion object {
        const val TAG = "ReadSpreadsheetActivity"
        const val RQ_GOOGLE_SIGN_IN = 999
    }

}

//333222559660-fgec9bosn6oaogn8i49beq1bmdkabal5.apps.googleusercontent.com