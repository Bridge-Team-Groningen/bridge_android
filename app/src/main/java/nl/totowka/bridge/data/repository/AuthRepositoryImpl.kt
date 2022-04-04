package nl.totowka.bridge.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Implementation of [AuthRepository]
 */
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override fun update(profile: ProfileEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun add(profile: ProfileEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int): Completable {
        TODO("Not yet implemented")
    }

    override fun get(id: Int): Single<ProfileEntity> {
        TODO("Not yet implemented")
    }

    override fun isUser(id: Int): Single<Boolean> {
        TODO("Not yet implemented")
    }
}