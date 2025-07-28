package fr.vitesse.android.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class VitesseHttpClient {
    private val currencyApiUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json"
    private val instance: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
    }

    suspend fun getCurrencyApiResponse(): CurrencyApiResponse {
        val currencyApiResponse = instance.get(currencyApiUrl).body<CurrencyApiResponse>()
        return currencyApiResponse
    }
}
