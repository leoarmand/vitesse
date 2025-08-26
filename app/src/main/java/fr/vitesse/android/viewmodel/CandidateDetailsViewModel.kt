package fr.vitesse.android.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.module.ActionComposerModule
import fr.vitesse.shared.module.HttpClientModule
import fr.vitesse.android.service.CandidateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CandidateDetailsViewModel (
    private val candidateService: CandidateService,
    private val httpClientModule: HttpClientModule,
    private val candidateActionComposerModule: ActionComposerModule,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _candidate = MutableStateFlow<Candidate?>(null)
    val candidate: StateFlow<Candidate?> = _candidate.asStateFlow()

    init {
        val candidateId: Int? = savedStateHandle["candidateId"]
        if (candidateId != null) {
            viewModelScope.launch {
                candidateService.getCandidateFlowById(candidateId).collect { updatedCandidate ->
                    _candidate.value = updatedCandidate
                }
            }
        }
    }

    fun toggleCandidateFavorite(candidateId: Int) {
        viewModelScope.launch {
            candidateService.toggleCandidateFavorite(candidateId)
        }
    }

    fun deleteCandidate () {
        viewModelScope.launch {
            _candidate.value?.let {
                candidateService.deleteCandidate(_candidate.value!!)
            }
        }
    }

    suspend fun convertEuroToPounds(amountInEur: Double): Double {
        return amountInEur * httpClientModule.getCurrencyApiResponse().eur.gbp
    }

    fun callCandidate() {
        candidateActionComposerModule.call(_candidate.value!!.phoneNumber)
    }

    fun sendSmsToCandidate() {
        candidateActionComposerModule.sendSms(_candidate.value!!.phoneNumber)
    }

    fun sendEmailToCandidate() {
        candidateActionComposerModule.sendEmail(_candidate.value!!.email)
    }
}
