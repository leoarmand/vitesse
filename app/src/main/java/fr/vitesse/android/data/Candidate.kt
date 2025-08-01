package fr.vitesse.android.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.ColumnInfo
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
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "birthday")
    val birthday: LocalDate,
    @ColumnInfo(name = "salary")
    val salary: Double,
    @ColumnInfo(name = "note")
    val note: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "avatar_path")
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
