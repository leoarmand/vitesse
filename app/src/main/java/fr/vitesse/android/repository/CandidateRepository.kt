package fr.vitesse.android.repository

import fr.vitesse.android.data.Candidate
import fr.vitesse.android.data.CandidateDao
import javax.inject.Inject

class CandidateRepository @Inject constructor(
    private val dao: CandidateDao
) {
    suspend fun insertDefaultCandidates() {
        val defaultPhoneNumber = "0775281822"
        val defaultNote = "Supporting line text lorem ipsum dolor site amet, consectetur. Dolor site amet consectetur."
        val candidates = listOf(
            Candidate(email = "alice.durand@gmail.com", phoneNumber = defaultPhoneNumber, firstName = "Alice", lastName = "Durand", note = defaultNote),
            Candidate(email = "bob.martin@gmail.com", phoneNumber = defaultPhoneNumber, firstName = "Bob", lastName = "Martin", note = defaultNote),
            Candidate(email = "clara.petit@gmail.com", phoneNumber = defaultPhoneNumber, firstName = "Clara", lastName = "Petit", note = defaultNote)
        )
        dao.insertAll(candidates)
    }

    suspend fun getAll(): List<Candidate> = dao.getAll()
}
