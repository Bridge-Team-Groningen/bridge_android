package nl.totowka.bridge.data.model

import android.os.Parcelable
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import nl.totowka.bridge.domain.model.ProfileEntity

/**
 * Data-layer entity of profile.
 */
@Parcelize
@TypeConverters(Converters::class)
data class ProfileDataEntity(
    @SerializedName("name") var name: String? = null,
    @SerializedName("googleId") var googleId: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("age") var age: Int? = null,
    @SerializedName("profilePicture") var profilePicture: String? = null,
    @SerializedName("hobbies") var hobbies: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("interestList") var interestList: String? = null,
    @SerializedName("capacity") var capacity: Int? = null,
    @SerializedName("starSign") var starSign: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("mottoInLife") var mottoInLife: String? = null,
    @SerializedName("questionWifi") var questionWifi: String? = null
) : Parcelable {

    fun toEntity() = ProfileEntity(
        name,
        googleId,
        email,
        age,
        profilePicture,
        hobbies,
        gender,
        description,
        interestList,
        capacity,
        starSign,
        city,
        mottoInLife,
        questionWifi
    )

    companion object {
        fun fromEntity(entity: ProfileEntity) = with(entity) {
            ProfileDataEntity(
                name,
                googleId,
                email,
                age,
                profilePicture,
                hobbies,
                gender,
                description,
                interestList,
                capacity,
                starSign,
                city,
                mottoInLife,
                questionWifi
            )
        }
    }
}

@Parcelize
@TypeConverters(Converters::class)
data class Like(@SerializedName("message") var message: Boolean? = null) : Parcelable