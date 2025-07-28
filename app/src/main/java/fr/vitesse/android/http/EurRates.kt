package fr.vitesse.android.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EurRates(
    @SerialName("gbp")
    val gbp: Double
)
