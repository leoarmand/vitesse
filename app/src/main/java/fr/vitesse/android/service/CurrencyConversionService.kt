package fr.vitesse.android.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class CurrencyConversionService {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    val url = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json"

    suspend fun convertEuroToPounds(amountInEur: Double): Double {
        val jsonElementResponse = client.get(url).body<JsonElement>()
        val eurListJsonObject = jsonElementResponse.jsonObject["eur"]?.jsonObject
        val gbpRate = eurListJsonObject?.get("gbp")?.jsonPrimitive?.double!!

        return amountInEur * gbpRate
    }
}
