package nl.totowka.bridge.presentation.auth.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentAuthBinding
import nl.totowka.bridge.presentation.profile.view.details.ProfileFragment

/**
 * Фрагмент, отвечающий за экран изучения слов.
 */
class AuthFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var options: GoogleSignInOptions
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAuth()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this.context as Context)
        account?.let {
            startProfile(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        bindButtons()
        return binding.root
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
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment.newInstance(account), ProfileFragment.TAG)
            .commit()
    }

    private fun bindButtons() {
        binding.googleAuth.setOnClickListener(this)
        binding.googleAuth.setSize(SignInButton.SIZE_WIDE)
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