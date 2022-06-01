package nl.totowka.bridge.presentation.events.view.signed

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.HolderEventDetailsBinding
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.presentation.SharedViewModel
import nl.totowka.bridge.utils.Common.toCoolString
import nl.totowka.bridge.utils.callback.EventClickListener
import nl.totowka.bridge.utils.callback.SignInClickListener

/**
 * [BottomSheetDialogFragment] to display the event details.
 */
class EventDetailsBottomDialogFragment(private var signInListener: SignInClickListener) :
    BottomSheetDialogFragment() {
    private lateinit var binding: HolderEventDetailsBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var event: EventEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HolderEventDetailsBinding.inflate(layoutInflater)
        event = arguments?.getParcelable(EVENT_ENTITY_TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HolderEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        event?.let { event ->
            Glide.with(context)
                .load(context.resources.getDrawable(R.drawable.club, null))
                .circleCrop()
                .into(binding.image)
            binding.title.text = event.name
            binding.place.text = context.getString(R.string.place, event.location)
            binding.time.text = context.getString(
                R.string.time,
                event.date?.toCoolString() ?: "unknown"
            )
            binding.people.text = context.getString(
                R.string.people_details,
                event.noOfParticipants,
                event.maxCapacity
            )
            binding.description.text = event.description
            event.isSigned?.let { isSigned ->
                signUp(isSigned)
                binding.signup.setOnClickListener {
                    event.let {
                        sharedViewModel.adapterPosition.value?.let { position ->
                            signInListener.onClick(
                                it,
                                position
                            )
                        }
                    }
                    event.isSigned = !isSigned
                    signUp(!isSigned)
                    binding.signup.isEnabled = false
                }
            }

        }
    }

    private fun signUp(isSigned: Boolean) {
        if (isSigned) {
            binding.signup.setBackgroundColor(Color.WHITE)
            binding.signup.text = "Signed"
            context?.let { binding.signup.setTextColor(it.getColor(R.color.purple)) }
        } else {
            context?.let { binding.signup.setBackgroundColor(it.getColor(R.color.purple)) }
            binding.signup.setTextColor(Color.WHITE)
            binding.signup.text = "Sign up"
        }
    }

    companion object {
        /**
         * Retrieve the instance of [EventDetailsBottomDialogFragment]
         */
        fun newInstance(event: EventEntity, signInClickListener: SignInClickListener) =
            EventDetailsBottomDialogFragment(signInClickListener).apply {
                arguments = Bundle().apply {
                    putParcelable(EVENT_ENTITY_TAG, event)
                }
            }

        const val TAG = "EventDetailsBottomDialogFragment"
        const val EVENT_ENTITY_TAG = "EVENT_ENTITY_TAG"
    }
}