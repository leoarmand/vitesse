package fr.vitesse.android.repository

import fr.vitesse.android.data.Candidate
import fr.vitesse.android.data.CandidateDao
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class CandidateRepository(
    private val dao: CandidateDao
) {
    fun getAll(): Flow<List<Candidate>> = dao.getAll()

    fun getCandidateFlowById(candidateId: Int) = dao.getCandidateFlowById(candidateId)

    suspend fun upsertCandidate(candidate: Candidate) {
        dao.upsert(candidate)
    }

    suspend fun getCandidateById(candidateId: Int) = dao.getCandidateById(candidateId)

    suspend fun toggleCandidateFavorite(candidateId: Int) {
        // ✅ utilise la version suspendue pour récupérer la valeur actuelle
        val retrievedCandidate = dao.getCandidateById(candidateId)
        val updated = retrievedCandidate.copy(isFavorite = !retrievedCandidate.isFavorite)
        dao.upsert(updated)
    }

    suspend fun deleteCandidate(candidate: Candidate) {
        dao.delete(candidate)
    }
}
