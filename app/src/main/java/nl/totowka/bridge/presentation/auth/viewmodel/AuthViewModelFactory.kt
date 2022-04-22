package nl.totowka.bridge.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.presentation.auth.viewmodel.AuthViewModel
import nl.totowka.bridge.utils.scheduler.SchedulersProvider

/**
 * Фабрика для ViewModel [ProfileViewModel]
 */
class AuthViewModelFactory(
    private val profileInteractor: ProfileInteractor,
    private val schedulersProvider: SchedulersProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return AuthViewModel(
            profileInteractor,
            schedulersProvider
        ) as T
    }
}
