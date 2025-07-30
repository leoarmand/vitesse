package fr.vitesse.android.service

import fr.vitesse.android.data.Candidate
import fr.vitesse.android.repository.CandidateRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class CandidateService(private val candidateRepository: CandidateRepository) {
    fun getAll(): Flow<List<Candidate>> = candidateRepository.getAll()

    suspend fun getCandidateById(candidateId: Long): Candidate = candidateRepository.getCandidateById(candidateId)

    suspend fun toggleCandidateFavorite(candidateId: Long) {
        candidateRepository.toggleCandidateFavorite(candidateId)
    }

    suspend fun deleteCandidate(candidate: Candidate) {
        candidateRepository.deleteCandidate(candidate)
    }
}
