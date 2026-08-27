package com.jpt.todotogether.home.presentation.homePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jpt.todotogether.core.theming.Theme
import com.jpt.todotogether.home.presentation.components.Counter
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

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
