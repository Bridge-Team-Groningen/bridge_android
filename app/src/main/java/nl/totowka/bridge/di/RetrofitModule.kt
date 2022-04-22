package nl.totowka.bridge.di

import dagger.Module
import dagger.Provides
import nl.totowka.bridge.BuildConfig
import nl.totowka.bridge.data.api.AuthService
import nl.totowka.bridge.data.api.EventService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module to DI the Retrofit
 */
@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideAuthService(builder: Retrofit.Builder): AuthService = builder
        .baseUrl("$SERVER_BASE_URL")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideEventService(builder: Retrofit.Builder): EventService = builder
        .baseUrl("$SERVER_BASE_URL")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(EventService::class.java)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    companion object {
        private const val SERVER_BASE_URL = "https://bridgerug.herokuapp.com/" // TODO: add url
    }
}