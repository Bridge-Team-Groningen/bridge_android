package nl.totowka.bridge.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity

class SharedViewModel : ViewModel() {
    private var _user = MutableLiveData(ProfileEntity())
    private var _allEvents = MutableLiveData(emptyList<EventEntity>())
    private var _signedEvents = MutableLiveData(emptyList<EventEntity>())

    var user: LiveData<ProfileEntity>? = _user
    var signedEvents: LiveData<List<EventEntity>>? = _signedEvents
    var allEvents: LiveData<List<EventEntity>>? = _allEvents

    fun setUser(user: ProfileEntity) {
        _user.value = user
    }

    fun setSignedEvents(events: List<EventEntity>) {
        _signedEvents.value = events
    }

    fun setAllEvents(events: List<EventEntity>) {
        _allEvents.value = events
    }
}