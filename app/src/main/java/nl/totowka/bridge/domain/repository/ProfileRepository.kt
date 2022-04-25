package nl.totowka.bridge.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
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
     * Check whether the [ProfileEntity] saved in DB.
     */
    fun isUser(googleId: String): Single<Boolean>
}