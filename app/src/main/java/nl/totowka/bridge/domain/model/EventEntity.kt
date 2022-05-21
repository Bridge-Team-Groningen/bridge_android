package nl.totowka.bridge.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.Ignore
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

///**
// * Domain-layer entity of event.
// */
//@Parcelize
//data class EventEntity(
//    @DrawableRes var image: Int,
//    var title: String? = null,
//    var place: String? = null,
//    var time: String? = null,
//    var people: Int? = null,
//    var isSigned: Boolean = false,
//) : Parcelable

/**
 * Domain-layer entity of event.
 */
@Parcelize
data class EventEntity(
    var id: Int,
    var activity: String? = null,
    var name: String? = null,
    var description: String? = null,
    var location: String? = null,
    var noOfParticipants: Int? = null,
    var maxCapacity: Int? = null,
    var date: LocalDate? = null,
    var isSigned: Boolean? = null
) : Parcelable