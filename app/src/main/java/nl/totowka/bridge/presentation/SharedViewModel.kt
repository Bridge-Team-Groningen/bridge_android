package nl.totowka.bridge.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity

class SharedViewModel : ViewModel() {
    private var _user = MutableLiveData(ProfileEntity())
    private var _matchedUser = MutableLiveData(ProfileEntity())
    private var _allEvents = MutableLiveData(emptyList<EventEntity>())
    private var _event = MutableLiveData(EventEntity(0))
    private var _editEvent = MutableLiveData(EventEntity(0))
    private var _signedEvents = MutableLiveData(emptyList<EventEntity>())
    private var _adapterPosition = MutableLiveData(0)

    var user: LiveData<ProfileEntity>? = _user
    var matchedUser: LiveData<ProfileEntity>? = _matchedUser
    var event: LiveData<EventEntity>? = _event
    var editEvent: LiveData<EventEntity>? = _editEvent
    var signedEvents: LiveData<List<EventEntity>>? = _signedEvents
    var allEvents: LiveData<List<EventEntity>>? = _allEvents
    var adapterPosition: LiveData<Int> = _adapterPosition

    fun setUser(user: ProfileEntity) {
        _user.value = user
    }

    fun setMatchedUser(user: ProfileEntity) {
        _matchedUser.value = user
    }

    fun setSignedEvents(events: List<EventEntity>) {
        _signedEvents.value = events
    }

    fun setAllEvents(events: List<EventEntity>) {
        _allEvents.value = events
    }

    fun setAdapterPosition(adapterPosition: Int) {
        _adapterPosition.value = adapterPosition
    }

    fun setEvent(event: EventEntity) {
        _event.value = event
    }

    fun setEditEvent(event: EventEntity) {
        _editEvent.value = event
    }
}