package nl.totowka.bridge.data.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.api.LikeService
import nl.totowka.bridge.data.api.ProfileService
import nl.totowka.bridge.data.model.Like
import nl.totowka.bridge.data.model.ProfileDataEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Implementation of [ProfileRepository]
 */
class ProfileRepositoryImpl @Inject constructor(
    val profileService: ProfileService,
    val likeService: LikeService
) : ProfileRepository {
    override fun update(googleId: String, profile: ProfileEntity): Completable =
        profileService.updateUser(googleId, ProfileDataEntity.fromEntity(profile))


    override fun add(profile: ProfileEntity): Completable =
        profileService.addUser(ProfileDataEntity.fromEntity(profile))

    override fun delete(googleId: String): Completable =
        profileService.deleteUser(googleId)


    override fun get(googleId: String): Single<ProfileEntity> =
        profileService.getUser(googleId).map { it.toEntity() }

    override fun getAll(userId: String): Single<List<ProfileEntity>> =
        profileService.getUsers().map { list ->
            list.filter { it.googleId != userId }.map { user ->
                user.toEntity().apply {
                    likeService.likes(user1Id = userId, user2Id = user.googleId ?: "0")
                        .subscribe({
                            Log.d("TOTOWKA", "result is ${it}")
                            this.isLiked = it.message
                        }, { error ->
                            Log.d("TOTOWKA", error?.message ?: "")
                            this.isLiked = false
                        }).dispose()
                    if (this.profilePicture.equals("-"))
                        this.profilePicture = null
                }
            }
        }


    override fun isUser(googleId: String): Single<Boolean> {
        TODO("Not yet implemented")
    }

    override fun like(user1Id: String, user2Id: String): Completable =
        likeService.like(user1Id, user2Id)

    override fun unlike(user1Id: String, user2Id: String): Completable =
        likeService.unlike(user1Id, user2Id)

    override fun match(user1Id: String, user2Id: String): Single<Like> =
        likeService.match(user1Id, user2Id)
}