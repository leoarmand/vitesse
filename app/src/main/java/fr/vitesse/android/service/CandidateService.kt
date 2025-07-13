package fr.vitesse.android.service

import fr.vitesse.android.data.Candidate
import fr.vitesse.android.repository.CandidateRepository
import javax.inject.Inject

class CandidateService @Inject constructor(
    private val repository: CandidateRepository
) {
    suspend fun initIfNeeded() {
        val existing = repository.getAll()
        if (existing.isEmpty()) repository.insertDefaultCandidates()
    }

    suspend fun getAll(): List<Candidate> = repository.getAll()

    suspend fun getCandidateById(candidateId: Long): Candidate = repository.getCandidateById(candidateId)
}
