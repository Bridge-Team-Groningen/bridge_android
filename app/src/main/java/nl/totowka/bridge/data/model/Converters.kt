package nl.totowka.bridge.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson

/**
 * Object for converting the entities to json string and vice versa.
 */
object Converters {
    /**
     * Converts list of profile entities to JSON string.
     */
    @TypeConverter
    fun profilesToJson(value: List<ProfileDataEntity>) = Gson().toJson(value)

    /**
     * Converts JSON string to list of profile entities.
     */
    @TypeConverter
    fun jsonToProfiles(value: String) = Gson().fromJson(value, Array<ProfileDataEntity>::class.java).toList()

    /**
     * Converts list of profile entities to JSON string.
     */
    @TypeConverter
    fun eventsToJson(value: List<EventDataEntity>) = Gson().toJson(value)

    /**
     * Converts JSON string to list of profile entities.
     */
    @TypeConverter
    fun jsonToEvents(value: String) = Gson().fromJson(value, Array<EventDataEntity>::class.java).toList()

    /**
     * Converts profile entity to JSON string.
     */
    @TypeConverter
    fun profileToJson(value: ProfileDataEntity?) = Gson().toJson(value)

    /**
     * Converts JSON string to profile entity.
     */
    @TypeConverter
    fun jsonToProfile(value: String) = Gson().fromJson(value, ProfileDataEntity::class.java)

    /**
     * Converts profile entity to JSON string.
     */
    @TypeConverter
    fun eventToJson(value: EventDataEntity?) = Gson().toJson(value)

    /**
     * Converts JSON string to profile entity.
     */
    @TypeConverter
    fun jsonToEvent(value: String) = Gson().fromJson(value, EventDataEntity::class.java)

    /**
     * ConvertsBoolean to JSON string.
     */
    @TypeConverter
    fun booleanToJson(value: Boolean) = Gson().toJson(value)

    /**
     * Converts JSON string to Boolean.
     */
    @TypeConverter
    fun jsonToBoolean(value: String) = Gson().fromJson(value, Boolean::class.java)
}