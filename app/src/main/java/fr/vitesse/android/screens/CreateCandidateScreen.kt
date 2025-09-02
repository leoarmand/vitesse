package fr.vitesse.android.screens

import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.vitesse.android.R
import fr.vitesse.android.components.AvatarComponent
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.viewmodel.CreateCandidateViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri
import fr.vitesse.android.viewmodel.isValidEmail
import fr.vitesse.android.viewmodel.isValidPhoneNumber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCandidateScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val createCandidateViewModel: CreateCandidateViewModel =
        koinViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val collectedCandidate by createCandidateViewModel.candidate.collectAsStateWithLifecycle()
    val candidate = collectedCandidate

    val titleText = candidate?.id?.let {
        stringResource(id = R.string.edit_candidate)
    } ?: stringResource(id = R.string.add_candidate)
    val avatarPath = rememberSaveable { mutableStateOf(candidate?.avatarPath) }
    val firstName = rememberSaveable { mutableStateOf(candidate?.firstName ?: "") }
    val lastName = rememberSaveable { mutableStateOf(candidate?.lastName ?: "") }
    val phoneNumber = rememberSaveable { mutableStateOf(candidate?.phoneNumber ?: "") }
    val email = rememberSaveable { mutableStateOf(candidate?.email ?: "") }
    val birthDay = rememberSaveable { mutableStateOf<Long?>(null) }
    val salary = rememberSaveable { mutableStateOf(candidate?.salary) }
    val note = rememberSaveable { mutableStateOf(candidate?.note) }
    val formValidationState = remember { mutableStateOf(false) }

    candidate?.let {
        avatarPath.value = it.avatarPath
        firstName.value = it.firstName
        lastName.value = it.lastName
        phoneNumber.value = it.phoneNumber
        email.value = it.email
        birthDay.value = it.birthday
        salary.value = it.salary
        note.value = it.note
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Text(titleText)
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
                    formValidationState.value = true

                    if (
                        verifyAndCreateCandidate(
                            createCandidateViewModel,
                            candidate?.id ?: 0,
                            avatarPath.value,
                            firstName.value,
                            lastName.value,
                            phoneNumber.value,
                            email.value,
                            birthDay.value,
                            salary.value,
                            note.value
                        ))
                    {
                        onSaveClick()
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.save)
                )
            }
        }
    ) { contentPadding ->
        CreateCandidateForm(
            modifier = Modifier.padding(contentPadding),
            avatarPath = avatarPath.value,
            firstName = firstName.value,
            lastName = lastName.value,
            email = email.value,
            birthDay = birthDay.value,
            salary = salary.value,
            note = note.value,
            onAvatarUriChanged = { avatarPath.value = it.toString() },
            onFirstNameChanged = { firstName.value = it },
            onLastNameChanged = { lastName.value = it },
            phoneNumber = phoneNumber.value,
            onPhoneChanged = { phoneNumber.value = it },
            onBirthDayChanged = { birthDay.value = it },
            onEmailChanged = { email.value = it },
            onSalaryChanged = { salary.value = it },
            onNoteChanged = { note.value = it },
            formValidationState = formValidationState
        )
    }
}

fun verifyAndCreateCandidate(
    createCandidateViewModel: CreateCandidateViewModel,
    candidateId: Int,
    avatarPath: String?,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    email: String,
    birthday: Long?,
    salary: Double?,
    note: String?
): Boolean {
    if (
        !(firstName.isNotBlank() &&
        lastName.isNotBlank() &&
        phoneNumber.isNotBlank() &&
        email.isNotBlank() && birthday != null)
    ) {
        return false
    }

    val candidateToUpsert = Candidate(
        id = candidateId,
        email = email,
        phoneNumber = phoneNumber,
        firstName = firstName,
        lastName = lastName,
        birthday = birthday,
        salary = salary,
        note = note,
        avatarPath = avatarPath
    )

    createCandidateViewModel.upsertCandidate(candidateToUpsert)

    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCandidateForm(
    modifier: Modifier = Modifier,
    avatarPath: String?,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    email: String,
    birthDay: Long?,
    salary: Double?,
    note: String?,
    onAvatarUriChanged: ((uri: Uri) -> Unit),
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onBirthDayChanged: (Long) -> Unit,
    onSalaryChanged: (Double) -> Unit,
    onNoteChanged: (String) -> Unit,
    formValidationState: MutableState<Boolean>
) {
    val scrollState = rememberScrollState()
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var birthDayError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    val formatErrorDesc: String = stringResource(id = R.string.invalid_format)
    val mandatoryStringDesc: String = stringResource(id = R.string.mandatory_field)
    var avatarUri: Uri? = null
    if (avatarPath?.isNotBlank() == true) {
        avatarUri = avatarPath.toUri()
    }

    LaunchedEffect(formValidationState.value) {
        if (formValidationState.value) {
            firstNameError = if (firstName.isBlank()) mandatoryStringDesc else null
            lastNameError = if (lastName.isBlank()) mandatoryStringDesc else null
            phoneNumberError = if (phoneNumber.isBlank()) mandatoryStringDesc else if (!isValidPhoneNumber(phoneNumber)) formatErrorDesc else null
            emailError = if (email.isBlank()) mandatoryStringDesc else if (!isValidEmail(email)) formatErrorDesc else null
            birthDayError = if (birthDay == null) mandatoryStringDesc else null

            formValidationState.value = false
        }
    }

    Column(
        modifier = modifier
            .padding(top = 0.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.size(4.dp))
        AvatarComponent(avatarUri = avatarUri, onAvatarUriChanged = onAvatarUriChanged)
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Icon(
                modifier = Modifier.padding(top = 24.dp, end = 16.dp),
                imageVector = Icons.Outlined.Person,
                contentDescription = stringResource(id = R.string.candidate)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstName,
                onValueChange = {
                    onFirstNameChanged(it)
                    firstNameError = if (firstName.isBlank()) mandatoryStringDesc else null
                },
                label = { Text(stringResource(id = R.string.first_name)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                isError = firstNameError != null,
                supportingText = {
                    firstNameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
        }

        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Spacer( modifier = Modifier
                .padding(end = 8.dp)
                .size(32.dp) )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = lastName,
                onValueChange = {
                    onLastNameChanged(it)
                    lastNameError = if (lastName.isBlank()) mandatoryStringDesc else null
                },
                label = { Text(stringResource(id = R.string.last_name)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                isError = lastNameError != null,
                supportingText = {
                    lastNameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
        }

        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                modifier = Modifier.padding(top = 24.dp, end = 16.dp),
                imageVector = Icons.Outlined.Phone,
                contentDescription = stringResource(id = R.string.phone)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phoneNumber,
                onValueChange = {
                    onPhoneChanged(it)
                    phoneNumberError = if (isValidPhoneNumber(it)) null else formatErrorDesc
                },
                label = { Text(stringResource(id = R.string.phone)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneNumberError != null,
                singleLine = true,
                supportingText = {
                    phoneNumberError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            )
        }
        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                modifier = Modifier.padding(top = 24.dp, end = 16.dp),
                imageVector = Icons.Outlined.Mail,
                contentDescription = stringResource(id = R.string.email)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {
                    onEmailChanged(it)
                    emailError = if (isValidEmail(it)) null else formatErrorDesc
                },
                label = { Text(stringResource(id = R.string.email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null,
                singleLine = true,
                supportingText = {
                    emailError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            )
        }

        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp, end = 16.dp),
                imageVector = Icons.Outlined.Cake,
                contentDescription = stringResource(id = R.string.birthday)
            )
            DatePicker(
                onDateSelected = {
                    date -> birthDayError = null
                    onBirthDayChanged(date) },
                date = birthDay,
                birthDayError = birthDayError
            )
        }

        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                modifier = Modifier.padding(top = 24.dp, end = 16.dp),
                imageVector = Icons.Outlined.AttachMoney,
                contentDescription = stringResource(id = R.string.salary_expectations),
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = salary?.toString() ?: "",
                onValueChange = { onSalaryChanged(it.toDouble()) },
                label = { Text(stringResource(id = R.string.salary_expectations)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                isError = false,
                singleLine = true
            )
        }
        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                modifier = Modifier.padding(top = 24.dp, end = 16.dp),
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(id = R.string.notes),
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = note ?: "",
                onValueChange = { onNoteChanged(it) },
                label = { Text(stringResource(id = R.string.notes)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                isError = false,
                singleLine = false
            )
        }
        Spacer(modifier = Modifier.size(80.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(onDateSelected: (Long) -> Unit, date: Long?, birthDayError: String?) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val isDateFilled = date != null
    val selectedDate = datePickerState.selectedDateMillis ?: date
    var selectedDateStr =
        stringResource(id = R.string.date_display_pattern)
    if (isDateFilled) {
        selectedDateStr = SimpleDateFormat(stringResource(id = R.string.birthday_pattern), Locale.getDefault())
            .format(Date(date))
    }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        if (selectedDate !== null) {
            onDateSelected(selectedDate)
        }
        showDatePicker = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        Row {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.select_a_date),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.enter_a_date),
                style = MaterialTheme.typography.headlineLarge
            )
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Outlined.Today,
                contentDescription = stringResource(id = R.string.birthday)
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            color = MaterialTheme.colorScheme.outline
        )
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isDateFilled) { showDatePicker = !showDatePicker },
                value = selectedDateStr,
                label = { Text(stringResource(id = R.string.date)) },
                placeholder = { Text(stringResource(id = R.string.birthday_pattern)) },
                enabled = false,
                onValueChange = { },
                isError = birthDayError != null,
                supportingText = {
                    birthDayError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
        }

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false }
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}
