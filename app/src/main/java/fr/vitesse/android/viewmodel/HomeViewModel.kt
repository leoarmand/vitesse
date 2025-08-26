package fr.vitesse.android.viewmodel

import androidx.lifecycle.ViewModel
import fr.vitesse.android.service.CandidateService
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel (
    candidateService: CandidateService
) : ViewModel() {
    val candidates = candidateService.getAll()
}
