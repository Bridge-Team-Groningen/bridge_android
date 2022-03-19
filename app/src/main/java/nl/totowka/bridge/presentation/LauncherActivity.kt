package nl.totowka.bridge.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import nl.totowka.bridge.R
import nl.totowka.bridge.presentation.auth.view.AuthFragment

/**
 * Main Activity. All fragments will be added on top of it.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, AuthFragment.newInstance(), AuthFragment.TAG)
                .commit()
        }
    }
}