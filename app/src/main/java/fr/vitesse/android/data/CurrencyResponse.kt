package fr.vitesse.android.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Eur(
    @SerialName("gbp")
    val gbp: Double
)

@Serializable
data class CurrencyResponse(
    @SerialName("eur")
    val eur: Eur
)
