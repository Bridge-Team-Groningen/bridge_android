package nl.totowka.bridge.presentation.auth.viewmodel

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
 * ViewModel for AuthFragment.
 */
class AuthViewModel(
    private val profileInteractor: ProfileInteractor,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val successLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val profileLiveData = MutableLiveData<ProfileEntity>()
    private val disposables = CompositeDisposable()

    /**
     * Получить слово с id=[id] из БД
     *
     * @param id идентификатор
     */
    fun getProfile(id: Int) {
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
     * Обновить слово [wordEntity] в БД
     *
     * @param wordEntity слово
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
                successLiveData.postValue(true)
            }, errorLiveData::setValue)
        )
    }

    /**
     * Обновить слово с wordEntity=[wordEntity] в БД
     *
     * @param wordEntity слово
     */
    fun isUser(id: Int) {
        disposables.add(profileInteractor.isUser(id)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(TAG, "completed isUser!") }, errorLiveData::setValue)
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
    }

    /**
     * @return LiveData<Boolean> для подписки
     */
    fun getProgressLiveData(): LiveData<Boolean> {
        return progressLiveData
    }

    /**
     * @return LiveData<Boolean> для подписки
     */
    fun getErrorLiveData(): LiveData<Throwable> {
        return errorLiveData
    }

    /**
     * @return LiveData<Boolean> для подписки
     */
    fun getSuccessLiveData(): LiveData<Boolean> {
        return successLiveData
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}