package nl.totowka.bridge.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import nl.totowka.bridge.presentation.LauncherActivity
import javax.inject.Singleton

/**
 * Common component fot the whole project.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
@Singleton
@Component(
    modules = [
        BindModule::class
    ]
)
interface AppComponent {
    fun inject(launcher: LauncherActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}