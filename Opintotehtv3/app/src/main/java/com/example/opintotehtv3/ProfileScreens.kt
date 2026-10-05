package com.example.opintotehtv3

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.opintotehtv3.ui.theme.Opintotehtävä3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileListScreen(
    profiles: List<Profile>,
    onProfileClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Save UI choices across rotation and while this destination is on the back stack.
    var searchText by rememberSaveable { mutableStateOf("") }
    var availableOnly by rememberSaveable { mutableStateOf(false) }
    val visibleProfiles = filterProfiles(profiles, searchText, availableOnly)
    val focusManager = LocalFocusManager.current
    val filterLabel = stringResource(R.string.available_only)

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.profiles_title)) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text(stringResource(R.string.search_label)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.clear_search)
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("profile_search"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(filterLabel, modifier = Modifier.weight(1f))
                Switch(
                    checked = availableOnly,
                    onCheckedChange = { availableOnly = it },
                    modifier = Modifier
                        .testTag("available_filter")
                        .semantics { contentDescription = filterLabel }
                )
            }
            Text(
                text = stringResource(R.string.profiles_count, visibleProfiles.size, profiles.size),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyColumn(
                modifier = Modifier.weight(1f).testTag("profile_list"),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (visibleProfiles.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.no_matching_profiles),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                items(visibleProfiles, key = { it.id }) { profile ->
                    ProfileCard(
                        profile = profile,
                        onClick = {
                            focusManager.clearFocus()
                            onProfileClick(profile.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    profile: Profile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // The card reports a click; ProfileApp decides how to navigate.
    ElevatedCard(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(profile.imageRes),
            contentDescription = null, // Shared decorative illustration, not a person's photo.
            modifier = Modifier.fillMaxWidth().height(140.dp).padding(8.dp),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(profile.name, style = MaterialTheme.typography.titleLarge)
            Text(profile.role, style = MaterialTheme.typography.bodyMedium)
            Text(
                profile.interests,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AvailabilityStatus(isAvailable = profile.isAvailableForProjects)
        }
    }
}

@Composable
fun ProfileDetailScreen(
    profile: Profile,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { ProfileDetailTopBar(title = profile.name, onBackClick = onBackClick) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(profile.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )
            Text(
                profile.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(profile.role, style = MaterialTheme.typography.titleMedium)
            AvailabilityStatus(isAvailable = profile.isAvailableForProjects)
            Text(
                stringResource(profile.descriptionRes),
                style = MaterialTheme.typography.bodyLarge
            )
            HorizontalDivider()
            InfoRow(label = stringResource(R.string.school_variable), value = profile.school)
            InfoRow(label = stringResource(R.string.major_variable), value = profile.major)
            InfoRow(label = stringResource(R.string.intrests_variable), value = profile.interests)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(value, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun AvailabilityStatus(isAvailable: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isAvailable) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (isAvailable) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isAvailable) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
            }
            Text(
                stringResource(
                    if (isAvailable) R.string.available_for_projects else R.string.unavailable_for_projects
                ),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileDetailTopBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
        }
    )
}

@Composable
fun ProfileNotFoundScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ProfileDetailTopBar(
                title = stringResource(R.string.profile),
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Text(
            stringResource(R.string.profile_not_found),
            modifier = Modifier.padding(innerPadding).padding(24.dp)
        )
    }
}

@Preview(name = "Profile list", showBackground = true, showSystemUi = true)
@Composable
private fun ProfileListPreview() {
    Opintotehtävä3Theme { ProfileListScreen(profiles = sampleProfiles, onProfileClick = {}) }
}

@Preview(name = "Profile detail in Finnish", locale = "fi", showBackground = true, showSystemUi = true)
@Composable
private fun ProfileDetailPreview() {
    Opintotehtävä3Theme { ProfileDetailScreen(profile = sampleProfiles.first(), onBackClick = {}) }
}
