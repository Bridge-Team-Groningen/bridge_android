package nl.totowka.bridge.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import nl.totowka.bridge.R
import nl.totowka.bridge.presentation.auth.view.AuthFragment
import nl.totowka.bridge.presentation.events.view.signed.EventsFragment
import nl.totowka.bridge.presentation.events.view.trending.TrendingFragment
import nl.totowka.bridge.presentation.profile.view.ProfileFragment
import nl.totowka.bridge.presentation.profile.view.UsersFragment

/**
 * Main [Activity]. All fragments will be added on top of it.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, AuthFragment.newInstance(), AuthFragment.TAG)
                .commit()
        }
        bottom_nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> setCurrentFragment(ProfileFragment.newInstance(), ProfileFragment.TAG)
                R.id.trending -> setCurrentFragment(TrendingFragment.newInstance(), TrendingFragment.TAG)
                R.id.events -> setCurrentFragment(EventsFragment.newInstance(), EventsFragment.TAG)
                R.id.users -> setCurrentFragment(UsersFragment.newInstance(), UsersFragment.TAG)
            }
            true
        }
    }

    fun isBottomNavVisible(isVisible: Boolean) {
        if(isVisible) {
            bottom_nav.visibility = View.VISIBLE
        } else {
            bottom_nav.visibility = View.GONE
        }
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String) =
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
}