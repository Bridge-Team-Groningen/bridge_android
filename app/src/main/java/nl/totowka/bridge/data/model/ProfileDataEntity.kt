package nl.totowka.bridge.data.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import nl.totowka.bridge.domain.model.ProfileEntity
import java.time.LocalDate

///**
// * Data-layer entity of profile.
// */
//@Parcelize
//@TypeConverters(Converters::class)
//data class ProfileDataEntity(
//    @SerializedName("id") var id: Int,
//    @SerializedName("googleId") var googleId: Int? = null,
//    @SerializedName("name") var name: String? = null,
//    @SerializedName("surname") var surname: String? = null,
//    @SerializedName("city") var city: String? = null,
//    @SerializedName("age") var age: Int? = null,
//
//    @SerializedName("starSign") var starSign: String? = null,
//    @SerializedName("birthday") var birthday: LocalDate? = null,
//    @SerializedName("motto") var motto: String? = null,
//    @SerializedName("description") var description: String? = null,
//
//    @SerializedName("email") var email: String? = null,
//    @SerializedName("photoUrl") var photoUrl: Uri? = null,
//    @SerializedName("interests") var interests: List<String>? = null,
//    @SerializedName("peopleToMeet") var peopleToMeet: Int? = null,
//    @SerializedName("gender") var gender: String? = null,
//) : Parcelable {
//
//    constructor() : this(0)
//
//    fun toEntity() = ProfileEntity(
//        id,
//        googleId,
//        name,
//        surname,
//        city,
//        age,
//        starSign,
//        birthday,
//        motto,
//        description,
//        email,
//        photoUrl,
//        interests,
//        peopleToMeet,
//        gender
//    )
//
//    companion object {
//        fun fromEntity(entity: ProfileEntity) = with(entity) {
//            ProfileDataEntity(
//                id,
//                googleId,
//                name,
//                surname,
//                city,
//                age,
//                starSign,
//                birthday,
//                motto,
//                description,
//                email,
//                photoUrl,
//                interests,
//                peopleToMeet,
//                gender
//            )
//        }
//    }
//}

/**
 * Data-layer entity of profile.
 */
@Parcelize
@TypeConverters(Converters::class)
data class ProfileDataEntity(
    @SerializedName("name") var name: String? = null,
    @SerializedName("googleId") var googleId: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("interest") var interest: String? = null,
    @SerializedName("capacity") var capacity: Int? = null,
    @SerializedName("starSign") var starSign: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("mottoInLife") var mottoInLife: String? = null,
) : Parcelable {

    fun toEntity() = ProfileEntity(
        name,
        googleId,
        email,
        gender,
        description,
        interest,
        capacity,
        starSign,
        city,
        mottoInLife
    )

    companion object {
        fun fromEntity(entity: ProfileEntity) = with(entity) {
            ProfileDataEntity(
                name,
                googleId,
                email,
                gender,
                description,
                interest,
                capacity,
                starSign,
                city,
                mottoInLife
            )
        }
    }
}