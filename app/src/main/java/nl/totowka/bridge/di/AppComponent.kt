package nl.totowka.bridge.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.auth.view.AuthFragment
import nl.totowka.bridge.presentation.events.view.add.AddEventFragment
import nl.totowka.bridge.presentation.events.view.edit.EditEventFragment
import nl.totowka.bridge.presentation.events.view.signed.EventDetailsBottomDialogFragment
import nl.totowka.bridge.presentation.events.view.signed.EventsFragment
import nl.totowka.bridge.presentation.events.view.trending.TrendingFragment
import nl.totowka.bridge.presentation.profile.view.EditProfileFragment
import nl.totowka.bridge.presentation.profile.view.ProfileBottomDialogFragment
import nl.totowka.bridge.presentation.profile.view.ProfileFragment
import nl.totowka.bridge.presentation.profile.view.UsersFragment
import javax.inject.Singleton

/**
 * Common component fot the whole project.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
@Singleton
@Component(
    modules = [
        BindModule::class, RetrofitModule::class
    ]
)
interface AppComponent {
    fun inject(launcher: LauncherActivity)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: EventsFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: TrendingFragment)
    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: AddEventFragment)
    fun inject(fragment: EditEventFragment)
    fun inject(fragment: EventDetailsBottomDialogFragment)
    fun inject(fragment: ProfileBottomDialogFragment)
    fun inject(fragment: UsersFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}