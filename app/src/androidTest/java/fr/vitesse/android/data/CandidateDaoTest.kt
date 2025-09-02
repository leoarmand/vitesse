package fr.vitesse.android.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.vitesse.android.module.dbName
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CandidateDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var candidateDao: CandidateDao

    @Before
    fun setUp() {
        database = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
            dbName
        ).build()

        candidateDao = database.candidateDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    private fun sampleCandidate(
        id: Int = 1,
        email: String = "test@example.com",
        phoneNumber: String = "1234567890",
        firstName: String = "John",
        lastName: String = "Doe",
        birthday: Long = System.currentTimeMillis() - 25L * 365 * 24 * 60 * 60 * 1000, //25 years old
        salary: Double? = 45000.0,
        note: String? = "Some note",
        isFavorite: Boolean = false,
        avatarPath: String? = null
    ) = Candidate(
        id = id,
        email = email,
        phoneNumber = phoneNumber,
        firstName = firstName,
        lastName = lastName,
        birthday = birthday,
        salary = salary,
        note = note,
        isFavorite = isFavorite,
        avatarPath = avatarPath
    )

    @Test
    fun upsertAndGetCandidateByIdUpsertedCandidate() = runTest {
        val candidate = sampleCandidate()

        candidateDao.upsert(candidate)
        val upsertedCandidate = candidateDao.getAll().first().first()

        Assert.assertEquals(candidate.email, upsertedCandidate.email)
        Assert.assertEquals(candidate.firstName, upsertedCandidate.firstName)
        Assert.assertEquals(candidate.lastName, upsertedCandidate.lastName)
        Assert.assertEquals(candidate.phoneNumber, upsertedCandidate.phoneNumber)
    }

    @Test
    fun deleteCandidate() = runTest {
        val candidate = sampleCandidate()

        candidateDao.upsert(candidate)
        val inserted = candidateDao.getAll().first().first()

        candidateDao.delete(inserted)
        val result = candidateDao.getCandidateById(inserted.id)

        Assert.assertNull(result)
    }

    @Test
    fun getAllCandidates() = runTest {
        val candidate1 = sampleCandidate(email = "a@example.com", firstName = "Alice", id = 2)
        val candidate2 = sampleCandidate(email = "b@example.com", firstName = "Bob", id = 3)

        candidateDao.upsert(candidate1)
        candidateDao.upsert(candidate2)

        val candidates = candidateDao.getAll().first()
        Assert.assertEquals(2, candidates.size)
        Assert.assertTrue(candidates.any { it.email == "a@example.com" })
        Assert.assertTrue(candidates.any { it.email == "b@example.com" })
    }

    @Test
    fun getCandidateFlowById() = runTest {
        val candidate = sampleCandidate(firstName = "Charlie", email = "charlie@example.com")
        candidateDao.upsert(candidate)
        val inserted = candidateDao.getAll().first().first()

        val result = candidateDao.getCandidateFlowById(inserted.id).first()
        Assert.assertNotNull(result)
        Assert.assertEquals("Charlie", result?.firstName)
    }
}
