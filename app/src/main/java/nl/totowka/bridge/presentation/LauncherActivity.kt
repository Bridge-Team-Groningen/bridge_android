package nl.totowka.bridge.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import nl.totowka.bridge.R

/**
 * Main Activity. All fragments will be added on top of it.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        setContentView(R.layout.activity_main)
    }
}