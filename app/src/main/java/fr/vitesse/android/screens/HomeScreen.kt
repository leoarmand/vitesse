package fr.vitesse.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    candidates: List<Candidate> = emptyList<Candidate>(),
    onCandidateClick: (Candidate) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(stringResource(R.string.app_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.app_name))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.app_name))
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )
                }
            )
        }
    ) { contentPadding ->
        CandidateList(
            modifier = modifier.padding(contentPadding),
            candidates = candidates,
            onCandidateClick = onCandidateClick
        )
    }
}



@Composable
private fun CandidateList(
    modifier: Modifier = Modifier,
    candidates: List<Candidate>,
    onCandidateClick: (Candidate) -> Unit,
) {
    LazyColumn(modifier) {
        items(candidates) { candidate ->
            HomeCell(
                candidate = candidate,
                onCandidateClick = onCandidateClick
            )
        }
    }
}

@Composable
private fun HomeCell(
    candidate: Candidate,
    onCandidateClick: (Candidate) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable {
                onCandidateClick(candidate)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .width(54.dp)
                .height(54.dp),
            painter = painterResource(R.drawable.default_avatar),
            contentDescription = stringResource(id = R.string.app_name)
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(
                text = candidate.getFullName(),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = candidate.note,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
