package com.potatomioo.split

import android.app.Application
import com.google.firebase.FirebaseApp

class SplitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}