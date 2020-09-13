package com.dsvag.currencyexchanger.data.utils

import android.app.Application
import android.content.Context

class App : Application() {
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        component = AppComponent(this)
    }
}

fun Context.getAppComponent(): AppComponent {
    return (applicationContext as App).component
}