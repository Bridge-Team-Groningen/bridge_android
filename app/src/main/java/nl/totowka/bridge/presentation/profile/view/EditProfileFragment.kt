package nl.totowka.bridge.presentation.profile.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import nl.totowka.bridge.App
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentEditProfileBinding
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.SharedViewModel
import nl.totowka.bridge.presentation.events.view.signed.EventsFragment
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModel
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModelFactory
import nl.totowka.bridge.utils.Common.glideFactory
import nl.totowka.bridge.utils.Common.isEmpty
import nl.totowka.bridge.utils.Common.isNotEmpty
import nl.totowka.bridge.utils.Common.text
import nl.totowka.bridge.utils.Common.toCoolString
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import javax.inject.Inject


/**
 * [Fragment] to edit the user data.
 *
 * @author Kocharyan Tigran
 */
class EditProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditProfileBinding
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
        profile = arguments?.getParcelable(ProfileFragment.PROFILE_TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.saveProfile.setOnClickListener(this)
        binding.back.setOnClickListener(this)

        val genders = resources.getStringArray(R.array.genders)
        val gendersAdapter = ArrayAdapter(this.context as Context, R.layout.dropdown_item, genders)
        binding.gendersDropdown.setAdapter(gendersAdapter)

        val starSigns = resources.getStringArray(R.array.starSigns)
        val starSignsAdapter =
            ArrayAdapter(this.context as Context, R.layout.dropdown_item, starSigns)
        binding.starSignsDropdown.setAdapter(starSignsAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        observeLiveData()
        binding.profileTitle.text = profile?.name ?: "undefined"
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as LauncherActivity).isBottomNavVisible(false)
        profile?.let {
            showProfileDetails(it)
            Glide.with(view.context)
                .load(it.profilePicture)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade(glideFactory()).crossFade(100))
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(binding.profileImage)
        }
    }

    private fun showProfileDetails(profile: ProfileEntity) {
        binding.city.setText(profile.city)
        binding.age.setText(profile.age.toString())
        binding.description.setText(profile.description)
        binding.interests.setText(profile.interestList)
        binding.hobbies.setText(profile.hobbies)
        binding.people.setText(profile.capacity.toString())
        binding.mottoInLife.setText(profile.mottoInLife.toString())
        binding.starSignsDropdown.setText(profile.starSign)
        binding.gendersDropdown.setText(profile.gender)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                profile?.googleId?.let { viewModel.getProfile(it) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.save_profile -> {
                profile?.let {
                    if (it.gender.isNullOrEmpty() or it.city.isNullOrEmpty() or (it.age == null) or it.interestList.isNullOrEmpty() or (it.capacity == null)) {
                        if (binding.gendersMenu.isEmpty() or binding.city.isEmpty() or binding.age.isEmpty() or binding.interests.isEmpty() or binding.people.isEmpty()) {
                            Snackbar.make(
                                binding.root,
                                "Age, Gender, City, Interest and № of people to meet should be filled!",
                                BaseTransientBottomBar.LENGTH_SHORT
                            ).show()
                        } else
                            saveUser()
                    } else
                        saveUser()
                }
            }
            R.id.back -> {
                profile?.googleId?.let { viewModel.getProfile(it) }
            }
        }
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, ProfileViewModelFactory(interactor, schedulers)
        ).get(ProfileViewModel::class.java)
    }

    private fun saveUser() {
        profile?.let {
            if (!binding.interests.isEmpty()) {
                it.interestList = binding.interests.text()
            }
            if (binding.gendersMenu.isNotEmpty()) {
                it.gender = binding.gendersMenu.editText?.text()
            }
            if (!binding.description.isEmpty()) {
                it.description = binding.description.text()
            }
            if (binding.starSignsMenu.isNotEmpty()) {
                it.starSign = binding.starSignsMenu.editText?.text()
            }
            if (!binding.mottoInLife.isEmpty()) {
                it.mottoInLife = binding.mottoInLife.text()
            }
            if (!binding.people.isEmpty()) {
                it.capacity = binding.people.text().toInt()
            }
            if (!binding.city.isEmpty()) {
                it.city = binding.city.text()
            }
            if (!binding.age.isEmpty()) {
                it.age = binding.age.text().toInt()
            }
            if (!binding.hobbies.isEmpty()) {
                it.hobbies = binding.hobbies.text()
            }
            it.googleId?.let { id ->
                viewModel.updateProfile(id, it)
            } ?: showError(Exception("Couldn't find Google ID"))
            sharedViewModel.setUser(it)
        }
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(EventsFragment.TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showSuccess(message: String) {
        Log.d(TAG, "showSuccess() called")
        Snackbar.make(binding.root, message, BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(
            binding.root,
            throwable.toString(),
            BaseTransientBottomBar.LENGTH_SHORT
        )
            .show()
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getSuccessLiveData().observe(viewLifecycleOwner, this::showSuccess)
        viewModel.getProfileLiveData().observe(viewLifecycleOwner, this::startProfile)
    }

    private fun startProfile(profile: ProfileEntity) {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                ProfileFragment.newInstance(profile),
                ProfileFragment.TAG
            )
            .commit()
    }

    private fun exit() {
        (activity as AppCompatActivity).supportFragmentManager
            .popBackStack()
    }

    companion object {
        const val TAG = "EditProfileFragment"
        const val PROFILE_TAG = "EDIT_PROFILE_TAG"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Builder for [ProfileFragment]
         */
        fun newInstance() = EditProfileFragment()

        /**
         * Builder for [ProfileFragment] with profile entity
         */
        fun newInstance(profile: ProfileEntity?) = EditProfileFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ProfileFragment.PROFILE_TAG, profile)
            }
        }
    }
}