package nl.totowka.bridge.presentation.events.view.signed

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import nl.totowka.bridge.App
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.HolderEventDetailsBinding
import nl.totowka.bridge.domain.interactor.EventInteractor
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.presentation.SharedViewModel
import nl.totowka.bridge.presentation.auth.view.AuthFragment
import nl.totowka.bridge.presentation.events.view.edit.EditEventFragment
import nl.totowka.bridge.presentation.events.viewmodel.EventViewModel
import nl.totowka.bridge.presentation.events.viewmodel.EventViewModelFactory
import nl.totowka.bridge.presentation.profile.adapter.UsersAdapter
import nl.totowka.bridge.presentation.profile.view.EditProfileFragment
import nl.totowka.bridge.presentation.profile.view.ProfileBottomDialogFragment
import nl.totowka.bridge.presentation.profile.view.ProfileFragment
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModel
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModelFactory
import nl.totowka.bridge.utils.Common.setGone
import nl.totowka.bridge.utils.Common.toCoolString
import nl.totowka.bridge.utils.callback.UserClickListener
import nl.totowka.bridge.utils.callback.SignInClickListener
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import javax.inject.Inject

/**
 * [BottomSheetDialogFragment] to display the event details.
 */
class EventDetailsBottomDialogFragment(private var signInListener: SignInClickListener) :
    BottomSheetDialogFragment() {
    private lateinit var binding: HolderEventDetailsBinding
    private lateinit var eventViewModel: EventViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var adapter: UsersAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var event: EventEntity? = null
    var user: ProfileEntity? = null

    @Inject
    lateinit var eventInteractor: EventInteractor

    @Inject
    lateinit var profileInteractor: ProfileInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HolderEventDetailsBinding.inflate(layoutInflater)
        (activity?.applicationContext as App).getAppComponent().inject(this)
        event = sharedViewModel.event?.value
        user = sharedViewModel.user?.value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HolderEventDetailsBinding.inflate(inflater, container, false)
        if (!event?.creatorId.equals(user?.googleId)) binding.editEvent.setGone()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        createViewModel()
        observeLiveData()
        createAdapter()
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
                binding.editEvent.setOnClickListener {
                    sharedViewModel.setEditEvent(event)
                    this.dismiss()
                    (activity as AppCompatActivity).supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(
                            R.id.fragment_container,
                            EditEventFragment.newInstance(),
                            EditEventFragment.TAG
                        )
                        .commit()
                }
            }
        }
    }

    private fun createViewModel() {
        eventViewModel = ViewModelProvider(
            this, EventViewModelFactory(eventInteractor, schedulers)
        ).get(EventViewModel::class.java)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory(profileInteractor, schedulers)
        ).get(ProfileViewModel::class.java)
    }

    private fun createAdapter() {
        adapter = UsersAdapter(ArrayList(), listener)
        binding.users.layoutManager = LinearLayoutManager(context)
        binding.users.adapter = adapter
        binding.users.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        sharedViewModel.user?.value?.googleId?.let {
            eventViewModel.getUsersOfEvent(event?.id.toString(), it)
        }
    }

    private fun showError(throwable: Throwable) {
        Log.d(AuthFragment.TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showUsers(users: List<ProfileEntity>) {
        adapter.updateData(ArrayList(users))
    }

    private fun observeLiveData() {
        eventViewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        eventViewModel.getProfilesLiveData().observe(viewLifecycleOwner, this::showUsers)
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

    val listener = object : UserClickListener {
        override fun onLike(user: ProfileEntity) {
            profileViewModel.like(sharedViewModel.user?.value?.googleId ?: "", user.googleId ?: "")
        }

        override fun onUnlike(user: ProfileEntity) {
            profileViewModel.unlike(
                sharedViewModel.user?.value?.googleId ?: "",
                user.googleId ?: ""
            )
        }

        override fun onOpenUser(user: ProfileEntity) {
            if (profileViewModel.match(
                    sharedViewModel.user?.value?.googleId ?: "",
                    user.googleId ?: ""
                ) == true
            ) {
                val profileBottomDialogFragment =
                    ProfileBottomDialogFragment.newInstance()
                sharedViewModel.setMatchedUser(user)
                profileBottomDialogFragment.show(
                    (activity as AppCompatActivity).supportFragmentManager,
                    ProfileBottomDialogFragment.TAG
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "You're not matched with ${user.name} yet!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        /**
         * Retrieve the instance of [EventDetailsBottomDialogFragment]
         */
        fun newInstance(signInClickListener: SignInClickListener) =
            EventDetailsBottomDialogFragment(signInClickListener)

        const val TAG = "EventDetailsBottomDialogFragment"
    }
}