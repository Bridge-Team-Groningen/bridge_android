package nl.totowka.bridge.presentation.profile.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.view.*
import nl.totowka.bridge.App
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentProfileBinding
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.SharedViewModel
import nl.totowka.bridge.presentation.auth.view.AuthFragment
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModel
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModelFactory
import nl.totowka.bridge.utils.ModelPreferencesManager
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
        profile = arguments?.getParcelable(PROFILE_TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.editProfile.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        observeLiveData()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as LauncherActivity).isBottomNavVisible(true)

        profile?.let { profile ->
            sharedViewModel.setUser(profile)
            binding.profileTitle.text = profile.name ?: "undefined"
            binding.gender.text = profile.gender ?: "undefined"
            binding.description.text = profile.description ?: "undefined"
            binding.starSign.text = profile.starSign ?: "undefined"
            binding.interests.text = profile.interestList ?: "undefined"
            binding.hobbies.text = profile.hobbies ?: "undefined"
            binding.mottoInLife.text = profile.mottoInLife ?: "undefined"
            binding.age.text = context?.getString(R.string.age, profile.age.toString())
            binding.people.text = profile.capacity?.let {
                context?.getString(R.string.people, it)
            } ?: "undefined"
            binding.city.text = profile.city ?: "undefined"
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
                    .replace(R.id.fragment_container, EditProfileFragment.newInstance(profile), EditProfileFragment.TAG)
                    .commit()
            }
        }
    }

    private fun showProgress(isVisible: Boolean) {
        // TODO
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