package nl.totowka.bridge.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.model.Like
import nl.totowka.bridge.domain.model.ProfileEntity

/**
 * Interface to work with Auth API.
 */
interface ProfileRepository {
    /**
     * Update the [ProfileEntity] on DB.
     */
    fun update(googleId: String, profile: ProfileEntity): Completable

    /**
     * Add the [ProfileEntity] to DB.
     */
    fun add(profile: ProfileEntity): Completable

    /**
     * Delete the [ProfileEntity] from DB.
     */
    fun delete(googleId: String): Completable

    /**
     * Get the [ProfileEntity] from DB.
     */
    fun get(googleId: String): Single<ProfileEntity>

    /**
     * Get the [ProfileEntity] from DB.
     */
    fun getAll(userId: String): Single<List<ProfileEntity>>

    /**
     * Check whether the [ProfileEntity] saved in DB.
     */
    fun isUser(googleId: String): Single<Boolean>

    /**
     * Likes of users
     */
    fun like(user1Id: String, user2Id: String): Completable

    /**
     * Likes of users
     */
    fun unlike(user1Id: String, user2Id: String): Completable

    /**
     * Likes of users
     */
    fun match(user1Id: String, user2Id: String): Single<Like>
}