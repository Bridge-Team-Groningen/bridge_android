package nl.totowka.bridge.utils.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Implementation of interface [SchedulersProvider].
 * Created by Kocharyan Tigran on 04.03.2022.
 */
class SchedulersProviderImpl @Inject constructor() : SchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}