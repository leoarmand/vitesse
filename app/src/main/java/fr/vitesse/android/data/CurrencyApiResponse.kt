package fr.vitesse.android.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyApiResponse(
    @SerialName("eur")
    val eur: EurRates,
    val date: String
)
