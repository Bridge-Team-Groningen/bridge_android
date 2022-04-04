package nl.totowka.bridge.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder

/**
 * Singleton class for managing preferences for POJO or model class's object.
 * @author Kocharyan Tigran
 */
object ModelPreferencesManager {
    lateinit var preferences: SharedPreferences

    private const val PREFERENCES_FILE_NAME = "PREFERENCES_FILE_NAME"
    const val USER_PREFERENCES_KEY = "USER_PREFERENCES_KEY"

    /**
     * Call this first before retrieving or saving object.
     *
     * @param application Instance of application class
     */
    fun with(application: Application) {
        preferences = application.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Saves object into the Preferences.
     *
     * @param model Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(model: T, key: String = USER_PREFERENCES_KEY) {
        val jsonString = GsonBuilder().create().toJson(model)
        preferences.edit().putString(key, jsonString).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String = USER_PREFERENCES_KEY): T? {
        val value = preferences.getString(key, null)
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}