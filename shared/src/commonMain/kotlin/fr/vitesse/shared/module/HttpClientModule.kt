package fr.vitesse.shared.module

import fr.vitesse.shared.model.CurrencyApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

private const val CURRENCY_API_URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json"

@Single
class HttpClientModule {
    private val instance: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
    }

    suspend fun getCurrencyApiResponse(): CurrencyApiResponse {
        val currencyApiResponse = instance.get(CURRENCY_API_URL).body<CurrencyApiResponse>()
        return currencyApiResponse
    }
}
