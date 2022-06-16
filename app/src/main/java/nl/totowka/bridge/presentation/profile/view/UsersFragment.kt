package nl.totowka.bridge.presentation.profile.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import nl.totowka.bridge.databinding.FragmentUsersBinding
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.SharedViewModel
import nl.totowka.bridge.presentation.auth.view.AuthFragment
import nl.totowka.bridge.presentation.events.view.signed.EventsFragment
import nl.totowka.bridge.presentation.profile.adapter.UsersAdapter
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModel
import nl.totowka.bridge.presentation.profile.viewmodel.ProfileViewModelFactory
import nl.totowka.bridge.utils.Common.color
import nl.totowka.bridge.utils.callback.UserClickListener
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import javax.inject.Inject

class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var viewModel: ProfileViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: UsersAdapter

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
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        observeLiveData()
        createAdapter()
        createRefresher()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as LauncherActivity).isBottomNavVisible(true)
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, ProfileViewModelFactory(interactor, schedulers)
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
        sharedViewModel.user?.value?.googleId?.let { viewModel.getProfiles(it) }
    }

    private fun createRefresher() {
        binding.refresher.isRefreshing = true
        binding.refresher.setColorSchemeColors(context?.color(R.color.purple) ?: Color.MAGENTA)
        binding.refresher.setOnRefreshListener {
            sharedViewModel.user?.value?.googleId?.let { viewModel.getProfiles(it) }
        }
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

    private fun showUsers(users: List<ProfileEntity>) {
        adapter.updateData(ArrayList(users))
        binding.refresher.isRefreshing = false
    }


    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getProfilesLiveData().observe(viewLifecycleOwner, this::showUsers)
    }

    val listener = object : UserClickListener {
        override fun onLike(user: ProfileEntity) {
            viewModel.like(sharedViewModel.user?.value?.googleId ?: "", user.googleId ?: "")
        }

        override fun onUnlike(user: ProfileEntity) {
            viewModel.unlike(sharedViewModel.user?.value?.googleId ?: "", user.googleId ?: "")
        }

        override fun onOpenUser(user: ProfileEntity) {
            if (viewModel.match(
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
        const val TAG = "UsersFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Builder for [UsersFragment] with no profile entity
         */
        fun newInstance() = UsersFragment()
    }
}