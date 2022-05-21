package nl.totowka.bridge.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.totowka.bridge.domain.model.ProfileEntity

class SharedViewModel : ViewModel() {
    private var _user = MutableLiveData(ProfileEntity())
    var user : LiveData<ProfileEntity>? = _user

    fun setUser(user: ProfileEntity) {
        _user.value = user
    }
}