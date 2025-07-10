package fr.vitesse.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.vitesse.android.R
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.viewmodel.CandidateDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateDetailsScreen(
    modifier: Modifier = Modifier,
    candidateDetailsViewModel: CandidateDetailsViewModel,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    val candidateState by candidateDetailsViewModel.candidate.collectAsState()
    val candidate = candidateState!!

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
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
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { contentPadding ->
        candidate?.let {
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
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart),
                text = candidate.fullName,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
