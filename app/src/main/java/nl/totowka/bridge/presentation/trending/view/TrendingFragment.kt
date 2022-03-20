package nl.totowka.bridge.presentation.trending.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentTrendingBinding
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.presentation.events.EventDetailsBottomDialogFragment
import nl.totowka.bridge.presentation.trending.adapter.TrendingEventsAdapter
import nl.totowka.bridge.utils.callback.EventClickListener

/**
 * Фрагмент, отвечающий за экран изучения слов.
 */
class TrendingFragment : Fragment() {
    private lateinit var binding: FragmentTrendingBinding
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
        binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
    }

    private fun createAdapter() {
        var sport1 = EventEntity(R.drawable.sport, "Gym bros","Gym partners, Zernike", "10am", 3)
        var sport2 = EventEntity(R.drawable.sport, "Squash bros", "Squash championship, Zernike", "6pm", 10)
        var sports = arrayListOf(sport1, sport2)
        adapter = TrendingEventsAdapter(sports, clickListener)
        binding.sportEvents.layoutManager = LinearLayoutManager(context)
        binding.sportEvents.adapter = adapter
        binding.sportEvents.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        var party1 = EventEntity(R.drawable.club, "Drink and Dance!", "Night Club at Martini Tower", "11am", 3)
        var party2 = EventEntity(R.drawable.club, "Sing with us!", "Karaoke", "10pm", 4)
        var parties = arrayListOf(party1, party2)
        adapter = TrendingEventsAdapter(parties, clickListener)
        binding.partyEvents.layoutManager = LinearLayoutManager(context)
        binding.partyEvents.adapter = adapter
        binding.partyEvents.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
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