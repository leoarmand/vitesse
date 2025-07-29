package fr.vitesse.android.module

import fr.vitesse.android.http.VitesseHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val httpClientModule = module {
    singleOf(::VitesseHttpClient)
}
