package nl.totowka.bridge.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import nl.totowka.bridge.BuildConfig
import nl.totowka.bridge.data.api.EventService
import nl.totowka.bridge.data.api.LikeService
import nl.totowka.bridge.data.api.ProfileService
import nl.totowka.bridge.data.api.UserEventService
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
    fun provideAuthService(builder: Retrofit.Builder, gson: Gson): ProfileService = builder
        .baseUrl(SERVER_BASE_URL + USER_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ProfileService::class.java)

    @Provides
    @Singleton
    fun provideLikeService(builder: Retrofit.Builder, gson: Gson): LikeService = builder
        .baseUrl(SERVER_BASE_URL + LIKE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(LikeService::class.java)

    @Provides
    @Singleton
    fun provideEventService(builder: Retrofit.Builder, gson: Gson): EventService = builder
        .baseUrl(SERVER_BASE_URL + EVENT_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(EventService::class.java)

    @Provides
    @Singleton
    fun provideUserEventService(builder: Retrofit.Builder, gson: Gson): UserEventService = builder
        .baseUrl(SERVER_BASE_URL + USER_EVENT_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(UserEventService::class.java)

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
    fun provideGson(): Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    companion object {
        private const val SERVER_BASE_URL = "https://bridgerug.herokuapp.com/"
        private const val LOCAL_BASE_URL = "http://localhost:8080//"
        private const val EVENT_URL = "api/events/"
        private const val USER_EVENT_URL = "api/user_events/"
        private const val USER_URL = "api/users/"
        private const val LIKE_URL = "api/user_likes/"
    }
}