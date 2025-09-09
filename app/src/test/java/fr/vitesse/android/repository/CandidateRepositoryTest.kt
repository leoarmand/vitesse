package fr.vitesse.android.repository

import fr.vitesse.android.data.Candidate
import fr.vitesse.android.data.CandidateDao
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.every
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.Runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CandidateRepositoryTest {
    private lateinit var dao: CandidateDao
    private lateinit var repository: CandidateRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dao = mockk(relaxed = true)
        repository = CandidateRepository(dao)
    }

    @Test
    fun getAllFlowOfCandidates() = runTest {
        val candidates = listOf(
            Candidate(1, "test1@mail.com", "1234", "John", "Doe", 0L, null, null, false, null),
            Candidate(2, "test2@mail.com", "5678", "Jane", "Smith", 0L, null, null, true, null),
        )
        every { dao.getAll() } returns flowOf(candidates)

        val result = repository.getAll().first()

        Assert.assertEquals(candidates, result)
    }

    @Test
    fun getCandidateFlowById() = runTest {
        val candidate = Candidate(1, "test@mail.com", "1234", "John", "Doe", 0L, null, null, false, null)
        every { dao.getCandidateFlowById(any()) } returns flowOf(candidate)

        val result = repository.getCandidateFlowById(1).first()

        Assert.assertEquals(candidate, result)
    }

    @Test
    fun upsertCandidateCallsDao() = runTest {
        val candidate = Candidate(1, "test@mail.com", "1234", "John", "Doe", 0L, null, null, false, null)
        coEvery { dao.upsert(candidate) } just Runs

        repository.upsertCandidate(candidate)

        coVerify { dao.upsert(candidate) }
    }

    @Test
    fun getCandidateById() = runTest {
        val candidate = Candidate(1, "test@mail.com", "1234", "John", "Doe", 0L, null, null, false, null)
        coEvery { dao.getCandidateById(any()) } returns candidate

        val result = repository.getCandidateById(candidate.id)

        Assert.assertEquals(candidate, result)
    }

    @Test
    fun toggleCandidateFavoriteStatus() = runTest {
        val candidate = Candidate(1, "test@mail.com", "1234", "John", "Doe", 0L, null, null, false, null)
        val updated = candidate.copy(isFavorite = true)
        coEvery { dao.getCandidateById(any()) } returns candidate
        coEvery { dao.upsert(updated) } just Runs

        repository.toggleCandidateFavorite(candidate.id)

        coVerify { dao.upsert(updated) }
    }

    @Test
    fun toggleCandidateFavoriteIfCandidateNotFound() = runTest {
        coEvery { dao.getCandidateById(any()) } returns null

        repository.toggleCandidateFavorite(999)

        coVerify(exactly = 0) { dao.upsert(any()) }
    }

    @Test
    fun deleteCandidateCallsDaoDelete() = runTest {
        val candidate = Candidate(1, "test@mail.com", "1234", "John", "Doe", 0L, null, null, false, null)
        coEvery { dao.delete(any()) } just Runs

        repository.deleteCandidate(candidate)

        coVerify { dao.delete(candidate) }
    }
}
