package nl.totowka.bridge.data.api

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.model.EventDataEntity
import nl.totowka.bridge.data.model.ProfileDataEntity
import retrofit2.http.*


/**
 * Auth API for logging in users.
 *
 * @author Tigran Kocharyan
 */
interface ProfileService {
    /**
     * Method takes googleID to identify the user in db
     *
     * @param googleId user's id
     */
    @GET("{googleId}")
    fun getUser(@Path("googleId") googleId: String): Single<ProfileDataEntity>

    /**
     * Method returns all the users from db
     */
    @GET(GET_ALL_USERS_URL)
    fun getUsers(): Single<List<ProfileDataEntity>>

    /**
     * Method takes googleID to delete the user from db
     *
     * @param googleId user's id
     */
    @DELETE("{googleId}")
    fun deleteUser(@Path("googleId") googleId: String): Completable

    /**
     * Method adds user's profile to db
     *
     * @param profile entity of user
     */
    @POST(POST_USER_URL)
    fun addUser(@Body profile: ProfileDataEntity): Completable

    /**
     * Method updates user's profile on db
     *
     * @param profile entity of user
     */
    @Headers("Content-Type: application/json")
    @PUT("{googleId}")
    fun updateUser(@Path("googleId") googleId: String, @Body profile: ProfileDataEntity): Completable

    @GET("savedevents/{googleId}")
    fun getSignedEvents(@Path("googleId") googleId: String): Single<List<EventDataEntity>>

    companion object {
        private const val POST_USER_URL = "."
        private const val GET_ALL_USERS_URL = "."
        private const val DELETE_USER_URL = "."
    }
}