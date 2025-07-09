package fr.vitesse.android.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.repository.CandidateRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository
) : ViewModel() {
    private val _candidates = mutableStateOf<List<Candidate>>(emptyList())
    val candidates: State<List<Candidate>> = _candidates

    init {
        viewModelScope.launch {
            _candidates.value = candidateRepository.getAll()
        }
    }
}
