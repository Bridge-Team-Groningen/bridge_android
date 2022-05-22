package nl.totowka.bridge.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.Ignore
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.domain.model.ProfileEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

///**
// * Data-layer entity of event.
// */
//@Parcelize
//@TypeConverters(Converters::class)
//data class EventDataEntity(
//    @SerializedName("image") @DrawableRes var image: Int,
//    @SerializedName("title") var title: String? = null,
//    @SerializedName("place") var place: String? = null,
//    @SerializedName("time") var time: String? = null,
//    @SerializedName("people") var people: Int? = null,
//    @SerializedName("isSigned") var isSigned: Boolean = false,
//) : Parcelable {
//    fun toEntity() = EventEntity(
//        image,
//        title,
//        place,
//        time,
//        people,
//        isSigned
//    )
//
//    companion object {
//        fun fromEntity(entity: EventEntity) = with(entity) {
//            EventDataEntity(
//                image,
//                title,
//                place,
//                time,
//                people,
//                isSigned
//            )
//        }
//    }
//}

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
    @SerializedName("date") var date: Date? = null
) : Parcelable {
    fun toEntity() = EventEntity(
        id, activity, name, description, location, noOfParticipants, maxCapacity, date
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
                date
            )
        }
    }
}