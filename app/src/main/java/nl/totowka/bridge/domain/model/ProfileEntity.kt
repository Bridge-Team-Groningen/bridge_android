package nl.totowka.bridge.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Domain-layer entity of profile.
 */
@Parcelize
data class ProfileEntity(
    var name: String? = null,
    var googleId: String? = null,
    var email: String? = null,
    var age: Int? = null,
    var profilePicture: String? = null,
    var hobbies: String? = null,
    var gender: String? = null,
    var description: String? = null,
    var interestList: String? = null,
    var capacity: Int? = null,
    var starSign: String? = null,
    var city: String? = null,
    var mottoInLife: String? = null,
    var questionWifi: String? = null,
) : Parcelable

@Parcelize
enum class Gender(var gender: String) : Parcelable {
    MALE("male"),
    FEMALE("female"),
    UNDEFINED("undefined")
}