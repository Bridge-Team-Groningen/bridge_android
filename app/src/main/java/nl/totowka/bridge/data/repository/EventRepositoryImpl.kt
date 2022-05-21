package nl.totowka.bridge.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.api.EventService
import nl.totowka.bridge.data.api.UserEventService
import nl.totowka.bridge.data.model.EventDataEntity
import nl.totowka.bridge.data.model.ProfileDataEntity
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.domain.repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventService: EventService,
    private val userEventService: UserEventService
) : EventRepository {
    override fun addEvent(event: EventEntity): Completable =
        eventService.addEvent(EventDataEntity.fromEntity(event))

    override fun deleteEvent(id: String): Completable = eventService.deleteEvent(id)

    override fun getAllEvents(): Single<List<EventEntity>> =
        eventService.getAllEvents().map { list -> list.map { it.toEntity() } }

    override fun getActivityEvents(activity: String): Single<List<EventEntity>> =
        eventService.getActivityEvents(activity).map { list -> list.map { it.toEntity() } }

    override fun getUsersOfEvent(eventId: String): Single<List<ProfileEntity>> =
        userEventService.getUsersOfEvent(eventId).map { list -> list.map { it.toEntity() } }

    override fun deleteUser(eventId: String, userId: String): Completable =
        userEventService.deleteUser(eventId, userId)

    override fun addUserToEvent(eventId: String, userId: String): Completable =
        userEventService.addUserToEvent(eventId, userId)
}