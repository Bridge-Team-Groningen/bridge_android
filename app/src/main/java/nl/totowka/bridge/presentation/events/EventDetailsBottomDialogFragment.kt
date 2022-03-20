package nl.totowka.bridge.presentation.events

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.EventDetailsHolderBinding
import nl.totowka.bridge.domain.model.EventEntity

/**
 * BottomSheetDialogFragment для отображения смыслов переведенного слова
 */
class EventDetailsBottomDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: EventDetailsHolderBinding
    var event: EventEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EventDetailsHolderBinding.inflate(layoutInflater)
        event = arguments?.getParcelable(EVENT_ENTITY_TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = EventDetailsHolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        event?.let {
            Glide.with(context)
                .load(context.resources.getDrawable(it.image, null))
                .circleCrop()
                .into(binding.image)
            binding.title.text = it.title
            binding.place.text = context.getString(R.string.place, it.place)
            binding.time.text = context.getString(R.string.time, it.time)
            binding.people.text = context.getString(R.string.people, it.people)
            signUp(it.isSigned)
            binding.signup.setOnClickListener { view ->
                it.isSigned = !it.isSigned
                signUp(it.isSigned)
            }
        }
    }

    private fun signUp(isSigned: Boolean) {
        if(isSigned) {
            binding.signup.setBackgroundColor(Color.WHITE)
            binding.signup.text = "Signed"
            context?.let { binding.signup.setTextColor(it.getColor(R.color.purple))}
        } else {
            context?.let { binding.signup.setBackgroundColor(it.getColor(R.color.purple)) }
            binding.signup.setTextColor(Color.WHITE)
            binding.signup.text = "Sign up"
        }
    }

    companion object {
        /**
         * Получение инстанса [EventDetailsBottomDialogFragment]
         */
        fun newInstance(event: EventEntity) =
            EventDetailsBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EVENT_ENTITY_TAG, event)
                }
            }

        const val TAG = "EventDetailsBottomDialogFragment"
        const val EVENT_ENTITY_TAG = "EVENT_ENTITY_TAG"
    }
}