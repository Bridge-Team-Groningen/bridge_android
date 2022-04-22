package nl.totowka.bridge.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

/**
 * Domain-layer entity of profile.
 */
//@Parcelize
//data class ProfileEntity(
//    var id: Int, // The unique one, that created Backend in DB
//    var googleId: Long? = null, // The unique one, that Google provides us
//    var name: String? = null,
//    var surname: String? = null,
//    var city: String? = null,
//    var age: Int? = null,
//
//    var starSign: String? = null,
//    var birthday: LocalDate? = null,
//    var motto: String? = null,
//    var description: String? = null,
//
//    var email: String? = null,
//    var photoUrl: Uri? = null,
//    var interests: List<String>? = null,
//    var peopleToMeet: Int? = null,
//    var gender: String? = null,
//) : Parcelable

@Parcelize
data class ProfileEntity(
    var name: String? = null,
    var googleId: String? = null,
    var email: String? = null,
    var gender: String? = null,
    var description: String? = null,
    var interest: String? = null,
    var capacity: Int? = null,
    var starSign: String? = null,
    var city: String? = null,
    var mottoInLife: String? = null,
) : Parcelable

@Parcelize
enum class Gender(var gender: String) : Parcelable {
    MALE("male"),
    FEMALE("female"),
    UNDEFINED("undefined")
}