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
     * Converts profile entity to JSON string.
     */
    @TypeConverter
    fun profileToJson(value: ProfileDataEntity?) = Gson().toJson(value)

    /**
     * Converts JSON string to profile entity.
     */
    @TypeConverter
    fun jsonToProfile(value: String) = Gson().fromJson(value, ProfileDataEntity::class.java)
}