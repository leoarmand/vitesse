package fr.vitesse.android.service

import fr.vitesse.android.data.CurrencyApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class CurrencyConversionService  (
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
    }
) {
    suspend fun convertEuroToPounds(amountInEur: Double): Double {
        val currencyApiResponse = httpClient.get("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json").body<CurrencyApiResponse>()
        val gbpRate = currencyApiResponse.eur.gbp

        return amountInEur * gbpRate
    }
}
