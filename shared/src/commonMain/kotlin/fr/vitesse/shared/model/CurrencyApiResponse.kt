package fr.vitesse.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyApiResponse(
    @SerialName("eur")
    val eur: EurRates
)
