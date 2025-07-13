package fr.vitesse.android.screens

import android.R.style.Theme
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.vitesse.android.R
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.viewmodel.CandidateDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateDetailsScreen(
    modifier: Modifier = Modifier,
    candidateDetailsViewModel: CandidateDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    val _candidate by candidateDetailsViewModel.candidate.collectAsStateWithLifecycle()
    val candidate = _candidate

    if (candidate == null) {
        return
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
                        //TODO
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = stringResource(id = R.string.favorite)
                        )
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(id = R.string.edit)
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
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
        candidate.let {
            CandidateDetails(
                modifier = modifier.padding(contentPadding),
                candidate = it,
            )
        }
    }
}

fun deleteCandidate(
    candidate: Candidate?
)
{
    ///TODO
}

@Composable
private fun CandidateDetails(
    modifier: Modifier = Modifier,
    candidate: Candidate
)
{
    val scrollState = rememberScrollState()

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
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CandidateAction(
                drawableRes = R.drawable.call_24px,
                text = stringResource(id = R.string.call)
            )
            CandidateAction(
                drawableRes = R.drawable.chat_24px,
                text = stringResource(id = R.string.sms)
            )
            CandidateAction(
                drawableRes = R.drawable.mail_24px,
                text = stringResource(id = R.string.email)
            )
        }
    }
}

@Composable
private fun CandidateAction(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    text: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .width(54.dp)
                .height(54.dp),
            tint = MaterialTheme.colorScheme.onSurface,
            painter = painterResource(drawableRes),
            contentDescription = text
        )
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
