package com.example.pula

import android.app.Application
import com.example.pula.di.module.appModule
import com.example.pula.di.module.repoModule
import com.example.pula.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }

    companion object {
        open val context = appModule

    }
}