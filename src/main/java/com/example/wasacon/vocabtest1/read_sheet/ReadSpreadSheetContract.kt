package com.example.wasacon.vocabtest1.read_sheet

import com.google.android.gms.auth.api.signin.GoogleSignInClient

interface ReadSpreadSheetContract {
    interface View : BaseView{
        fun setRealm(dictMap: MutableMap<String, String>)
        fun launchAuthentication(client : GoogleSignInClient)
        fun showName(username : String)
    }

    interface Presenter : BasePresenter {
        fun startAuthentication()
        fun loginSuccessful()
        fun loginFailed()
    }
}