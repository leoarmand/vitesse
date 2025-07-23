package fr.vitesse.android.service

import fr.vitesse.android.data.Candidate
import fr.vitesse.android.repository.CandidateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CandidateService @Inject constructor(
    private val candidateRepository: CandidateRepository
) {
    suspend fun initIfNeeded() {
        //val candidatesFlow = candidateRepository.getAll()
        candidateRepository.insertDefaultCandidates()
        /*candidatesFlow.collect {
            candidates ->
                {
                    if (candidates.isEmpty()) {
                    }
                }
        }*/
    }

    fun getAll(): Flow<List<Candidate>> = candidateRepository.getAll()

    suspend fun getCandidateById(candidateId: Long): Candidate = candidateRepository.getCandidateById(candidateId)

    suspend fun toggleCandidateFavorite(candidate: Candidate) {
        candidateRepository.toggleCandidateFavorite(candidate)
    }

    suspend fun deleteCandidate(candidate: Candidate) {
        candidateRepository.deleteCandidate(candidate)
    }
}
