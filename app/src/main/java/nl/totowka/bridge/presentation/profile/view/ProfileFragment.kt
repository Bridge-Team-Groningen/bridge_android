package nl.totowka.bridge.presentation.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentProfileBinding
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.utils.ModelPreferencesManager


/**
 * [Fragment] to display the user data.
 */
class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    var account: GoogleSignInAccount? = null
    var profile: ProfileEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = arguments?.getParcelable(PROFILE_TAG)
        // TODO: Call API with google id to get User's data
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.editProfile.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as LauncherActivity).isBottomNavVisible(true)

        if(account != null) {
            account.let {
                profile = ProfileEntity(0, it?.id?.toInt() ?: 0, it?.displayName ?: "undefinded", it?.familyName ?: "undefined")
                ModelPreferencesManager.put(profile)
            }
        } else {
            profile = ModelPreferencesManager.get<ProfileEntity>()
        }

        profile?.let { profile ->
            binding.interests.text = profile.interests?.joinToString() ?: "undefined"
            binding.age.text = profile.age?.let {
                context?.getString(R.string.age, it)
            } ?: "undefined"
            binding.people.text = profile.peopleToMeet?.let {
                context?.getString(R.string.people, it)
            } ?: "undefined"
            binding.city.text = profile.city ?: "undefined"
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.edit_profile -> (activity as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, EditProfileFragment.newInstance(), EditProfileFragment.TAG)
                    .commit()
            }
        }
    }

    companion object {
        const val TAG = "ProfileFragment"
        const val PROFILE_TAG = "PROFILE_TAG"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [ProfileFragment]
         */
        fun newInstance(account: GoogleSignInAccount) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PROFILE_TAG, account)
            }
        }

        /**
         * Получение объекта [ProfileFragment] без учета профиля
         */
        fun newInstance() = ProfileFragment()
    }
}