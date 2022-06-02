package nl.totowka.bridge.data.api

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.model.ProfileDataEntity
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserEventService {
    /**
     * Method gets all users who participate in the event
     */
    @GET("participants/{eventId}")
    fun getUsersOfEvent(@Path("eventId") eventId: String): Single<List<ProfileDataEntity>>

    /**
     * Method to delete the user from event in db
     *
     * @param eventId event's id
     * @param userId userId's id
     */
    @DELETE("{eventId}/{userId}")
    fun deleteUser(
        @Path("eventId") eventId: String,
        @Path("userId") userId: String
    ): Completable

    /**
     * Method to add user to event in db.
     *
     * @param eventId event's id
     * @param userId userId's id
     */
    @POST("{eventId}/{userId}")
    fun addUserToEvent(
        @Path("eventId") eventId: String,
        @Path("userId") userId: String
    ): Completable
}