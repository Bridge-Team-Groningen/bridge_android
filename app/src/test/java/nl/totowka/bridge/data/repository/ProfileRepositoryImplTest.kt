package nl.totowka.bridge.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.api.ProfileService
import nl.totowka.bridge.data.model.ProfileDataEntity.Companion.fromEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ProfileRepositoryImplTest {
    private lateinit var profileRepositoryImpl: ProfileRepositoryImpl
    lateinit var profileService: ProfileService
    private val entityStub = ProfileEntity(
        name = "Tigran Kocharyan",
        googleId = "12345678",
        email = "tigrankocharyan9@gmail.com",
        age = 20,
        interestList = "soccer",
        gender = "Male",
        capacity = 5,
        city = "groningen"
    )
    private val entityDataStub = fromEntity(entityStub)
    private val exception = IOException("")

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        profileService = mockk()
        profileRepositoryImpl = ProfileRepositoryImpl(profileService)
    }

    @Test
    fun `get is successful`() {
        // Arrange
        every { profileService.getUser(userId) } returns Single.just(entityDataStub)
        val expected = entityStub

        // Act
        val actual = profileRepositoryImpl.get(userId).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { profileService.getUser(userId) }
    }

    @Test
    fun `get is error`() {
        // Arrange
        every { profileService.getUser(userId) } throws exception

        // Act && Assert
        assertThrows(IOException::class.java) { profileRepositoryImpl.get(userId) }
        verify(exactly = 1) { profileService.getUser(userId) }
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
        private const val userId = "0"
        private const val eventId = "0"
    }
}