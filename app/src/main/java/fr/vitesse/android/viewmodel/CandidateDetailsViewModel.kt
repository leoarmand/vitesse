package fr.vitesse.android.viewmodel

import androidx.annotation.VisibleForTesting
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
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@KoinViewModel
class CandidateDetailsViewModel (
    private val candidateService: CandidateService,
    private val httpClientModule: HttpClientModule,
    private val candidateActionComposerModule: ActionComposerModule,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _candidate = MutableStateFlow<Candidate?>(null)
    private val _formattedPoundSalary = MutableStateFlow<String?>(null)
    val candidate: StateFlow<Candidate?> = _candidate.asStateFlow()
    val formattedPoundSalary: StateFlow<String?> = _formattedPoundSalary.asStateFlow()

    init {
        val candidateId: Int? = savedStateHandle["candidateId"]
        if (candidateId != null) {
            viewModelScope.launch {
                candidateService.getCandidateFlowById(candidateId).collect { updatedCandidate ->
                    _candidate.value = updatedCandidate
                    if (updatedCandidate != null && updatedCandidate.salary != null) {
                        val poundSalary = convertEuroToPounds(updatedCandidate.salary)
                        val poundLocale = Locale.UK
                        val formatter = NumberFormat.getCurrencyInstance(poundLocale).apply {
                            this.currency = Currency.getInstance(poundLocale)
                        }
                        _formattedPoundSalary.value = formatter.format(poundSalary)
                    }
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

    fun callCandidate() {
        candidateActionComposerModule.call(_candidate.value!!.phoneNumber)
    }

    fun sendSmsToCandidate() {
        candidateActionComposerModule.sendSms(_candidate.value!!.phoneNumber)
    }

    fun sendEmailToCandidate() {
        candidateActionComposerModule.sendEmail(_candidate.value!!.email)
    }

    @VisibleForTesting
    suspend fun convertEuroToPounds(amountInEur: Double): Double {
        try {
            val salaryInPounds = amountInEur * httpClientModule.getCurrencyApiResponse().eur.gbp
            return salaryInPounds
        } catch (_: Exception) {
            val fallbackSalaryInPounds = amountInEur * 0.86
            return fallbackSalaryInPounds
        }
    }

    @VisibleForTesting
    fun setCandidateForTest(candidate: Candidate) {
        _candidate.value = candidate
    }
}
