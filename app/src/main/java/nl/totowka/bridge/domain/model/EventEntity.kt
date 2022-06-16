package nl.totowka.bridge.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

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
    var date: Date? = null,
    var creatorId: String? = null,
    var isSigned: Boolean? = null
) : Parcelable