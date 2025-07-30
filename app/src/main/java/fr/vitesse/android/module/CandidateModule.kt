package fr.vitesse.android.module

import fr.vitesse.android.repository.CandidateRepository
import fr.vitesse.android.service.CandidateService
import fr.vitesse.android.viewmodel.CandidateDetailsViewModel
import fr.vitesse.android.viewmodel.CreateCandidateViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

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
