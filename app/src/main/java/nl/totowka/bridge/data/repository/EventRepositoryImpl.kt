package nl.totowka.bridge.data.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.api.EventService
import nl.totowka.bridge.data.api.LikeService
import nl.totowka.bridge.data.api.ProfileService
import nl.totowka.bridge.data.api.UserEventService
import nl.totowka.bridge.data.model.EventDataEntity
import nl.totowka.bridge.data.model.ProfileDataEntity
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.domain.repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventService: EventService,
    private val userEventService: UserEventService,
    private val profileService: ProfileService,
    private val likeService: LikeService
) : EventRepository {
    override fun addEvent(event: EventEntity): Single<EventEntity> =
        eventService.addEvent(EventDataEntity.fromEntity(event)).map { it.toEntity() }

    override fun updateEvent(eventId: String, event: EventEntity): Completable =
        eventService.updateUser(eventId, EventDataEntity.fromEntity(event))

    override fun deleteEvent(id: String): Completable = eventService.deleteEvent(id)

    override fun getAllEvents(): Single<List<EventEntity>> =
        eventService.getAllEvents().map { list ->
            list.map { event ->
                event.toEntity().also { it.isSigned = false }
            }
        }

    override fun getSignedEvents(userId: String): Single<List<EventEntity>> =
        profileService.getSignedEvents(userId).map { list ->
            list.map { event ->
                event.toEntity().also { it.isSigned = true }
            }
        }

    override fun getActivityEvents(activity: String): Single<List<EventEntity>> =
        eventService.getActivityEvents(activity).map { list -> list.map { it.toEntity() } }

    override fun getUsersOfEvent(eventId: String, userId: String): Single<List<ProfileEntity>> =
        userEventService.getUsersOfEvent(eventId).map { list ->
            list.map {
                it.toEntity().apply {
                    likeService.likes(user1Id = userId, user2Id = this.googleId ?: "0")
                        .subscribe({
                            this.isLiked = it.message
                        }, { error ->
                            Log.d("getUsersOfEvent", error?.message ?: "")
                            this.isLiked = false
                        }).dispose()
                    if (this.profilePicture.equals("-"))
                        this.profilePicture = null
                }
            }
        }

    override fun deleteUser(eventId: String, userId: String): Completable =
        userEventService.deleteUser(eventId, userId)

    override fun addUserToEvent(eventId: String, userId: String): Completable =
        userEventService.addUserToEvent(eventId, userId)
}