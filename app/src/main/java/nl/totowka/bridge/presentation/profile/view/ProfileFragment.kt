package nl.totowka.bridge.presentation.profile.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import nl.totowka.bridge.App
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentProfileBinding
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.SharedViewModel
import nl.totowka.bridge.presentation.auth.view.AuthFragment
import nl.totowka.bridge.presentation.events.view.signed.EventsFragment
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModel
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModelFactory
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import javax.inject.Inject


/**
 * [Fragment] to display the user data.
 */
class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var profile: ProfileEntity? = null

    @Inject
    lateinit var interactor: ProfileInteractor

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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.editProfile.setOnClickListener(this)
        binding.logout.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        observeLiveData()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as LauncherActivity).isBottomNavVisible(true)

        profile = sharedViewModel.user?.value
        profile?.let { profile ->
            binding.profileTitle.text = profile.name ?: "undefined"
            binding.gender.text = profile.gender ?: "undefined"
            binding.description.text = profile.description ?: "undefined"
            binding.starSign.text = profile.starSign ?: "undefined"
            binding.interests.text = profile.interestList ?: "undefined"
            binding.hobbies.text = profile.hobbies ?: "undefined"
            binding.mottoInLife.text = profile.mottoInLife ?: "undefined"
            binding.age.text = context?.getString(R.string.age, profile.age.toString())
            binding.people.text = profile.capacity?.let {
                context?.getString(R.string.people_profile, it)
            } ?: "undefined"
            binding.city.text = profile.city ?: "undefined"
            Glide.with(view.context)
                .load(profile.profilePicture)
                .circleCrop()
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(binding.profileImage)
        }
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, ProfileViewModelFactory(interactor, schedulers)
        ).get(ProfileViewModel::class.java)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.edit_profile -> (activity as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(
                        R.id.fragment_container,
                        EditProfileFragment.newInstance(profile),
                        EditProfileFragment.TAG
                    )
                    .commit()
                R.id.logout -> {
                    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()
                    val client = GoogleSignIn.getClient(this.activity as Activity, options)

                    client.signOut()
                        .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                            (activity as AppCompatActivity).supportFragmentManager
                                .beginTransaction()
                                .addToBackStack(null)
                                .replace(
                                    R.id.fragment_container,
                                    AuthFragment.newInstance(),
                                    AuthFragment.TAG
                                )
                                .commit()
                        })
                }
            }
        }
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(EventsFragment.TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showSuccess(message: String) {
        Log.d(AuthFragment.TAG, "showSuccess() called")
        Snackbar.make(binding.root, message, BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showError(throwable: Throwable) {
        Log.d(AuthFragment.TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getSuccessLiveData().observe(viewLifecycleOwner, this::showSuccess)
    }

    companion object {
        const val TAG = "ProfileFragment"
        const val PROFILE_TAG = "PROFILE_TAG"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Builder for [ProfileFragment] with profile entity
         */
        fun newInstance(profile: ProfileEntity) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PROFILE_TAG, profile)
            }
        }

        /**
         * Builder for [ProfileFragment] with no profile entity
         */
        fun newInstance() = ProfileFragment()
    }
}