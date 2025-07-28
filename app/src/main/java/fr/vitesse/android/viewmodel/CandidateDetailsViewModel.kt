package fr.vitesse.android.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.service.CandidateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CandidateDetailsViewModel (
    private val candidateService: CandidateService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _candidate = MutableStateFlow<Candidate?>(null)
    val candidate: StateFlow<Candidate?> = _candidate

    init {
        val candidateId: Long? = savedStateHandle["candidateId"]
        if (candidateId != null) {
            viewModelScope.launch {
                _candidate.value = candidateService.getCandidateById(candidateId)
            }
        }
    }

    fun toggleCandidateFavorite(
        candidateId: Long
    ) {
        viewModelScope.launch {
            _candidate.value?.let {
                candidateService.toggleCandidateFavorite(candidateId)
                val updated = candidateService.getCandidateById(candidateId)
                _candidate.value = updated
            }
        }
    }

    fun deleteCandidate () {
        viewModelScope.launch {
            _candidate.value?.let {
                candidateService.deleteCandidate(_candidate.value!!)
            }
        }
    }
}
