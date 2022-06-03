package nl.totowka.bridge.presentation.events.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import nl.totowka.bridge.data.model.EventDataEntity
import nl.totowka.bridge.domain.interactor.EventInteractor
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import nl.totowka.bridge.utils.scheduler.SchedulersProviderImplStub
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class EventViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: EventViewModel
    lateinit var eventInteractor: EventInteractor
    lateinit var schedulers: SchedulersProvider
    private val eventId = "0"
    private val userId = "0"
    private val entityStub = EventEntity(eventId.toInt())
    private val entitiesStub = arrayListOf(entityStub)
    private val entityDataStub = EventDataEntity.fromEntity(entityStub)
    private val exception = IOException("")

    var eventsObserver: Observer<List<EventEntity>> = mockk()
    var progressObserver: Observer<Boolean> = mockk()
    var errorObserver: Observer<Throwable> = mockk()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        eventInteractor = mockk()
        schedulers = SchedulersProviderImplStub()

        every { eventsObserver.onChanged(any()) } just Runs
        every { progressObserver.onChanged(any()) } just Runs
        every { errorObserver.onChanged(any()) } just Runs
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        viewModel = EventViewModel(eventInteractor, schedulers)
        viewModel.getEventsLiveData().observeForever(eventsObserver)
        viewModel.getErrorLiveData().observeForever(errorObserver)
        viewModel.getProgressLiveData().observeForever(progressObserver)
    }

    @Test
    fun `getAllEvents is success`() {
        every { eventInteractor.getAllEvents() } returns Single.just(entitiesStub)

        viewModel.getAllEvents()

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            eventsObserver.onChanged(entitiesStub)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.getAllEvents() }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getAllEvents is error`() {
        every { eventInteractor.getAllEvents() } returns Single.error(exception)

        viewModel.getAllEvents()

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.getAllEvents() }
        verify { eventsObserver wasNot Called }
    }

    @Test
    fun `getSignedEvents is success`() {
        every { eventInteractor.getSignedEvents(userId) } returns Single.just(entitiesStub)

        viewModel.getSignedEvents(userId)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            eventsObserver.onChanged(entitiesStub)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.getSignedEvents(userId) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getSignedEvents is error`() {
        every { eventInteractor.getSignedEvents(userId) } returns Single.error(exception)

        viewModel.getSignedEvents(userId)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.getSignedEvents(userId) }
        verify { eventsObserver wasNot Called }
    }

    @Test
    fun `signUpForEvent is success`() {
        every { eventInteractor.addUserToEvent(eventId, userId) } returns Completable.complete()

        viewModel.signUpForEvent(eventId, userId)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.addUserToEvent(eventId, userId) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `signUpForEvent is error`() {
        every { eventInteractor.addUserToEvent(eventId, userId) } returns Completable.error(exception)

        viewModel.signUpForEvent(eventId, userId)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.addUserToEvent(eventId, userId) }
    }

    @Test
    fun `removeFromEvent is success`() {
        every { eventInteractor.deleteUser(eventId, userId) } returns Completable.complete()

        viewModel.removeFromEvent(eventId, userId)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.deleteUser(eventId, userId) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `removeFromEvent is error`() {
        every { eventInteractor.deleteUser(eventId, userId) } returns Completable.error(exception)

        viewModel.removeFromEvent(eventId, userId)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.deleteUser(eventId, userId) }
    }

    @Test
    fun `addEvent is success`() {
        every { eventInteractor.addEvent(entityStub) } returns Single.just(entityStub)

        viewModel.addEvent(entityStub)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.addEvent(entityStub) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `addEvent is error`() {
        every { eventInteractor.addEvent(entityStub) } returns Single.error(exception)

        viewModel.addEvent(entityStub)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { eventInteractor.addEvent(entityStub) }
    }
}