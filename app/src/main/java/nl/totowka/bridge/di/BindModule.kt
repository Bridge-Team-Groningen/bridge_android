package nl.totowka.bridge.di

import dagger.Binds
import dagger.Module
import nl.totowka.bridge.utils.scheduler.SchedulersProvider
import nl.totowka.bridge.utils.scheduler.SchedulersProviderImpl

/**
 * Module with binding details of interfaces with their implementations.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
@Module
interface BindModule {
    @Binds
    fun bindSchedulers(impl: SchedulersProviderImpl): SchedulersProvider
}