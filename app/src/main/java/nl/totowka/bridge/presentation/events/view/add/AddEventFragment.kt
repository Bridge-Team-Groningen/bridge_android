package nl.totowka.bridge.presentation.events.view.add

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
import java.util.*
import javax.inject.Inject

/**
 * [Fragment] to display the events.
 *
 * @author Kocharyan Tigran
 */
class AddEventFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: FragmentAddEventBinding
    private lateinit var viewModel: EventViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var event = EventEntity(0)
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
        createViewModel()
        observeLiveData()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as LauncherActivity).isBottomNavVisible(false)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.save_event -> {
                if (binding.activity.isEmpty() or binding.title.isEmpty() or
                    binding.description.isEmpty() or binding.location.isEmpty() or
                    binding.maxCapacity.isEmpty() or binding.maxCapacity.text.toString().equals("0")
                    or binding.maxCapacity.text.toString().equals("1") or (dateTime?.equals(null) ?: true)) {
                    Snackbar.make(
                        binding.root,
                        "All the fields must be filled and max № of people must be at least 2",
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                } else
                    saveEvent()
            }
            R.id.back -> {
                exit()
            }
            R.id.pickDateTime -> {
                pickDate()
            }
        }
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, EventViewModelFactory(interactor, schedulers)
        ).get(EventViewModel::class.java)
    }

    private fun exit() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun saveEvent() {
        event.apply {
            this.activity = binding.activity.text()
            this.name = binding.title.text()
            this.description = binding.description.text()
            this.location = binding.location.text()
            this.maxCapacity = binding.maxCapacity.text().toInt()
            this.noOfParticipants = 0
            var dt = Date()
            val c: Calendar = Calendar.getInstance()
            c.setTime(dt)
            c.add(Calendar.DATE, 1)
            dt = c.getTime()
            this.date = dt
            viewModel.addEvent(this)
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
        binding.dateTime.text = "${day} + \"-\" + (${month + 1}) + \"-\" + ${year}"
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
        val result = LocalDateTime.of(year, month + 1, day, hour, minute)
        dateTime = convertToDateViaInstant(result)
        binding.dateTime.text = dateTime?.toCoolString()
        binding.dateTime.isVisible = true
        this.event.date = dateTime
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDateViaInstant(dateToConvert: LocalDateTime) = Date
        .from(
            dateToConvert.atZone(ZoneId.systemDefault())
                .toInstant()
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
        const val TAG = "AddEventFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Builder for [AddEventFragment]
         */
        fun newInstance() = AddEventFragment()
    }
}