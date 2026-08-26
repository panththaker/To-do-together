package com.jpt.todotogether

import android.app.Application
import com.jpt.todotogether.di.initKoin
import org.koin.android.ext.koin.androidContext

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

         initKoin {
             androidContext(this@SampleApplication)
         }
    }
}