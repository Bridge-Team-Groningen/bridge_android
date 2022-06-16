package nl.totowka.bridge.data.model

import android.os.Parcelable
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import nl.totowka.bridge.domain.model.EventEntity
import java.util.*

/**
 * Data-layer entity of event.
 */
@Parcelize
@TypeConverters(Converters::class)
data class EventDataEntity(
    @SerializedName("id") var id: Int,
    @SerializedName("activity") var activity: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("noOfParticipants") var noOfParticipants: Int? = null,
    @SerializedName("maxCapacity") var maxCapacity: Int? = null,
    @SerializedName("date") var date: Date? = null,
    @SerializedName("creatorId") var creatorId: String? = null
) : Parcelable {
    fun toEntity() = EventEntity(
        id, activity, name, description, location, noOfParticipants, maxCapacity, date, creatorId
    )

    companion object {
        fun fromEntity(entity: EventEntity) = with(entity) {
            EventDataEntity(
                id,
                activity,
                name,
                description,
                location,
                noOfParticipants,
                maxCapacity,
                date,
                creatorId
            )
        }
    }
}