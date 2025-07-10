package fr.vitesse.android.data

import androidx.compose.runtime.Composable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "candidates")
data class Candidate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val note: String,
    val isFavorite: Boolean = false
) : Serializable {
    val fullName: String get() = "$firstName ${lastName.uppercase()}"
}
