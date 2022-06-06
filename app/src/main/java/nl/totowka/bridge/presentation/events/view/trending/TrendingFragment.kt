package nl.totowka.bridge.presentation.events.view.trending

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import nl.totowka.bridge.App
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentTrendingBinding
import nl.totowka.bridge.domain.interactor.EventInteractor
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.SharedViewModel
import nl.totowka.bridge.presentation.auth.view.AuthFragment
import nl.totowka.bridge.presentation.events.adapter.EventsAdapter
import nl.totowka.bridge.presentation.events.view.signed.EventDetailsBottomDialogFragment
import nl.totowka.bridge.presentation.events.view.signed.EventsFragment
import nl.totowka.bridge.presentation.events.viewmodel.EventViewModel
import nl.totowka.bridge.presentation.events.viewmodel.EventViewModelFactory
import nl.totowka.bridge.utils.Common.color
import nl.totowka.bridge.utils.callback.EventClickListener
import nl.totowka.bridge.utils.callback.SignInClickListener
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import javax.inject.Inject

/**
 * [Fragment] to display the trending events.
 *
 * @author Kocharyan Tigran
 */
class TrendingFragment : Fragment() {
    private lateinit var binding: FragmentTrendingBinding
    private lateinit var viewModel: EventViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: EventsAdapter

    @Inject
    lateinit var interactor: EventInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private var clickListener = object : EventClickListener {
        override fun onClick(event: EventEntity, position: Int) {
            val wordDetailsBottomDialogFragment =
                EventDetailsBottomDialogFragment.newInstance(signInListener)
            sharedViewModel.setAdapterPosition(position)
            sharedViewModel.setEvent(event)
            wordDetailsBottomDialogFragment.show(
                (activity as AppCompatActivity).supportFragmentManager,
                EventDetailsBottomDialogFragment.TAG
            )
        }
    }

    private var signInListener = object : SignInClickListener {
        override fun onClick(event : EventEntity, position: Int) {
            val user = sharedViewModel.user?.value
            user?.googleId?.let {
                if(event.isSigned == true) {
                    viewModel.removeFromEvent(event.id.toString(), it)
                } else {
                    viewModel.signUpForEvent(event.id.toString(), it)
                }
            }
            adapter.events.remove(event)
            adapter.notifyItemRemoved(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        observeLiveData()
        createAdapter()
        createRefresher()
        (activity as LauncherActivity).isBottomNavVisible(true)
    }

    private fun createRefresher() {
        binding.refresher.isRefreshing = true
        binding.refresher.setColorSchemeColors(context?.color(R.color.purple) ?: Color.MAGENTA)
        binding.refresher.setOnRefreshListener {
            viewModel.getAllEvents()
        }
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, EventViewModelFactory(interactor, schedulers)
        ).get(EventViewModel::class.java)
    }

    private fun createAdapter() {
        adapter = EventsAdapter(ArrayList(), clickListener)
        binding.eventsList.layoutManager = LinearLayoutManager(context)
        binding.eventsList.adapter = adapter
        binding.eventsList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        viewModel.getAllEvents()
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(EventsFragment.TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(AuthFragment.TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showEvents(events: List<EventEntity>) {
        adapter.updateData(ArrayList(events))
        binding.refresher.isRefreshing = false
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getEventsLiveData().observe(viewLifecycleOwner, this::showEvents)
    }

    companion object {
        const val TAG = "TrendingFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [TrendingFragment]
         */
        fun newInstance() = TrendingFragment()
    }
}