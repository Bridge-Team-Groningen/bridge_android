package nl.totowka.bridge.presentation.profile.view.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.FragmentEditProfileBinding
import nl.totowka.bridge.presentation.profile.view.details.ProfileFragment
import nl.totowka.bridge.utils.Common.string


/**
 * Фрагмент, отвечающий за экран изучения слов.
 */
class EditProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.saveProfile.setOnClickListener(this)
        binding.back.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                exit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.save_profile -> {
                Toast.makeText(this.context, "Successfully saved", Toast.LENGTH_SHORT).show()
                // TODO: SAVE ON BACKEND
            }
            R.id.back -> {
                exit()
            }
        }
    }

    private fun exit() {
        (activity as AppCompatActivity).supportFragmentManager
            .popBackStack()
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
        fun newInstance() = EditProfileFragment()
    }
}