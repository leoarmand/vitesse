package fr.vitesse.android.viewmodel

import android.util.Patterns
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
class CreateCandidateViewModel(
    private val candidateService: CandidateService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _candidate = MutableStateFlow<Candidate?>(null)
    val candidate: StateFlow<Candidate?> = _candidate

    init {
        val candidateId: Int? = savedStateHandle["candidateId"]
        if (candidateId != null) {
            viewModelScope.launch {
                _candidate.value = candidateService.getCandidateById(candidateId)
            }
        }
    }

    fun upsertCandidate(candidate: Candidate) {
        viewModelScope.launch {
            candidateService.upsertCandidate(candidate)
        }
    }
}

fun isValidPhoneNumber(phoneNumber: String): Boolean {
    return Patterns.PHONE.matcher(phoneNumber).matches()
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
