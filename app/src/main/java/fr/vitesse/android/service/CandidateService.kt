package fr.vitesse.android.service

import fr.vitesse.android.data.Candidate
import fr.vitesse.android.repository.CandidateRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class CandidateService(private val candidateRepository: CandidateRepository) {
    fun getAll(): Flow<List<Candidate>> = candidateRepository.getAll()

    fun getCandidateFlowById(candidateId: Int) = candidateRepository.getCandidateFlowById(candidateId)

    suspend fun upsertCandidate(candidate: Candidate) {
        candidateRepository.upsertCandidate(candidate)
    }

    suspend fun getCandidateById(candidateId: Int) = candidateRepository.getCandidateById(candidateId)

    suspend fun toggleCandidateFavorite(candidateId: Int) {
        candidateRepository.toggleCandidateFavorite(candidateId)
    }

    suspend fun deleteCandidate(candidate: Candidate) {
        candidateRepository.deleteCandidate(candidate)
    }
}
