package fr.vitesse.android.viewmodel

import androidx.lifecycle.SavedStateHandle
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.service.CandidateService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.Runs
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CreateCandidateViewModelTest {

    private lateinit var candidateService: CandidateService
    private lateinit var savedStateHandle: SavedStateHandle

    private val sampleCandidate = Candidate(
        id = 1,
        email = "test@example.com",
        phoneNumber = "0123456789",
        firstName = "John",
        lastName = "Doe",
        birthday = System.currentTimeMillis(),
        salary = 45000.0,
        note = "Some note",
        isFavorite = false,
        avatarPath = null
    )

    @Before
    fun setUp() {
        candidateService = mockk()
    }

    @Test
    fun initLoadsCandidateIfCandidateIdProvided() = runTest {
        coEvery { candidateService.getCandidateById(1) } returns sampleCandidate
        savedStateHandle = SavedStateHandle(mapOf("candidateId" to 1))

        val viewModel = CreateCandidateViewModel(candidateService, savedStateHandle)

        val result = viewModel.candidate.first()
        Assert.assertEquals(sampleCandidate, result)
        coVerify { candidateService.getCandidateById(1) }
    }

    @Test
    fun initDoesNothingIfCandidateIdNotProvided() = runTest {
        savedStateHandle = SavedStateHandle()

        val viewModel = CreateCandidateViewModel(candidateService, savedStateHandle)

        val result = viewModel.candidate.first()
        Assert.assertNull(result)
        coVerify(exactly = 0) { candidateService.getCandidateById(any()) }
    }

    @Test
    fun upsertCandidateUsingService() = runTest {
        coEvery { candidateService.upsertCandidate(sampleCandidate) } just Runs
        savedStateHandle = SavedStateHandle()
        val viewModel = CreateCandidateViewModel(candidateService, savedStateHandle)

        viewModel.upsertCandidate(sampleCandidate)

        coVerify { candidateService.upsertCandidate(sampleCandidate) }
    }

    @Test
    fun verifyAndCreateCandidate() = runTest {
        savedStateHandle = SavedStateHandle()
        val viewModel = CreateCandidateViewModel(candidateService, savedStateHandle)

        val shouldBeTrueRes = viewModel.verifyAndCreateCandidate(
            candidateId = sampleCandidate.id,
            avatarPath = sampleCandidate.avatarPath,
            firstName = sampleCandidate.firstName,
            lastName = sampleCandidate.lastName,
            phoneNumber = sampleCandidate.phoneNumber,
            email = sampleCandidate.email,
            birthday = sampleCandidate.birthday,
            salary = sampleCandidate.salary,
            note = sampleCandidate.note
        )

        Assert.assertTrue(shouldBeTrueRes)

        val shouldBeFalseRes = viewModel.verifyAndCreateCandidate(
            candidateId = 1,
            avatarPath = null,
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "0123456789",
            email = "test@example.com",
            birthday = null,
            salary = null,
            note = null
        )

        Assert.assertFalse(shouldBeFalseRes)
    }
}
