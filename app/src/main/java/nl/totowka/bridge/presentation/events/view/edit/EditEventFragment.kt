package nl.totowka.bridge.presentation.events.view.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import nl.totowka.bridge.App
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentAddEventBinding
import nl.totowka.bridge.domain.interactor.EventInteractor
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.SharedViewModel
import nl.totowka.bridge.presentation.events.view.signed.EventsFragment
import nl.totowka.bridge.presentation.events.viewmodel.EventViewModel
import nl.totowka.bridge.presentation.events.viewmodel.EventViewModelFactory
import nl.totowka.bridge.presentation.profile.view.EditProfileFragment
import nl.totowka.bridge.utils.Common.isEmpty
import nl.totowka.bridge.utils.Common.text
import nl.totowka.bridge.utils.Common.toCoolString
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

/**
 * [Fragment] to display the events.
 *
 * @author Kocharyan Tigran
 */
class EditEventFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: FragmentAddEventBinding
    private lateinit var viewModel: EventViewModel
    private lateinit var event: EventEntity
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val calendar = Calendar.getInstance()
    private var year = calendar[Calendar.YEAR]
    private var month = calendar[Calendar.MONTH]
    private var day = calendar[Calendar.DAY_OF_MONTH]
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)
    private var dateTime: Date? = null

    @Inject
    lateinit var interactor: EventInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEventBinding.inflate(inflater, container, false)
        binding.saveEvent.setOnClickListener(this)
        binding.back.setOnClickListener(this)
        binding.pickDateTime.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        event = sharedViewModel.editEvent?.value ?: EventEntity(0)
        showEventDetails()
        createViewModel()
        observeLiveData()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as LauncherActivity).isBottomNavVisible(false)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.save_event -> {
                updateEvent()
            }
            R.id.back -> {
                exit()
            }
            R.id.pickDateTime -> {
                pickDate()
            }
        }
    }

    private fun showEventDetails() {
        binding.description.setText(event.description)
        binding.activity.setText(event.activity)
        binding.title.setText(event.name)
        binding.location.setText(event.location)
        binding.maxCapacity.setText(event.maxCapacity.toString())
        binding.dateTime.text = event.date?.toCoolString()
        binding.dateTime.isVisible = true
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, EventViewModelFactory(interactor, schedulers)
        ).get(EventViewModel::class.java)
    }

    private fun exit() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun updateEvent() {
        event.apply {
            this.activity = binding.activity.text()
            this.name = binding.title.text()
            this.description = binding.description.text()
            this.location = binding.location.text()
            this.maxCapacity = binding.maxCapacity.text().toInt()
            viewModel.updateEvent(event.id.toString(), this)
        }
    }

    private fun pickDate() {
        DatePickerDialog(
            requireContext(),
            this,
            year,
            month,
            day
        ).apply { datePicker.minDate = calendar.timeInMillis }.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.day = dayOfMonth
        binding.dateTime.text = "${dayOfMonth} + \"-\" + (${month + 1}) + \"-\" + ${year}"
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
        val result = ZonedDateTime.of(
            year, month + 1, day, hour, minute, 0, 0,
            ZoneId.systemDefault()
        )
        dateTime = convertToDateViaInstant(result)
        binding.dateTime.text = dateTime?.toCoolString()
        binding.dateTime.isVisible = true
        this.event.date = dateTime
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDateViaInstant(dateToConvert: ZonedDateTime) = Date
        .from(
            dateToConvert.toInstant()
        )

    private fun showProgress(isVisible: Boolean) {
        Log.i(EventsFragment.TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(EditProfileFragment.TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(
            binding.root,
            throwable.toString(),
            BaseTransientBottomBar.LENGTH_SHORT
        )
            .show()
    }

    private fun processEvent(event: EventEntity) {
        sharedViewModel.user?.value?.googleId?.let {
            viewModel.signUpForEvent(
                event.id.toString(),
                it
            )
        }
        exit()
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getEventLiveData().observe(viewLifecycleOwner, this::processEvent)
    }

    companion object {
        const val TAG = "EditEventFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Builder for [EditEventFragment]
         */
        fun newInstance() = EditEventFragment()
    }
}