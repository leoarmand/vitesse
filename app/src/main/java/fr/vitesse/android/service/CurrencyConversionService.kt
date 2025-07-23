package fr.vitesse.android.service

import fr.vitesse.android.data.CurrencyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class CurrencyConversionService {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    val currenciesEurUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json"

    suspend fun convertEuroToPounds(amountInEur: Double): Double {
        val jsonElementResponse = client.get(currenciesEurUrl).body<CurrencyResponse>()
        val gbpRate = jsonElementResponse.eur["gbp"]!!

        return amountInEur * gbpRate
    }
}
