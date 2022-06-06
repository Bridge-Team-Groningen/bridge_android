package nl.totowka.bridge.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.api.LikeService
import nl.totowka.bridge.data.api.ProfileService
import nl.totowka.bridge.data.model.Like
import nl.totowka.bridge.data.model.ProfileDataEntity.Companion.fromEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ProfileRepositoryImplTest {
    private lateinit var profileRepositoryImpl: ProfileRepositoryImpl
    lateinit var profileService: ProfileService
    lateinit var likeService: LikeService
    private val entityStub = ProfileEntity(
        name = "Tigran Kocharyan",
        googleId = "12345678",
        email = "tigrankocharyan9@gmail.com",
        age = 20,
        interestList = "soccer",
        gender = "Male",
        capacity = 5,
        city = "groningen",
        isLiked = null
    )
    private val like = Like(true)
    private val entityDataStub = fromEntity(entityStub)
    private val entitiesDataStub = arrayListOf(entityDataStub)
    private val entitiesStub = arrayListOf(entityStub)
    private val exception = IOException("")

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        profileService = mockk()
        likeService = mockk()
        profileRepositoryImpl = ProfileRepositoryImpl(profileService, likeService)
    }

    @Test
    fun `get is successful`() {
        // Arrange
        every { profileService.getUser(user1Id) } returns Single.just(entityDataStub)
        val expected = entityStub

        // Act
        val actual = profileRepositoryImpl.get(user1Id).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { profileService.getUser(user1Id) }
    }

    @Test
    fun `get is error`() {
        // Arrange
        every { profileService.getUser(user1Id) } throws exception

        // Act && Assert
        assertThrows(IOException::class.java) { profileRepositoryImpl.get(user1Id) }
        verify(exactly = 1) { profileService.getUser(user1Id) }
    }

    @Test
    fun `getAll is successful`() {
        // Arrange
        every { profileService.getUsers() } returns Single.just(entitiesDataStub)
        every { likeService.match(userId, entityDataStub.googleId.toString()) } returns Single.just(Like(true))
        every { likeService.likes(userId, entityDataStub.googleId.toString()) } returns Single.just(Like(true))
        val expected = entitiesStub

        // Act
        val actual = profileRepositoryImpl.getAll(userId).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { profileService.getUsers() }
    }

    @Test
    fun `getAll is error`() {
        // Arrange
        every { profileService.getUsers() } returns Single.error(exception)
        every { likeService.match(userId, entityDataStub.googleId.toString()) } returns Single.just(Like(true))
        every { likeService.likes(userId, entityDataStub.googleId.toString()) } returns Single.just(Like(true))

        // Act && Assert
        profileRepositoryImpl.getAll(user1Id).test().assertError(exception)
        verify(exactly = 1) { profileService.getUsers()  }
    }

    @Test
    fun `like is successful`() {
        // Arrange
        every { likeService.like(user1Id, user2Id) } returns Completable.complete()

        // Act && Assert
        profileRepositoryImpl.like(user1Id, user2Id).test().assertComplete()
        verify(exactly = 1) { likeService.like(user1Id, user2Id) }
    }

    @Test
    fun `like is error`() {
        // Arrange
        every { likeService.like(user1Id, user2Id) } returns Completable.error(exception)

        // Act && Assert
        profileRepositoryImpl.like(user1Id, user2Id).test().assertError(exception)
        verify(exactly = 1) { likeService.like(user1Id, user2Id) }
    }

    @Test
    fun `unlike is successful`() {
        // Arrange
        every { likeService.unlike(user1Id, user2Id) } returns Completable.complete()

        // Act && Assert
        profileRepositoryImpl.unlike(user1Id, user2Id).test().assertComplete()
        verify(exactly = 1) { likeService.unlike(user1Id, user2Id) }
    }

    @Test
    fun `unlike is error`() {
        // Arrange
        every { likeService.unlike(user1Id, user2Id) } returns Completable.error(exception)

        // Act && Assert
        profileRepositoryImpl.unlike(user1Id, user2Id).test().assertError(exception)
        verify(exactly = 1) { likeService.unlike(user1Id, user2Id) }
    }

    @Test
    fun `delete is successful`() {
        // Arrange
        every { profileService.deleteUser(userId) } returns Completable.complete()

        // Act && Assert
        profileRepositoryImpl.delete(userId).test().assertComplete()
        verify(exactly = 1) { profileService.deleteUser(userId) }
    }

    @Test
    fun `delete is error`() {
        // Arrange
        every { profileService.deleteUser(userId) } returns Completable.error(exception)

        // Act && Assert
        profileRepositoryImpl.delete(userId).test().assertError(exception)
        verify(exactly = 1) { profileService.deleteUser(userId) }
    }

    @Test
    fun `add is successful`() {
        // Arrange
        every { profileService.addUser(entityDataStub) } returns Completable.complete()

        // Act && Assert
        profileRepositoryImpl.add(entityStub).test().assertComplete()
        verify(exactly = 1) { profileService.addUser(entityDataStub) }
    }

    @Test
    fun `add is error`() {
        // Arrange
        every { profileService.addUser(entityDataStub) } returns Completable.error(exception)

        // Act && Assert
        profileRepositoryImpl.add(entityStub).test().assertError(exception)
        verify(exactly = 1) { profileService.addUser(entityDataStub) }
    }

    @Test
    fun `update is successful`() {
        // Arrange
        every { profileService.updateUser(userId, entityDataStub) } returns Completable.complete()

        // Act && Assert
        profileRepositoryImpl.update(userId, entityStub).test().assertComplete()
        verify(exactly = 1) { profileService.updateUser(userId, entityDataStub) }
    }

    @Test
    fun `update is error`() {
        // Arrange
        every { profileService.updateUser(userId, entityDataStub) } returns Completable.error(exception)

        // Act && Assert
        profileRepositoryImpl.update(userId, entityStub).test().assertError(exception)
        verify(exactly = 1) { profileService.updateUser(userId, entityDataStub) }
    }

    companion object {
        private const val user1Id = "0"
        private const val user2Id = "1"
        private const val userId = "2"
        private const val eventId = "0"
    }
}