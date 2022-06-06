package nl.totowka.bridge.presentation.profile.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import nl.totowka.bridge.data.model.ProfileDataEntity
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import nl.totowka.bridge.utils.scheduler.SchedulersProviderImplStub
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: ProfileViewModel
    lateinit var profileInteractor: ProfileInteractor
    lateinit var schedulers: SchedulersProvider
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
    private val entityDataStub = ProfileDataEntity.fromEntity(entityStub)
    private val entitiesStub = arrayListOf(entityStub)
    private val exception = IOException("")
    private val id = "12345678"

    var profileObserver: Observer<ProfileEntity> = mockk()
    var profilesObserver: Observer<List<ProfileEntity>> = mockk()
    var progressObserver: Observer<Boolean> = mockk()
    var errorObserver: Observer<Throwable> = mockk()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        profileInteractor = mockk()
        schedulers = SchedulersProviderImplStub()

        every { profileObserver.onChanged(any()) } just Runs
        every { profilesObserver.onChanged(any()) } just Runs
        every { progressObserver.onChanged(any()) } just Runs
        every { errorObserver.onChanged(any()) } just Runs
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        viewModel = ProfileViewModel(profileInteractor, schedulers)
        viewModel.getProfileLiveData().observeForever(profileObserver)
        viewModel.getErrorLiveData().observeForever(errorObserver)
        viewModel.getProgressLiveData().observeForever(progressObserver)
        viewModel.getProfilesLiveData().observeForever(profilesObserver)
    }

    @Test
    fun `getProfile is success`() {
        every { profileInteractor.getProfile(id) } returns Single.just(entityStub)

        viewModel.getProfile(id)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            profileObserver.onChanged(entityStub)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.getProfile(id) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getProfile is error`() {
        every { profileInteractor.getProfile(id) } returns Single.error(exception)

        viewModel.getProfile(id)

        // The order in this sequence may be changing from time to time because the testing framework may be late to catch the changes.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.getProfile(id) }
    }

    @Test
    fun `getProfiles is success`() {
        every { profileInteractor.getProfiles(id) } returns Single.just(entitiesStub)

        viewModel.getProfiles(id)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            profilesObserver.onChanged(entitiesStub)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.getProfiles(id) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getProfiles is error`() {
        every { profileInteractor.getProfiles(id) } returns Single.error(exception)

        viewModel.getProfiles(id)

        // The order in this sequence may be changing from time to time because the testing framework may be late to catch the changes.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.getProfiles(id) }
    }

    @Test
    fun `updateProfile is success`() {
        every { profileInteractor.updateProfile(id, entityStub) } returns Completable.complete()

        viewModel.updateProfile(id, entityStub)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.updateProfile(id, entityStub) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `updateProfile is error`() {
        every { profileInteractor.updateProfile(id, entityStub) } returns Completable.error(exception)
        every { profileInteractor.addProfile(entityStub) } returns Completable.complete()

        viewModel.updateProfile(id, entityStub)

        // The progresses are called this way cause it first 'true' after updateProfile, then after addProfile functions.
        verifySequence {
            progressObserver.onChanged(true)
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.updateProfile(id, entityStub) }
        verify(exactly = 1) { profileInteractor.addProfile(entityStub) }
        verify{ errorObserver wasNot Called }
    }

    @Test
    fun `addProfile is success`() {
        every { profileInteractor.addProfile(entityStub) } returns Completable.complete()

        viewModel.addProfile(entityStub)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.addProfile(entityStub) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `addProfile is error`() {
        every { profileInteractor.addProfile(entityStub) } returns Completable.error(exception)

        viewModel.addProfile(entityStub)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.addProfile(entityStub) }
    }

    @Test
    fun `isUser is success`() {
        every { profileInteractor.isUser(id) } returns Single.just(true)

        viewModel.isUser(id)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.isUser(id) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `isUser is error`() {
        every { profileInteractor.isUser(id) } returns Single.error(exception)

        viewModel.isUser(id)

        // The order in this sequence may be changing from time to time because the testing framework may not be on time to catch the changing.
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { profileInteractor.isUser(id) }
    }
}