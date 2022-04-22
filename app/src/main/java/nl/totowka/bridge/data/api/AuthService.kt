package nl.totowka.bridge.data.api

import io.reactivex.Single
import nl.totowka.bridge.data.model.ProfileDataEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST

import android.R.string.no
import io.reactivex.Completable


/**
 * Auth API for logging in users.
 *
 * @author Tigran KocharyanT
 */
interface AuthService {

    /**
     * Method takes googleID to identify the user in db
     *
     * @param googleID user's id
     */
    @GET(GET_USER_URL)
    fun getUser(@Query("googleID") googleID: String): Single<ProfileDataEntity>

    /**
     * Method adds user's profile to db
     *
     * @param profile entity of user
     */
    @POST(ADD_USER_URL)
    fun addUser(@Body profile: ProfileDataEntity): Completable

    companion object {
        private const val GET_USER_URL = ""
        private const val ADD_USER_URL = "api/users/"
    }
}