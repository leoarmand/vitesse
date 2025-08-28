package fr.vitesse.android.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.vitesse.android.R
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.ExperimentalTime

@Entity(tableName = "candidates")
data class Candidate(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "birthday")
    val birthday: Long,
    @ColumnInfo(name = "salary")
    val salary: Double?,
    @ColumnInfo(name = "note")
    val note: String?,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "avatar_path")
    val avatarPath: String?,
) : Serializable {
    val fullName get() = "$firstName ${lastName.uppercase()}"

    @OptIn(ExperimentalTime::class)
    @Composable
    fun formatDateWithAge(): String {
        val birthDate = Calendar.getInstance().apply {
            timeInMillis = birthday
        }

        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        val formattedDate = SimpleDateFormat(
            stringResource(id = R.string.birthday_pattern),
            Locale.getDefault()
        ).format(Date(birthday))

        val ageLabel = "$age " + stringResource(id = R.string.years).lowercase()

        return "$formattedDate ($ageLabel)"
    }
}
