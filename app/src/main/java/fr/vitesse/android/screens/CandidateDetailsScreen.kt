package fr.vitesse.android.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import fr.vitesse.android.R
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.module.CandidateActionComposerModule
import fr.vitesse.android.service.CurrencyConversionService
import fr.vitesse.android.viewmodel.CandidateDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateDetailsScreen(
    modifier: Modifier = Modifier,
    sendToComposer: CandidateActionComposerModule,
    backStackEntry: NavBackStackEntry,
    onBackClick: () -> Unit,
) {
    val candidateDetailsViewModel: CandidateDetailsViewModel =
        koinViewModel(viewModelStoreOwner = backStackEntry)
    val collectedCandidate by candidateDetailsViewModel.candidate.collectAsStateWithLifecycle()
    val candidate = collectedCandidate

    if (candidate == null) {
        return
    }

    @Composable
    fun CandidateDetails(
        modifier: Modifier = Modifier,
        candidate: Candidate
    )
    {
        val scrollState = rememberScrollState()
        val currencyConversionService = CurrencyConversionService()
        val eurSalary = candidate.salary.toString() + " €"

        val defaultPoundSalary = (candidate.salary * 0.86).toString() + " £"
        var poundSalary by remember { mutableStateOf(defaultPoundSalary) }

        LaunchedEffect(candidate.salary) {
            poundSalary = String.format(
                "%.2f £", currencyConversionService.convertEuroToPounds(candidate.salary)
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.aspectRatio(3f / 2f)) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.default_avatar),
                    contentDescription = stringResource(id = R.string.default_avatar)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CandidateAction(
                    drawableRes = R.drawable.call_24px,
                    text = stringResource(id = R.string.call),
                    onClick = {
                        sendToComposer.call(candidate.phoneNumber)
                    }
                )
                CandidateAction(
                    drawableRes = R.drawable.chat_24px,
                    text = stringResource(id = R.string.sms),
                    onClick = {
                        sendToComposer.sendSms(candidate.phoneNumber)
                    }
                )
                CandidateAction(
                    drawableRes = R.drawable.mail_24px,
                    text = stringResource(id = R.string.email),
                    onClick = {
                        sendToComposer.sendEmail(candidate.email)
                    }
                )
            }
            Row {
                CandidateCard(
                    stringResource(id = R.string.about),
                    candidate.formatDateWithAge(),
                    stringResource(id = R.string.birthday)
                )
            }
            Row {
                CandidateCard(
                    stringResource(id = R.string.salary_expectations),
                    eurSalary,
                    stringResource(id = R.string.average_amount).lowercase() + " " + poundSalary
                )
            }
            Row {
                CandidateCard(
                    stringResource(id = R.string.notes),
                    candidate.note,
                    subtitle2 = null
                )
            }
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(text = stringResource(id = R.string.deletion))
            },
            text = {
                Text(text = stringResource(id = R.string.are_you_sure_you_want_to_delete_candidate) + '.')
            },
            confirmButton = {
                TextButton(onClick = {
                    candidateDetailsViewModel.deleteCandidate()
                    showDeleteDialog = false
                    onBackClick()
                }) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Text(candidate.fullName)
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
                },
                actions = {
                    IconButton(onClick = {
                        candidateDetailsViewModel.toggleCandidateFavorite(candidate.id)
                    }) {
                        CandidateFavoriteIcon(candidate.isFavorite)
                    }
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(id = R.string.edit)
                        )
                    }
                    IconButton(onClick = {
                        showDeleteDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { contentPadding ->
        CandidateDetails(
            modifier = modifier.padding(contentPadding),
            candidate = candidate,
        )
    }
}

@Composable
private fun CandidateFavoriteIcon(
    isFavorite: Boolean,
) {
    if (isFavorite) {
        return Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = stringResource(id = R.string.favorite)
        )
    }

    return Icon(
        painter = painterResource(R.drawable.outlined_star_24),
        contentDescription = stringResource(id = R.string.favorite)
    )
}

@Composable
private fun CandidateAction(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.onSurface, shape = CircleShape)
                .padding(12.dp)
                .width(32.dp)
                .height(32.dp),
            tint = MaterialTheme.colorScheme.onSurface,
            painter = painterResource(drawableRes),
            contentDescription = text
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun CandidateCard (
    title: String,
    subtitle1: String,
    subtitle2: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
            .background(color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle1,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (subtitle2.isNullOrBlank().not()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle2,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
