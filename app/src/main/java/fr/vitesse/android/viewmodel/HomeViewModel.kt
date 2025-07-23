package fr.vitesse.android.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.vitesse.android.service.CandidateService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    candidateService: CandidateService
) : ViewModel() {
    val candidates =
        candidateService.getAll()
}
