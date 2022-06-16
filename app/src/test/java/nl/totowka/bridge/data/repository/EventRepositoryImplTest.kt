package nl.totowka.bridge.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import nl.totowka.bridge.data.api.EventService
import nl.totowka.bridge.data.api.LikeService
import nl.totowka.bridge.data.api.ProfileService
import nl.totowka.bridge.data.api.UserEventService
import nl.totowka.bridge.data.model.EventDataEntity
import nl.totowka.bridge.data.model.Like
import nl.totowka.bridge.data.model.ProfileDataEntity
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class EventRepositoryImplTest {
    private lateinit var eventRepositoryImpl: EventRepositoryImpl
    lateinit var profileService: ProfileService
    lateinit var userEventService: UserEventService
    lateinit var eventService: EventService
    lateinit var likeService: LikeService
    private val profileStub = ProfileEntity(
        name = "Tigran Kocharyan",
        googleId = "12345678",
        email = "tigrankocharyan9@gmail.com",
        age = 20,
        interestList = "soccer",
        gender = "Male",
        capacity = 5,
        city = "groningen",
        isLiked = true
    )
    private val eventStub = EventEntity(0)
    private val eventsStub = arrayListOf(eventStub)
    private val profilesStub = arrayListOf(profileStub)
    private val profileDataStub = ProfileDataEntity.fromEntity(profileStub)
    private val eventDataStub = EventDataEntity.fromEntity(eventStub)
    private val eventsDataStub = arrayListOf(eventDataStub)
    private val profilesDataStub = arrayListOf(profileDataStub)
    private val exception = IOException("")

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        profileService = mockk()
        eventService = mockk()
        userEventService = mockk()
        likeService = mockk()
        eventRepositoryImpl =
            EventRepositoryImpl(eventService, userEventService, profileService, likeService)
    }

    @Test
    fun `addEvent is successful`() {
        // Arrange
        every { eventService.addEvent(eventDataStub) } returns Single.just(eventDataStub)

        // Act && Assert
        eventRepositoryImpl.addEvent(eventStub).test().assertComplete()
        verify(exactly = 1) { eventService.addEvent(eventDataStub) }
    }

    @Test
    fun `addEvent is error`() {
        // Arrange
        every { eventService.addEvent(eventDataStub) } returns Single.error(exception)

        // Act && Assert
        eventRepositoryImpl.addEvent(eventStub).test().assertError(exception)
        verify(exactly = 1) { eventService.addEvent(eventDataStub) }
    }

    @Test
    fun `deleteEvent is successful`() {
        // Arrange
        every { eventService.deleteEvent(eventId) } returns Completable.complete()

        // Act && Assert
        eventRepositoryImpl.deleteEvent(eventId).test().assertComplete()
        verify(exactly = 1) { eventService.deleteEvent(eventId) }
    }

    @Test
    fun `deleteEvent is error`() {
        // Arrange
        every { eventService.deleteEvent(eventId) } returns Completable.error(exception)

        // Act && Assert
        eventRepositoryImpl.deleteEvent(eventId).test().assertError(exception)
        verify(exactly = 1) { eventService.deleteEvent(eventId) }
    }

    @Test
    fun `deleteUser is successful`() {
        // Arrange
        every { userEventService.deleteUser(eventId, userId) } returns Completable.complete()

        // Act && Assert
        eventRepositoryImpl.deleteUser(userId, eventId).test().assertComplete()
        verify(exactly = 1) { userEventService.deleteUser(eventId, userId) }
    }

    @Test
    fun `deleteUser is error`() {
        // Arrange
        every { userEventService.deleteUser(eventId, userId) } returns Completable.error(exception)

        // Act && Assert
        eventRepositoryImpl.deleteUser(userId, eventId).test().assertError(exception)
        verify(exactly = 1) { userEventService.deleteUser(eventId, userId) }
    }

    @Test
    fun `addUserToEvent is successful`() {
        // Arrange
        every { userEventService.addUserToEvent(eventId, userId) } returns Completable.complete()

        // Act && Assert
        eventRepositoryImpl.addUserToEvent(userId, eventId).test().assertComplete()
        verify(exactly = 1) { userEventService.addUserToEvent(eventId, userId) }
    }

    @Test
    fun `addUserToEvent is error`() {
        // Arrange
        every { userEventService.addUserToEvent(eventId, userId) } returns Completable.error(
            exception
        )

        // Act && Assert
        eventRepositoryImpl.addUserToEvent(userId, eventId).test().assertError(exception)
        verify(exactly = 1) { userEventService.addUserToEvent(eventId, userId) }
    }

    @Test
    fun `getAllEvents is successful`() {
        // Arrange
        every { eventService.getAllEvents() } returns Single.just(eventsDataStub)
        val expected = eventsStub
        expected.map { it.isSigned = false }

        // Act
        val actual = eventRepositoryImpl.getAllEvents().blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { eventService.getAllEvents() }
    }

    @Test
    fun `getAllEvents is failure`() {
        // Arrange
        every { eventService.getAllEvents() } returns Single.error(exception)

        // Assert
        eventRepositoryImpl.getAllEvents().test().assertError(exception)
        verify(exactly = 1) { eventService.getAllEvents() }
    }

    @Test
    fun `getSignedEvents is successful`() {
        // Arrange
        every { profileService.getSignedEvents(userId) } returns Single.just(eventsDataStub)
        val expected = eventsStub
        expected.map { it.isSigned = true }

        // Act
        val actual = eventRepositoryImpl.getSignedEvents(userId).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { profileService.getSignedEvents(userId) }
    }

    @Test
    fun `getSignedEvents is failure`() {
        // Arrange
        every { profileService.getSignedEvents(userId) } returns Single.error(exception)

        // Assert
        eventRepositoryImpl.getSignedEvents(userId).test().assertError(exception)
        verify(exactly = 1) { profileService.getSignedEvents(userId) }
    }

    @Test
    fun `getActivityEvents is successful`() {
        // Arrange
        every { eventService.getActivityEvents(activity) } returns Single.just(eventsDataStub)
        val expected = eventsStub

        // Act
        val actual = eventRepositoryImpl.getActivityEvents(activity).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { eventService.getActivityEvents(activity) }
    }

    @Test
    fun `getActivityEvents is failure`() {
        // Arrange
        every { eventService.getActivityEvents(activity) } returns Single.error(exception)

        // Assert
        eventRepositoryImpl.getActivityEvents(activity).test().assertError(exception)
        verify(exactly = 1) { eventService.getActivityEvents(activity) }
    }

    @Test
    fun `getUsersOfEvent is successful`() {
        // Arrange
        every { userEventService.getUsersOfEvent(eventId) } returns Single.just(profilesDataStub)
        every { likeService.match(userId, profileDataStub.googleId.toString()) } returns Single.just(Like(true))
        every { likeService.likes(userId, profileDataStub.googleId.toString()) } returns Single.just(Like(true))
        val expected = profilesStub

        // Act
        val actual = eventRepositoryImpl.getUsersOfEvent(eventId, userId).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { userEventService.getUsersOfEvent(eventId) }
    }

    @Test
    fun `getUsersOfEvent is failure`() {
        // Arrange
        every { userEventService.getUsersOfEvent(eventId) } returns Single.error(exception)
        every { likeService.match(userId, profileDataStub.googleId.toString()) } returns Single.just(Like(true))
        every { likeService.likes(userId, profileDataStub.googleId.toString()) } returns Single.just(Like(true))

        // Assert
        eventRepositoryImpl.getUsersOfEvent(eventId, userId).test().assertError(exception)
        verify(exactly = 1) { userEventService.getUsersOfEvent(eventId) }
    }

    companion object {
        private const val userId = "0"
        private const val eventId = "0"
        private const val activity = "activity"
    }
}