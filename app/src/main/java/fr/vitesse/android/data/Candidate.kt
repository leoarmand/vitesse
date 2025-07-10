package fr.vitesse.android.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(tableName = "candidates")
data class Candidate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val salary: Double,
    val note: String,
    val isFavorite: Boolean = false,
    val avatarPath: String? = null,
) : Serializable {
    val fullName: String get() = "$firstName ${lastName.uppercase()}"
}
