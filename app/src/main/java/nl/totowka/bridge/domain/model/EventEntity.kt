package nl.totowka.bridge.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.Ignore
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * Domain-layer entity of event.
 */
@Parcelize
data class EventEntity(
    @DrawableRes var image: Int,
    var title: String? = null,
    var place: String? = null,
    var time: String? = null,
    var people: Int? = null,
    var isSigned: Boolean = false,
) : Parcelable