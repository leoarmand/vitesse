package fr.vitesse.android.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.service.CandidateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateDetailsViewModel @Inject constructor(
    private val candidateService: CandidateService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _candidate = MutableStateFlow<Candidate?>(null)
    val candidate: StateFlow<Candidate?> = _candidate

    init {
        val candidateId = savedStateHandle.get<Long>("candidateId")
        viewModelScope.launch {
            if (candidateId != null) {
                _candidate.value = candidateService.getCandidateById(candidateId)
            }
        }
    }
}
