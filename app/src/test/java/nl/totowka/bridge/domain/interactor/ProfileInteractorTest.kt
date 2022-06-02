package nl.totowka.bridge.domain.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.repository.ProfileRepositoryImpl
import nl.totowka.bridge.domain.model.ProfileEntity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ProfileInteractorTest {
    lateinit var profileRepositoryImpl: ProfileRepositoryImpl
    lateinit var profileInteractor: ProfileInteractor
    private val exception = IOException("")
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

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        profileRepositoryImpl = mockk()
        profileInteractor = ProfileInteractor(profileRepositoryImpl)
    }

    @Test
    fun `getProfile is successful`() {
        // Arrange
        every { profileRepositoryImpl.get(id) } returns Single.just(entityStub)
        val expected = entityStub

        // Act
        val actual = profileInteractor.getProfile(id).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { profileRepositoryImpl.get(id)}
    }

    @Test
    fun `getProfile is failure`() {
        // Arrange
        every { profileRepositoryImpl.get(id) } returns Single.error(exception)

        // Assert
        profileInteractor.getProfile(id).test().assertError(exception)
        verify(exactly = 1) { profileRepositoryImpl.get(id) }
    }

    @Test
    fun `addProfile is successful`() {
        // Arrange
        every { profileRepositoryImpl.add(entityStub) } returns Completable.complete()

        // Assert
        profileInteractor.addProfile(entityStub).test().assertComplete()
        verify(exactly = 1) { profileRepositoryImpl.add(entityStub)}
    }

    @Test
    fun `addProfile is failure`() {
        // Arrange
        every { profileRepositoryImpl.add(entityStub) } returns Completable.error(exception)

        // Assert
        profileInteractor.addProfile(entityStub).test().assertError(exception)
        verify(exactly = 1) { profileRepositoryImpl.add(entityStub) }
    }

    @Test
    fun `updateProfile is successful`() {
        // Arrange
        every { profileRepositoryImpl.update(id, entityStub) } returns Completable.complete()

        // Assert
        profileInteractor.updateProfile(id, entityStub).test().assertComplete()
        verify(exactly = 1) { profileRepositoryImpl.update(id, entityStub)  }
    }

    @Test
    fun `updateProfile is failure`() {
        // Arrange
        every { profileRepositoryImpl.update(id, entityStub) } returns Completable.error(exception)

        // Assert
        profileInteractor.updateProfile(id, entityStub).test().assertError(exception)
        verify(exactly = 1) { profileRepositoryImpl.update(id, entityStub) }
    }

    @Test
    fun `deleteProfile is successful`() {
        // Arrange
        every { profileRepositoryImpl.delete(id) } returns Completable.complete()

        // Assert
        profileInteractor.deleteProfile(id).test().assertComplete()
        verify(exactly = 1) { profileRepositoryImpl.delete(id)  }
    }

    @Test
    fun `deleteProfile is failure`() {
        // Arrange
        every { profileRepositoryImpl.delete(id) } returns Completable.error(exception)

        // Assert
        profileInteractor.deleteProfile(id).test().assertError(exception)
        verify(exactly = 1) { profileRepositoryImpl.delete(id) }
    }

    @Test
    fun `isUser is successful`() {
        // Arrange
        every { profileRepositoryImpl.isUser(id) } returns Single.just(true)

        // Assert
        profileInteractor.isUser(id).test().assertComplete()
        verify(exactly = 1) { profileRepositoryImpl.isUser(id) }
    }

    @Test
    fun `isUser is failure`() {
        // Arrange
        every { profileRepositoryImpl.isUser(id) } returns Single.error(exception)

        // Assert
        profileInteractor.isUser(id).test().assertError(exception)
        verify(exactly = 1) { profileRepositoryImpl.isUser(id) }
    }

    companion object {
        private const val id = "0"
    }
}