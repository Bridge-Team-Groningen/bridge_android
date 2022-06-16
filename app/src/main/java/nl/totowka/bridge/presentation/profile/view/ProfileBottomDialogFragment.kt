package nl.totowka.bridge.presentation.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nl.totowka.bridge.App
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.HolderUserDetailsBinding
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.presentation.LauncherActivity
import nl.totowka.bridge.presentation.SharedViewModel

/**
 * [BottomSheetDialogFragment] to display the user details.
 */
class ProfileBottomDialogFragment() : BottomSheetDialogFragment() {
    private lateinit var binding: HolderUserDetailsBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var profile: ProfileEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HolderUserDetailsBinding.inflate(layoutInflater)
        (activity?.applicationContext as App).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HolderUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as LauncherActivity).isBottomNavVisible(true)

        profile = sharedViewModel.matchedUser?.value
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

    companion object {
        /**
         * Retrieve the instance of [ProfileBottomDialogFragment]
         */
        fun newInstance() = ProfileBottomDialogFragment()

        const val TAG = "ProfileBottomDialogFragment"
    }
}