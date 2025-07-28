package fr.vitesse.android.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EurRates(
    @SerialName("gbp")
    val gbp: Double
)
