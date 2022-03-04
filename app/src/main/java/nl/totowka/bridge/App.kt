package nl.totowka.bridge

import android.app.Application
import nl.totowka.bridge.di.AppComponent
import nl.totowka.bridge.di.DaggerAppComponent

/**
 * Basic application to support DI with application context.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
class App() : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(applicationContext).build()
    }

    fun getAppComponent() = appComponent
}
