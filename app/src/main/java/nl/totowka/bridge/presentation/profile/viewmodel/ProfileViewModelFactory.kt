package nl.totowka.bridge.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.utils.scheduler.SchedulersProvider

/**
 * Фабрика для ViewModel [ProfileViewModel]
 */
class ProfileViewModelFactory(
    private val profileInteractor: ProfileInteractor,
    private val schedulersProvider: SchedulersProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return ProfileViewModel(
            profileInteractor,
            schedulersProvider
        ) as T
    }
}
