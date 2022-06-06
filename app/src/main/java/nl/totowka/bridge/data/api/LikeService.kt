package nl.totowka.bridge.data.api

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.model.Like
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeService {
    @POST("{user1Id}/{user2Id}")
    fun like(@Path("user1Id") user1Id: String, @Path("user2Id") user2Id: String): Completable

    @DELETE("{user1Id}/{user2Id}")
    fun unlike(@Path("user1Id") user1Id: String, @Path("user2Id") user2Id: String): Completable

    @GET("match/{user1Id}/{user2Id}")
    fun match(@Path("user1Id") user1Id: String, @Path("user2Id") user2Id: String): Single<Like>

    @GET("isliker/{user1Id}/{user2Id}")
    fun likes(@Path("user1Id") user1Id: String, @Path("user2Id") user2Id: String): Single<Like>
}