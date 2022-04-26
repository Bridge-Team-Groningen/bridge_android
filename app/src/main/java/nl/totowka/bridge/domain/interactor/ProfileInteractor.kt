package nl.totowka.bridge.domain.interactor

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
     * Check whether the user saved in DB.
     */
    fun isUser(id: String) = profileRepository.isUser(id)
}