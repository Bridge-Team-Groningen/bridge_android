package nl.totowka.bridge.presentation.auth.view

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import nl.totowka.bridge.App
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentAuthBinding
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.auth.viewmodel.AuthViewModel
import nl.totowka.bridge.presentation.profile.view.ProfileFragment
import nl.totowka.bridge.presentation.profile.viewmodel.AuthViewModelFactory
import nl.totowka.bridge.utils.ModelPreferencesManager
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import javax.inject.Inject

/**
 * [Fragment] to display the authentication information.
 *
 * @author Kocharyan Tigran
 */
class AuthFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var options: GoogleSignInOptions
    private lateinit var client: GoogleSignInClient
    private lateinit var viewModel: AuthViewModel

    @Inject
    lateinit var interactor: ProfileInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ModelPreferencesManager.with(activity?.applicationContext as Application)
        (activity?.applicationContext as App).getAppComponent().inject(this)
        setupAuth()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this.context as Context)
//        account?.let {
//            startProfile()
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        bindButtons()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as LauncherActivity).isBottomNavVisible(false)
        createViewModel()
        observeLiveData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_AUTH) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.google_auth -> startActivityForResult(client.signInIntent, GOOGLE_AUTH)
            }
        }
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, AuthViewModelFactory(interactor, schedulers)
        ).get(AuthViewModel::class.java)
    }

    private fun setupAuth() {
        options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this.activity as Activity, options);
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            startProfile(task.getResult(ApiException::class.java))
        } catch (e: ApiException) {
            Toast.makeText(this.context, "Login error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startProfile(account: GoogleSignInAccount) {
        viewModel.addProfile(
            ProfileEntity(
                account.displayName,
                account.id.toString(),
                account.email
            )
        )
//        (activity as AppCompatActivity).supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragment_container, ProfileFragment.newInstance(account), ProfileFragment.TAG)
//            .commit()
    }

    private fun startProfile() {

//        (activity as AppCompatActivity).supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragment_container, ProfileFragment.newInstance(), ProfileFragment.TAG)
//            .commit()
    }

    private fun bindButtons() {
        binding.googleAuth.setOnClickListener(this)
        binding.googleAuth.setSize(SignInButton.SIZE_WIDE)
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getSuccessLiveData().observe(viewLifecycleOwner, this::showSuccess)
    }

    private fun showProgress(isVisible: Boolean) {
        // TODO
    }

    private fun showSuccess(isSuccess: Boolean) {
        Log.d(TAG, "showSuccess() called")
        Snackbar.make(binding.root, "Successfully Added", BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    companion object {
        const val TAG = "AuthFragment"
        const val AUTH_TAG = "AUTH_TAG"
        const val GOOGLE_AUTH = 600613
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [AuthFragment]
         */
        fun newInstance() = AuthFragment()
    }
}