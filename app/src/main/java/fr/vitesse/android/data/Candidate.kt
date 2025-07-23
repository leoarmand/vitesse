package fr.vitesse.android.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.vitesse.android.R
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

    @Composable
    fun formatDateWithAge(): String {
        val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())
        val formattedDate = birthday.format(DateTimeFormatter.ofPattern(stringResource(id = R.string.birthday_pattern)))
        val ageLabel = "$age " + stringResource(id = R.string.years).lowercase()

        return "$formattedDate ($ageLabel)"
    }
}
