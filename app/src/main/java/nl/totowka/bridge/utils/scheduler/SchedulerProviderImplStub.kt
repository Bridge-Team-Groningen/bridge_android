package nl.totowka.bridge.utils.scheduler

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of interface [SchedulersProvider], that is used in testing.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
class SchedulersProviderImplStub : SchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}