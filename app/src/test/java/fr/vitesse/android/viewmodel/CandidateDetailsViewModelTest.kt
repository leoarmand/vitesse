package fr.vitesse.android.viewmodel

import androidx.lifecycle.SavedStateHandle
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.module.ActionComposerModule
import fr.vitesse.android.service.CandidateService
import fr.vitesse.shared.model.CurrencyApiResponse
import fr.vitesse.shared.module.HttpClientModule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.Runs
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CandidateDetailsViewModelTest {
    private lateinit var candidateService: CandidateService
    private lateinit var httpClientModule: HttpClientModule
    private lateinit var actionComposerModule: ActionComposerModule
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: CandidateDetailsViewModel

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
        httpClientModule = mockk()
        actionComposerModule = mockk()
        savedStateHandle = SavedStateHandle(mapOf("candidateId" to sampleCandidate.id))

        every { candidateService.getCandidateFlowById(any()) } returns MutableStateFlow(sampleCandidate)

        viewModel = CandidateDetailsViewModel(
            candidateService,
            httpClientModule,
            actionComposerModule,
            savedStateHandle
        )
    }

    @Test
    fun shouldLoadCandidateOnInit() = runTest {
        val candidate = viewModel.candidate.first()
        Assert.assertNotNull(candidate)
        Assert.assertEquals("John", candidate?.firstName)
    }

    @Test
    fun shouldToggleFavorite() = runTest {
        coEvery { candidateService.toggleCandidateFavorite(any()) } just Runs

        viewModel.toggleCandidateFavorite(sampleCandidate.id)

        coVerify { candidateService.toggleCandidateFavorite(sampleCandidate.id) }
    }

    @Test
    fun shouldDeleteCandidate() = runTest {
        coEvery { candidateService.deleteCandidate(any()) } just Runs

        viewModel = CandidateDetailsViewModel(
            candidateService,
            httpClientModule,
            actionComposerModule,
            SavedStateHandle()
        )
        viewModel.setCandidateForTest(sampleCandidate)
        viewModel.deleteCandidate()

        coVerify { candidateService.deleteCandidate(sampleCandidate) }
    }

    @Test
    fun shouldConvertEuroToPounds() = runTest {
        val currencyResponse = mockk<CurrencyApiResponse>()
        every { currencyResponse.eur.gbp } returns 0.86
        coEvery { httpClientModule.getCurrencyApiResponse() } returns currencyResponse

        val result = viewModel.convertEuroToPounds(100.0)
        Assert.assertEquals(86.0, result, 0.01)
    }

    @Test
    fun shouldCallCandidate() {
        every { actionComposerModule.call(any()) } just Runs

        viewModel.setCandidateForTest(sampleCandidate)
        viewModel.callCandidate()

        verify { actionComposerModule.call("0123456789") }
    }

    @Test
    fun shouldSendSmsToCandidate() {
        every { actionComposerModule.sendSms(any()) } just Runs

        viewModel.setCandidateForTest(sampleCandidate)
        viewModel.sendSmsToCandidate()

        verify { actionComposerModule.sendSms("0123456789") }
    }

    @Test
    fun shouldSendEmailToCandidate() {
        every { actionComposerModule.sendEmail(any()) } just Runs

        viewModel.setCandidateForTest(sampleCandidate)
        viewModel.sendEmailToCandidate()

        verify { actionComposerModule.sendEmail("test@example.com") }
    }
}
