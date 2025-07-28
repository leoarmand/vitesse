package fr.vitesse.android.module

import android.content.Context
import androidx.room.Room
import fr.vitesse.android.http.VitesseHttpClient
import fr.vitesse.android.data.AppDatabase
import fr.vitesse.android.repository.CandidateRepository
import fr.vitesse.android.service.CandidateService
import fr.vitesse.android.viewmodel.CandidateDetailsViewModel
import fr.vitesse.android.viewmodel.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class AppModule (appContext: Context) {
    val appDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "vitesse-db"
    ).build()

    val modules = module {
        single{ appDatabase }
        single{ appDatabase.candidateDao() }
        single { ActionComposerModule(appContext) }
        singleOf(::VitesseHttpClient)
        singleOf(::CandidateRepository)
        singleOf(::CandidateService)

        viewModelOf(::CandidateDetailsViewModel)
        viewModelOf(::HomeViewModel)
    }
}
