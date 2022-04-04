package nl.totowka.bridge.presentation.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentEventsBinding
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.presentation.trending.adapter.TrendingEventsAdapter
import nl.totowka.bridge.utils.callback.EventClickListener

/**
 * [Fragment] to display the events.
 *
 * @author Kocharyan Tigran
 */
class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding
    private lateinit var adapter: TrendingEventsAdapter


    private var clickListener = object : EventClickListener {
        override fun onClick(event: EventEntity) {
            val wordDetailsBottomDialogFragment = EventDetailsBottomDialogFragment.newInstance(event)
            wordDetailsBottomDialogFragment.show(
                (activity as AppCompatActivity).supportFragmentManager,
                EventDetailsBottomDialogFragment.TAG
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
    }

    private fun createAdapter() {
        var sport1 = EventEntity(R.drawable.sport, "Gym bros", "Gym partners, Zernike", "10am", 3, true)
        var party1 = EventEntity(R.drawable.club, "Drink and Dance!", "Night Club at Martini Tower", "11am", 3, true)
        var events = arrayListOf(sport1, party1)
        adapter = TrendingEventsAdapter(events, clickListener)
        binding.events.layoutManager = LinearLayoutManager(context)
        binding.events.adapter = adapter
        binding.events.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    companion object {
        const val TAG = "EventsFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [EventsFragment]
         */
        fun newInstance() = EventsFragment()
    }
}