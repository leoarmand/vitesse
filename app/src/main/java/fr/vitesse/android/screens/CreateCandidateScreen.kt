package fr.vitesse.android.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import fr.vitesse.android.R
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.viewmodel.CreateCandidateViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCandidateScreen(
    modifier: Modifier = Modifier,
    backStackEntry: NavBackStackEntry,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val createCandidateViewModel: CreateCandidateViewModel =
        koinViewModel(viewModelStoreOwner = backStackEntry)
    val collectedCandidate by createCandidateViewModel.candidate.collectAsStateWithLifecycle()
    val candidate = collectedCandidate

    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }
    val phoneNumber = rememberSaveable { mutableStateOf("") }

    candidate?.let {
        firstName.value = it.firstName
        lastName.value = it.lastName
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.add_candidate))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(percent = 50),
                onClick = {
                    //val regex = Regex("^[+]?[0-9]{6,15}$")
                    //if (!regex.matches(phoneNumber.value)) {
                    //if (verifyAndCreateCandidate(firstName.value, snackbarHostState, scope, context)) {
                        onSaveClick()
                    //}
                }
            ) {
                Text(
                    text = stringResource(id = R.string.save)
                )
            }
        }
    ) { contentPadding ->
        CreateCandidate(
            modifier = Modifier.padding(contentPadding),
            firstName = firstName.value,
            onFirstNameChanged = { firstName.value = it },
            lastName = lastName.value,
            onLastNameChanged = { lastName.value = it },
            phoneNumber = phoneNumber.value,
            onPhoneChanged = { phoneNumber.value = it }
        )
    }
}

fun verifyAndCreateCandidate(
    candidate: Candidate?,
    firstName: String,
    lastName: String,
    age: String,
    weight: String,
    height: String,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    context: Context
): Boolean
{

    val animalAge: Int;

    /*CandidateService.upsertCandidate(
        Candidate(
            candidate?.id ?: UUID.randomUUID(),
            firstName,
            lastName
        )
    )*/

    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCandidate(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(bottom = 88.dp, top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Row {
            Icon(
                modifier = Modifier
                    .padding(top = 8.dp, end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Outlined.Person,
                contentDescription = stringResource(id = R.string.candidate)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstName,
                onValueChange = { onFirstNameChanged(it) },
                label = { Text(stringResource(id = R.string.first_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer( modifier = Modifier.padding(end = 8.dp).size(32.dp) )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = lastName,
                onValueChange = { onLastNameChanged(it) },
                label = { Text(stringResource(id = R.string.last_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 8.dp, end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Outlined.Phone,
                contentDescription = stringResource(id = R.string.phone)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phoneNumber,
                onValueChange = { onPhoneChanged(it) },
                label = { Text(stringResource(id = R.string.phone)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = false,
                singleLine = true
            )
        }
    }
}
