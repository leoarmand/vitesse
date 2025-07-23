package fr.vitesse.android.service

import fr.vitesse.android.data.CurrencyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Inject

class CurrencyConversionService  @Inject constructor(
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
) {
    val currenciesEurUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json"
    val gbpKey = "gbp"

    suspend fun convertEuroToPounds(amountInEur: Double): Double {
        val jsonElementResponse = httpClient.get(currenciesEurUrl).body<CurrencyResponse>()
        val gbpRate = jsonElementResponse.eur[gbpKey]!!

        return amountInEur * gbpRate
    }
}
