package fr.vitesse.android.repository

import fr.vitesse.android.data.Candidate
import fr.vitesse.android.data.CandidateDao
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class CandidateRepository(
    private val dao: CandidateDao
) {
    suspend fun insertDefaultCandidates() {
        val defaultNote = "Supporting line text lorem ipsum dolor site amet, consectetur. Dolor site amet consectetur."
        val candidates = listOf(
            Candidate(email = "alice.durand@gmail.com", phoneNumber = "0775281822", firstName = "Alice", lastName = "Durand", birthday = LocalDate.of(1990, 11, 6), salary = 1550.0, note = defaultNote),
            Candidate(email = "bob.martin@gmail.com", phoneNumber = "0775281823", firstName = "Bob", lastName = "Martin", birthday = LocalDate.of(1990, 11, 7), salary = 1650.0, note = defaultNote, isFavorite = true),
            Candidate(email = "clara.petit@gmail.com", phoneNumber = "0775281824", firstName = "Clara", lastName = "Petit", birthday = LocalDate.of(1990, 11, 9), salary = 1750.0, note = defaultNote)
        )
        dao.insertAll(candidates)
    }

    fun getAll(): Flow<List<Candidate>> = dao.getAll()

    suspend fun getCandidateById(candidateId: Long): Candidate = dao.getCandidateById(candidateId)

    suspend fun toggleCandidateFavorite(candidateId: Long) {
        val retrievedCandidate = dao.getCandidateById(candidateId)
        val updated = retrievedCandidate.copy(isFavorite = !retrievedCandidate.isFavorite)
        dao.upsert(updated)
    }

    suspend fun deleteCandidate(candidate: Candidate) {
        dao.delete(candidate)
    }
}
