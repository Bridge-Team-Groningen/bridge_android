package nl.totowka.bridge.domain.interactor

import io.reactivex.schedulers.Schedulers
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Class to interact with [ProfileRepository] from ViewModel.
 */
class ProfileInteractor @Inject constructor(private val profileRepository: ProfileRepository) {
    /**
     * Update the user data on DB.
     */
    fun updateProfile(id: String, profile: ProfileEntity) = profileRepository.update(id, profile)

    /**
     * Add the user data to DB.
     */
    fun addProfile(profile: ProfileEntity) = profileRepository.add(profile)

    /**
     * Delete the user data from DB.
     */
    fun deleteProfile(id: String) = profileRepository.delete(id)

    /**
     * Get the user data from DB.
     */
    fun getProfile(id: String) = profileRepository.get(id)

    /**
     * Get the users data from DB.
     */
    fun getProfiles(userId: String) = profileRepository.getAll(userId)

    /**
     * Check whether the user saved in DB.
     */
    fun isUser(id: String) = profileRepository.isUser(id)

    /**
     * Likes the user
     */
    fun like(user1Id: String, user2Id: String) = profileRepository.like(user1Id, user2Id)

    /**
     * Likes the user
     */
    fun unlike(user1Id: String, user2Id: String) = profileRepository.unlike(user1Id, user2Id)

    /**
     * Matched the user
     */
    fun match(user1Id: String, user2Id: String) =
        profileRepository.match(user1Id, user2Id).subscribeOn(
            Schedulers.io()
        ).blockingGet().message
}