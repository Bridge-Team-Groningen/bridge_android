package nl.totowka.bridge.data.api

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.model.EventDataEntity
import nl.totowka.bridge.data.model.ProfileDataEntity
import retrofit2.http.*

/**
 * Event API for getting events from db.
 *
 * @author Tigran Kocharyan
 */
interface EventService {

    /**
     * Method calls API to get the activity events from db.
     */
    @GET("activity/{activity}")
    fun getActivityEvents(@Path("activity") activity: String): Single<List<EventDataEntity>>

    /**
     * Method calls API to get all events.
     */
    @GET(GET_ALL_EVENTS_URL)
    fun getAllEvents(): Single<List<EventDataEntity>>

    /**
     * Method to add event to db.
     *
     * @param event entity of event created by user
     */
    @POST(ADD_EVENT_URL)
    fun addEvent(@Body event: EventDataEntity): Single<EventDataEntity>

    /**
     * Method to delete event from db.
     *
     * @param id id of event created by user
     */
    @DELETE("{id}")
    fun deleteEvent(@Path("id") id: String): Completable

    /**
     * Method updates events on db
     *
     * @param event entity of event
     */
    @Headers("Content-Type: application/json")
    @PUT("{eventId}")
    fun updateUser(@Path("eventId") eventId: String, @Body event: EventDataEntity): Completable

    companion object {
        private const val GET_ALL_EVENTS_URL = "."
        private const val ADD_EVENT_URL = "."
    }
}