package nl.totowka.bridge.presentation.events.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.totowka.bridge.domain.interactor.EventInteractor
import nl.totowka.bridge.utils.scheduler.SchedulersProvider

class EventViewModelFactory(
    private val eventInteractor: EventInteractor,
    private val schedulersProvider: SchedulersProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return EventViewModel(
            eventInteractor,
            schedulersProvider
        ) as T
    }
}
