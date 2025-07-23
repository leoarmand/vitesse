package fr.vitesse.android.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    @SerialName("eur")
    val eur: Map<String, Double>,
    val date: String
)
