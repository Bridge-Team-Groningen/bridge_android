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

/**
 * Data-layer entity of event.
 */
@Parcelize
@TypeConverters(Converters::class)
data class EventDataEntity(
    @SerializedName("image") @DrawableRes var image: Int,
    @SerializedName("title") var title: String? = null,
    @SerializedName("place") var place: String? = null,
    @SerializedName("time") var time: String? = null,
    @SerializedName("people") var people: Int? = null,
    @SerializedName("isSigned") var isSigned: Boolean = false,
) : Parcelable {
    fun toEntity() = EventEntity(
        image,
        title,
        place,
        time,
        people,
        isSigned
    )

    companion object {
        fun fromEntity(entity: EventEntity) = with(entity) {
            EventDataEntity(
                image,
                title,
                place,
                time,
                people,
                isSigned
            )
        }
    }
}