package nl.totowka.bridge.domain.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.repository.EventRepositoryImpl
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class EventInteractorTest {
    lateinit var eventRepositoryImpl: EventRepositoryImpl
    lateinit var eventInteractor: EventInteractor
    private val exception = IOException("")
    private val eventId = "0"
    private val userId = "0"
    private val entityStub = EventEntity(eventId.toInt())
    private val userStub = ProfileEntity(
        name = "Tigran Kocharyan",
        googleId = "12345678",
        email = "tigrankocharyan9@gmail.com",
        age = 20,
        interestList = "soccer",
        gender = "Male",
        capacity = 5,
        city = "groningen"
    )
    private val entitiesStub = arrayListOf(entityStub)
    private val usersStub = arrayListOf(userStub)

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        eventRepositoryImpl = mockk()
        eventInteractor = EventInteractor(eventRepositoryImpl)
    }

    @Test
    fun `addEvent is successful`() {
        // Arrange
        every { eventRepositoryImpl.addEvent(entityStub) } returns Single.just(entityStub)

        // Assert
        eventInteractor.addEvent(entityStub).test().assertComplete()
        verify(exactly = 1) { eventRepositoryImpl.addEvent(entityStub) }
    }

    @Test
    fun `addEvent is failure`() {
        // Arrange
        every { eventRepositoryImpl.addEvent(entityStub) } returns Single.error(exception)

        // Assert
        eventInteractor.addEvent(entityStub).test().assertError(exception)
        verify(exactly = 1) { eventRepositoryImpl.addEvent(entityStub) }
    }

    @Test
    fun `deleteEvent is successful`() {
        // Arrange
        every { eventRepositoryImpl.deleteEvent(eventId) } returns Completable.complete()

        // Assert
        eventInteractor.deleteEvent(eventId).test().assertComplete()
        verify(exactly = 1) { eventRepositoryImpl.deleteEvent(eventId) }
    }

    @Test
    fun `deleteEvent is failure`() {
        // Arrange
        every { eventRepositoryImpl.deleteEvent(eventId) } returns Completable.error(exception)

        // Assert
        eventInteractor.deleteEvent(eventId).test().assertError(exception)
        verify(exactly = 1) { eventRepositoryImpl.deleteEvent(eventId) }
    }

    @Test
    fun `deleteUser is successful`() {
        // Arrange
        every { eventRepositoryImpl.deleteUser(eventId, userId) } returns Completable.complete()

        // Assert
        eventInteractor.deleteUser(eventId, userId).test().assertComplete()
        verify(exactly = 1) { eventRepositoryImpl.deleteUser(eventId, userId) }
    }

    @Test
    fun `deleteUser is failure`() {
        // Arrange
        every { eventRepositoryImpl.deleteUser(eventId, userId) } returns Completable.error(exception)

        // Assert
        eventInteractor.deleteUser(eventId, userId).test().assertError(exception)
        verify(exactly = 1) { eventRepositoryImpl.deleteUser(eventId, userId) }
    }

    @Test
    fun `addUserToEvent is successful`() {
        // Arrange
        every { eventRepositoryImpl.addUserToEvent(eventId, userId) } returns Completable.complete()

        // Assert
        eventInteractor.addUserToEvent(eventId, userId).test().assertComplete()
        verify(exactly = 1) { eventRepositoryImpl.addUserToEvent(eventId, userId) }
    }

    @Test
    fun `addUserToEvent is failure`() {
        // Arrange
        every { eventRepositoryImpl.addUserToEvent(eventId, userId) } returns Completable.error(exception)

        // Assert
        eventInteractor.addUserToEvent(eventId, userId).test().assertError(exception)
        verify(exactly = 1) { eventRepositoryImpl.addUserToEvent(eventId, userId) }
    }

    @Test
    fun `getAllEvents is successful`() {
        // Arrange
        every { eventRepositoryImpl.getAllEvents() } returns Single.just(entitiesStub)
        val expected = entitiesStub

        // Act
        val actual = eventInteractor.getAllEvents().blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { eventRepositoryImpl.getAllEvents()  }
    }

    @Test
    fun `getAllEvents is failure`() {
        // Arrange
        every { eventRepositoryImpl.getAllEvents() } returns Single.error(exception)

        // Assert
        eventInteractor.getAllEvents().test().assertError(exception)
        verify(exactly = 1) { eventRepositoryImpl.getAllEvents() }
    }

    @Test
    fun `getSignedEvents is successful`() {
        // Arrange
        every { eventRepositoryImpl.getSignedEvents(userId) } returns Single.just(entitiesStub)
        val expected = entitiesStub

        // Act
        val actual = eventInteractor.getSignedEvents(userId).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { eventRepositoryImpl.getSignedEvents(userId)  }
    }

    @Test
    fun `getSignedEvents is failure`() {
        // Arrange
        every { eventRepositoryImpl.getSignedEvents(userId) } returns Single.error(exception)

        // Assert
        eventInteractor.getSignedEvents(userId).test().assertError(exception)
        verify(exactly = 1) { eventRepositoryImpl.getSignedEvents(userId) }
    }

    @Test
    fun `getActivityEvents is successful`() {
        // Arrange
        every { eventRepositoryImpl.getActivityEvents(userId) } returns Single.just(entitiesStub)
        val expected = entitiesStub

        // Act
        val actual = eventInteractor.getActivityEvents(userId).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { eventRepositoryImpl.getActivityEvents(userId)  }
    }

    @Test
    fun `getActivityEvents is failure`() {
        // Arrange
        every { eventRepositoryImpl.getActivityEvents(userId) } returns Single.error(exception)

        // Assert
        eventInteractor.getActivityEvents(userId).test().assertError(exception)
        verify(exactly = 1) { eventRepositoryImpl.getActivityEvents(userId) }
    }

    @Test
    fun `getUsersOfEvent is successful`() {
        // Arrange
        every { eventRepositoryImpl.getUsersOfEvent(eventId, userId) } returns Single.just(usersStub)
        val expected = usersStub

        // Act
        val actual = eventInteractor.getUsersOfEvent(eventId, userId).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { eventRepositoryImpl.getUsersOfEvent(eventId, userId)  }
    }

    @Test
    fun `getUsersOfEvent is failure`() {
        // Arrange
        every { eventRepositoryImpl.getUsersOfEvent(eventId, userId) } returns Single.error(exception)

        // Assert
        eventInteractor.getUsersOfEvent(eventId, userId).test().assertError(exception)
        verify(exactly = 1) { eventRepositoryImpl.getUsersOfEvent(eventId, userId) }
    }
}