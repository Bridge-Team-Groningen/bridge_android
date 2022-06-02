package nl.totowka.bridge.presentation

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockkStatic
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class SharedViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: SharedViewModel
    private val profileStub = ProfileEntity(
        name = "Tigran Kocharyan",
        googleId = "12345678",
        email = "tigrankocharyan9@gmail.com",
        age = 20,
        interestList = "soccer",
        gender = "Male",
        capacity = 5,
        city = "groningen"
    )
    private val profilesStub = arrayListOf(profileStub)

    private val eventStub = EventEntity(0)
    private val eventsStub = arrayListOf(eventStub)

    private val position = 0

    private val exception = IOException("")
    private val id = "12345678"

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        viewModel = SharedViewModel()
    }

    @Test
    fun `setUser is consistent`() {
        // Arrange
        viewModel.setUser(profileStub)
        val expected = profileStub

        // Act
        val actual = viewModel.user!!.value

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `setAdapterPosition is consistent`() {
        // Arrange
        viewModel.setAdapterPosition(position)
        val expected = position

        // Act
        val actual = viewModel.adapterPosition.value

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `setAllEvents is consistent`() {
        // Arrange
        viewModel.setAllEvents(eventsStub)
        val expected = eventsStub

        // Act
        val actual = viewModel.allEvents!!.value

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `setSignedEvents is consistent`() {
        // Arrange
        viewModel.setSignedEvents(eventsStub)
        val expected = eventsStub

        // Act
        val actual = viewModel.signedEvents!!.value

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }
}