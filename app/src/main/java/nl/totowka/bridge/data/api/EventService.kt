package nl.totowka.bridge.data.api

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.model.EventDataEntity
import nl.totowka.bridge.data.model.ProfileDataEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Event API for getting events from db.
 *
 * @author Tigran Kocharyan
 */
interface EventService {

    /**
     * Method calls API to get the current trending events from db.
     */
    @GET(GET_TRENDING_EVENTS_URL)
    fun getTrendingEvents(): Single<List<EventDataEntity>>

    /**
     * Method calls API to get the events that user has signed for.
     *
     * @param googleID user's id provided by google
     */
    @GET(GET_USER_EVENTS_URL)
    fun getUserEvents(@Query("googleID") googleID: String): Single<List<EventDataEntity>>

    /**
     * Method to add event to db.
     *
     * @param event entity of event created by user
     */
    @POST(ADD_EVENT_URL)
    fun addEvent(@Body event: EventDataEntity): Completable

    companion object {
        private const val BASE_URL = "HEROKU URL"
        private const val GET_USER_EVENTS_URL = ""
        private const val GET_TRENDING_EVENTS_URL = ""
        private const val ADD_EVENT_URL = ""
    }
}