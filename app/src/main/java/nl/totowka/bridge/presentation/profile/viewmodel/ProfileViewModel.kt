package nl.totowka.bridge.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import nl.totowka.bridge.domain.interactor.ProfileInteractor
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.utils.scheduler.SchedulersProvider

/**
 * ViewModel for Profile related actions.
 */
class ProfileViewModel(
    private val profileInteractor: ProfileInteractor,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val successLiveData = MutableLiveData<String>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val profileLiveData = MutableLiveData<ProfileEntity>()
    private val profilesLiveData = MutableLiveData<List<ProfileEntity>>()
    private val disposables = CompositeDisposable()

    /**
     * Get user profile data with id=[id] from DB
     *
     * @param id user's id
     */
    fun getProfile(id: String) {
        disposables.add(profileInteractor.getProfile(id)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(profileLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Get user profiles data from DB
     */
    fun getProfiles(userId: String) {
        disposables.add(profileInteractor.getProfiles(userId)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(profilesLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Update user profile data with id=[id] in DB
     *
     * @param id user's id
     */
    fun updateProfile(id: String, profileEntity: ProfileEntity) {
        disposables.add(profileInteractor.updateProfile(id, profileEntity)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                Log.d(TAG, "completed updateProfile!")
                successLiveData.postValue("Successfully updated the profile in DB!")
            }, { addProfile(profileEntity) })
        )
    }

    /**
     * Add new user profile data with to DB
     *
     * @param profileEntity user's data
     */
    fun addProfile(profileEntity: ProfileEntity) {
        disposables.add(profileInteractor.addProfile(profileEntity)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                Log.d(TAG, "completed addProfile!")
                successLiveData.postValue("Successfully added the profile to DB!")
            }, errorLiveData::setValue)
        )
    }

    /**
     * Check whether user is already in DB
     *
     * @param id user's id
     */
    fun isUser(id: String) {
        disposables.add(profileInteractor.isUser(id)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(TAG, "completed isUser!") }, errorLiveData::setValue)
        )
    }

    /**
     * Like other user
     *
     * @param user1Id user's id
     * @param user2Id other user's id
     */
    fun like(user1Id: String, user2Id: String) {
        disposables.add(profileInteractor.like(user1Id, user2Id)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(TAG, "completed like!") }, { })
        )
    }

    /**
     * Unlikes other user
     *
     * @param user1Id user's id
     * @param user2Id other user's id
     */
    fun unlike(user1Id: String, user2Id: String) {
        disposables.add(profileInteractor.unlike(user1Id, user2Id)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(TAG, "completed unlike!") }, errorLiveData::setValue)
        )
    }

    /**
     * Check match between users
     *
     * @param user1Id user's id
     * @param user2Id other user's id
     */
    fun match(user1Id: String, user2Id: String) = profileInteractor.match(user1Id, user2Id)

    /**
     * Method clears disposables.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
    }

    /**
     * @return LiveData<Boolean> for progress display
     */
    fun getProgressLiveData(): LiveData<Boolean> =
        progressLiveData

    /**
     * @return LiveData<Boolean> for error display
     */
    fun getErrorLiveData(): LiveData<Throwable> =
        errorLiveData

    /**
     * @return LiveData<Boolean> for success message display
     */
    fun getSuccessLiveData(): LiveData<String> =
        successLiveData

    /**
     * @return LiveData<Boolean> for sharing the user profile entity
     */
    fun getProfileLiveData(): LiveData<ProfileEntity> =
        profileLiveData

    /**
     * @return LiveData<ProfileEntity> for sharing the users
     */
    fun getProfilesLiveData(): LiveData<List<ProfileEntity>> =
        profilesLiveData

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}