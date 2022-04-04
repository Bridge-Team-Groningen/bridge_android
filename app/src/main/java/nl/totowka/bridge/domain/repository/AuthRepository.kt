package nl.totowka.bridge.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.domain.model.ProfileEntity

/**
 * Interface to work with Auth API.
 */
interface AuthRepository {
    /**
     * Update the [ProfileEntity] on DB.
     */
    fun update(profile: ProfileEntity): Completable

    /**
     * Add the [ProfileEntity] to DB.
     */
    fun add(profile: ProfileEntity): Completable

    /**
     * Delete the [ProfileEntity] from DB.
     */
    fun delete(id: Int): Completable

    /**
     * Get the [ProfileEntity] from DB.
     */
    fun get(id: Int): Single<ProfileEntity>

    /**
     * Check whether the [ProfileEntity] saved in DB.
     */
    fun isUser(id: Int): Single<Boolean>
}