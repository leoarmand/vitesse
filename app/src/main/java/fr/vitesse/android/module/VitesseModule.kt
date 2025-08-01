package fr.vitesse.android.module

import android.content.Context
import fr.vitesse.android.repository.CandidateRepository
import fr.vitesse.android.service.CandidateService
import fr.vitesse.android.viewmodel.CandidateDetailsViewModel
import fr.vitesse.android.viewmodel.CreateCandidateViewModel
import fr.vitesse.android.viewmodel.HomeViewModel
import fr.vitesse.shared.module.HttpClientModule
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeViewModelModule = module {
    viewModelOf(::HomeViewModel)
}

val candidateRepositoryModule = module {
    singleOf(::CandidateRepository)
}

val candidateServiceModule = module {
    singleOf(::CandidateService)
}

val candidateDetailsViewModelModule = module {
    viewModelOf(::CandidateDetailsViewModel)
}

val createCandidateViewModelModule = module {
    viewModelOf(::CreateCandidateViewModel)
}

val httpClientModule = module {
    singleOf(::HttpClientModule)
}

fun vitesseModules(applicationContext: Context) = listOf(
    DatabaseModule(applicationContext).module,
    ActionComposerModule(applicationContext).module,
    httpClientModule,
    candidateRepositoryModule,
    candidateServiceModule,
    candidateDetailsViewModelModule,
    createCandidateViewModelModule,
    homeViewModelModule
)
