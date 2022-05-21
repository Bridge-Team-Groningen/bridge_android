package nl.totowka.bridge.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity

interface EventRepository {
    fun addEvent(event: EventEntity): Completable
    fun deleteEvent(id: String): Completable
    fun getAllEvents(): Single<List<EventEntity>>
    fun getActivityEvents(activity: String): Single<List<EventEntity>>
    fun getUsersOfEvent(eventId: String): Single<List<ProfileEntity>>
    fun deleteUser(eventId: String, userId: String): Completable
    fun addUserToEvent(eventId: String, userId: String): Completable
}