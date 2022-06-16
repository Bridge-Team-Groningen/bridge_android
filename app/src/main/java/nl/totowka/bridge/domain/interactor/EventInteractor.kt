package nl.totowka.bridge.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.domain.repository.EventRepository
import javax.inject.Inject

class EventInteractor @Inject constructor(private val eventRepository: EventRepository) {
    fun addEvent(event: EventEntity): Single<EventEntity> = eventRepository.addEvent(event)

    fun updateEvent(eventId: String, event: EventEntity): Completable =
        eventRepository.updateEvent(eventId, event)

    fun deleteEvent(id: String): Completable = eventRepository.deleteEvent(id)

    fun getAllEvents(): Single<List<EventEntity>> = eventRepository.getAllEvents()

    fun getSignedEvents(userId: String): Single<List<EventEntity>> =
        eventRepository.getSignedEvents(userId)

    fun getActivityEvents(activity: String): Single<List<EventEntity>> =
        eventRepository.getActivityEvents(activity)

    fun getUsersOfEvent(eventId: String, userId: String): Single<List<ProfileEntity>> =
        eventRepository.getUsersOfEvent(eventId, userId)

    fun deleteUser(eventId: String, userId: String): Completable =
        eventRepository.deleteUser(eventId, userId)

    fun addUserToEvent(eventId: String, userId: String): Completable =
        eventRepository.addUserToEvent(eventId, userId)
}