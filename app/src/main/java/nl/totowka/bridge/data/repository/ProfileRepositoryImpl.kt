package nl.totowka.bridge.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.api.ProfileService
import nl.totowka.bridge.data.model.ProfileDataEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Implementation of [ProfileRepository]
 */
class ProfileRepositoryImpl @Inject constructor(val auth: ProfileService) : ProfileRepository {
    override fun update(googleId: String, profile: ProfileEntity): Completable =
        auth.updateUser(googleId, ProfileDataEntity.fromEntity(profile))


    override fun add(profile: ProfileEntity): Completable =
        auth.addUser(ProfileDataEntity.fromEntity(profile))

    override fun delete(googleId: String): Completable =
        auth.deleteUser(googleId)


    override fun get(googleId: String): Single<ProfileEntity> =
        auth.getUser(googleId).map { it.toEntity() }


    override fun isUser(googleId: String): Single<Boolean> {
        TODO("Not yet implemented")
    }
}