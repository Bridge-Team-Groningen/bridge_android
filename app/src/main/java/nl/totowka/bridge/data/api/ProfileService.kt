package nl.totowka.bridge.data.api

import io.reactivex.Single
import nl.totowka.bridge.data.model.ProfileDataEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import android.R.string.no
import io.reactivex.Completable
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
     * @param googleID user's id
     */
    @GET("login/{googleId}")
    fun getUser(@Path("googleId") googleID: String): Single<ProfileDataEntity>

    /**
     * Method takes googleID to delete the user from db
     *
     * @param googleID user's id
     */
    @DELETE
    fun deleteUser(@Query("googleID") googleID: String): Completable

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
    @PUT("googleId/{googleId}")
    fun updateUser(@Path("googleId") googleId: String, @Body profile: ProfileDataEntity): Completable

    companion object {
        private const val POST_USER_URL = "."
    }
}