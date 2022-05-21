package nl.totowka.bridge.presentation.events.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import nl.totowka.bridge.domain.interactor.EventInteractor
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.utils.scheduler.SchedulersProvider

class EventViewModel(
    private val eventInteractor: EventInteractor,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val eventsLiveData = MutableLiveData<List<EventEntity>>()
    private val disposables = CompositeDisposable()

    fun getAllEvents() {
        disposables.add(eventInteractor.getAllEvents()
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(eventsLiveData::setValue, errorLiveData::setValue)
        )
    }

    fun signUpForEvent(eventId: String, userId: String) {
        disposables.add(eventInteractor.addUserToEvent(eventId, userId)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(TAG, "completed signing for event!") }, errorLiveData::setValue)
        )
    }

    fun removeFromEvent(eventId: String, userId: String) {
        disposables.add(eventInteractor.deleteUser(eventId, userId)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                { Log.d(TAG, "completed deleting from event!") },
                errorLiveData::setValue
            )
        )
    }

    /**
     * Method clears disposables.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
    }

    fun getProgressLiveData(): LiveData<Boolean> = progressLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun getEventsLiveData(): LiveData<List<EventEntity>> = eventsLiveData

    companion object {
        private const val TAG = "EventViewModel"
    }
}