package nl.totowka.bridge.domain.interactor

import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Class to interact with [AuthRepository] from ViewModel.
 */
class ProfileInteractor @Inject constructor(private val authRepository: AuthRepository) {
    /**
     * Update the user data on DB.
     */
    fun updateProfile(profile: ProfileEntity) = authRepository.update(profile)

    /**
     * Add the user data to DB.
     */
    fun addProfile(profile: ProfileEntity) = authRepository.add(profile)

    /**
     * Delete the user data from DB.
     */
    fun deleteProfile(id: Int) = authRepository.delete(id)

    /**
     * Get the user data from DB.
     */
    fun getProfile(id: Int) = authRepository.get(id)

    /**
     * Check whether the user saved in DB.
     */
    fun isUser(id: Int) = authRepository.isUser(id)
}