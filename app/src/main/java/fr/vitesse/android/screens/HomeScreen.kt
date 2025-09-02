package fr.vitesse.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.R
import fr.vitesse.android.components.AvatarComponent
import fr.vitesse.android.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCandidateClick: (Int) -> Unit = {},
    onAddCandidateClick: () -> Unit = {}
) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val candidates by homeViewModel.candidates.collectAsStateWithLifecycle(emptyList())
    var isLoading by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    fun computeFilteredCandidates(
        candidates: List<Candidate>,
        selectedTabIndex: Int,
        searchQuery: String
    ): List<Candidate> {
        isLoading = true
        val fCandidates = candidates.filter {
            val matchesSearch = it.fullName.contains(searchQuery, ignoreCase = true)
            val matchesTab = when (selectedTabIndex) {
                0 -> true
                1 -> it.isFavorite
                else -> true
            }
            matchesSearch && matchesTab
        }
        isLoading = false
        return fCandidates
    }

    val filteredCandidates by remember(candidates, selectedTabIndex, searchQuery) {
        mutableStateOf(
            computeFilteredCandidates(
                candidates,
                selectedTabIndex,
                searchQuery
            )
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        SearchCandidateBar { searchedQuery ->
                            searchQuery = searchedQuery
                        }
                    }
                )

                CandidateTabs(selectedTabIndex, onTabClick = {index -> selectedTabIndex = index})
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddCandidateClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add)
                )
            }
        }
    ) { contentPadding ->
        CandidateList(
            modifier = modifier.padding(contentPadding),
            isLoading,
            candidates = filteredCandidates,
            onCandidateClick = onCandidateClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CandidateTabs(
    selectedTabIndex: Int,
    onTabClick: (index: Int) -> Unit
    ) {
    val tabs = listOf(stringResource(R.string.all), stringResource(R.string.favorites))

    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabClick(index) },
                text = { Text(title) }
            )
        }
    }
}

@Composable
private fun SearchCandidateBar(
    onValueChange: (searchedQuery: String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    Box {
        TextField(
            value = searchQuery,
            onValueChange = { searchedQuery ->
                searchQuery = searchedQuery
                onValueChange(searchedQuery)
            },
            placeholder = { Text(
                text = stringResource(R.string.search_a_candidate),
                color = MaterialTheme.colorScheme.onSurface
            ) },
            modifier = Modifier
                .clip(RoundedCornerShape(36.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(0.96f)
                .align(Alignment.Center),
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
            )
        )
    }
}

@Composable
private fun CandidateList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    candidates: List<Candidate>,
    onCandidateClick: (Int) -> Unit,
) {
    when {
        isLoading -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        candidates.isEmpty() -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_candidate),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        else -> {
            LazyColumn(modifier) {
                items(candidates) { candidate ->
                    HomeCell(
                        candidate = candidate,
                        onCandidateClick = onCandidateClick
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeCell(
    candidate: Candidate,
    onCandidateClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCandidateClick(candidate.id)
            }
            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarComponent(isSmallVersion = true, avatarUri = candidate.avatarPath?.toUri())
        Column(
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(
                text = candidate.fullName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = candidate.note ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
