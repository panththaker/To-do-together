package com.jpt.todotogether.home.presentation.homePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jpt.todotogether.core.theming.Theme
import com.jpt.todotogether.home.presentation.components.Counter
import com.mmk.kmpauth.google.rememberGoogleSignInState
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// public page wrapper that is used for routing
// is separated like this to avoid passing view model for previews and to keep the screen composable focused on ui
@Composable
fun HomePageScreenRoot(
    viewModel: HomePageViewModel = koinViewModel(),
    navigateToSettingsPage: () -> Unit = { },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomePageScreen(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
        },
        navigateToSettingsPage = navigateToSettingsPage,
    )
}

// Preview for the screen, there can be multiple of these, and we can pass in arbitrary data.
// Helpful to 'test' ui independently.
@Composable
@Preview
private fun HomePageScreenPreview() {
    HomePageScreen(
        state = HomePageState(
            isLoading = false,
        ),
        onAction = {},
        navigateToSettingsPage = {},
    )
}

// Main page composable, should be focused on just ui. All state is passed in, and all events are passed out.
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNUSED_PARAMETER")
@Composable
private fun HomePageScreen(
    state: HomePageState,
    onAction: (HomePageAction) -> Unit,
    navigateToSettingsPage: () -> Unit,
    ) {
    // no explicit sheet state; we use simple boolean flags and rely on ModalBottomSheet's onDismissRequest

    var showAddBottomSheet by remember { mutableStateOf(false) }
    var showAddEnvelopeMenu by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }

    val googleSignIn = rememberGoogleSignInState(
        // library default (true) silently skips the chooser and can return a
        // stale cached credential instead of a fresh token - wrong for an
        // explicit sign-in tap. Silent reauth on app start already happens
        // separately via our own refresh token (HomePageViewModel.loadSession).
        isAutoSelectEnabled = false,
        onResult = { result ->
        result
            .onSuccess { user -> onAction(HomePageAction.OnGoogleSignInSucceeded(user.idToken)) }
            .onFailure { error -> onAction(HomePageAction.OnGoogleSignInFailed(error.message ?: "Sign-in failed")) }
    })

    Theme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text("Todo Together")
                    },
                    actions = {
                        IconButton(onClick = {
                            showMoreMenu = true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.MoreHoriz,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = "More Options",
                            )
                        }

                        // Dropdown menu anchored to the More icon. Uses local state `showMoreMenu`.
                        DropdownMenu(
                            expanded = showMoreMenu,
                            onDismissRequest = { showMoreMenu = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    // TODO
                                    showMoreMenu = false
                                }
                            )

                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .shadow(5.dp)
                        .zIndex(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(onClick = { /* TODO */ }) {
                            Text("Page 100")
                        }
                        Button(onClick = { /* TODO */ }) {
                            Text("Page 2")
                        }
                        Button(onClick = { /* TODO */ }) {
                            Text("Page 3")
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column (
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (state.authUser != null) {
                        Text(
                            text = "Signed in as ${state.authUser.name}",
                            modifier = Modifier.weight(1f),
                        )
                        Button(onClick = { onAction(HomePageAction.OnSignOutClicked) }) {
                            Text("Sign out")
                        }
                    } else if (state.isSigningIn || googleSignIn.isInProgress) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        GoogleSignInButton(onClick = { googleSignIn.launch() })
                    }
                }

                if (state.authError != null) {
                    Text(
                        text = state.authError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 4.dp),
                    )
                }

                AuthDebugPanel(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 8.dp),
                )

                Counter(
                    value = state.count,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(0.9f),
                    onPlusClick = { onAction(HomePageAction.OnIncrementCounter(1)) },
                    onMinusClick = { onAction(HomePageAction.OnIncrementCounter(-1)) },
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = state.newTodoTitle,
                        onValueChange = { onAction(HomePageAction.OnNewTodoTitleChanged(it)) },
                        label = { Text("New todo") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onAction(HomePageAction.OnAddTodo) }) {
                        Text("Add")
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(1f),
                ) {
                    items(state.todos, key = { it.id }) { todo ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = todo.completed,
                                onCheckedChange = { onAction(HomePageAction.OnToggleTodo(todo)) },
                            )
                            Text(
                                text = todo.title,
                                modifier = Modifier.weight(1f),
                            )
                            IconButton(onClick = { onAction(HomePageAction.OnDeleteTodo(todo.id)) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete todo")
                            }
                        }
                    }
                }
            }

         }
     }
 }

// Debug-only panel showing the raw auth state (session presence, tokens,
// expiry). Tokens are truncated so they aren't fully readable on screen/in
// screenshots, but enough of each is shown to spot which token you're looking at.
@OptIn(ExperimentalTime::class)
@Composable
private fun AuthDebugPanel(
    state: HomePageState,
    onAction: (HomePageAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val session = state.authSession

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "Auth debug", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))

            DebugLine("session loading", state.isSessionLoading.toString())
            DebugLine("signing in", state.isSigningIn.toString())
            DebugLine("signed in", (session != null).toString())

            if (session != null) {
                DebugLine("user id", session.user.id.toString())
                DebugLine("email", session.user.email)
                DebugLine("access token", session.accessToken.debugTruncate())
                DebugLine("refresh token", session.refreshToken.debugTruncate())

                val expiresAt = Instant.fromEpochMilliseconds(session.expiresAtEpochMillis)
                val isExpired = Clock.System.now() >= expiresAt
                DebugLine("expires at", expiresAt.toString())
                DebugLine("expired", isExpired.toString())
            }

            if (state.authError != null) {
                DebugLine("last error", state.authError)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Always available regardless of authUser, unlike the normal sign-out
            // button - this is the only way to clear Credential Manager's
            // remembered/cached Google credential when the backend exchange has
            // never succeeded (so the app never reaches a "signed in" state that
            // would otherwise expose sign-out).
            OutlinedButton(onClick = { onAction(HomePageAction.OnSignOutClicked) }) {
                Text("Force clear cached credential")
            }
        }
    }
}

@Composable
private fun DebugLine(label: String, value: String) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodySmall,
        fontFamily = FontFamily.Monospace,
    )
}

private fun String.debugTruncate(): String =
    if (length <= 16) this else "${take(8)}…${takeLast(6)}"
