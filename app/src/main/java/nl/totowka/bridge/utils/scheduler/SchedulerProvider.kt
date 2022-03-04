package nl.totowka.bridge.utils.scheduler

import io.reactivex.Scheduler

/**
 * Interface to work with and test Schedulers.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
interface SchedulersProvider {
    /**
     * For IO-thread
     */
    fun io(): Scheduler

    /**
     * For UI-thread
     */
    fun ui(): Scheduler
}