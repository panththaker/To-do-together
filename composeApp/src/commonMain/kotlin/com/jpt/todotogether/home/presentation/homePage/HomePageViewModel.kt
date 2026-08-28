package com.jpt.todotogether.home.presentation.homePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpt.todotogether.core.domain.model.Todo
import com.jpt.todotogether.core.domain.repository.AuthRepository
import com.jpt.todotogether.core.domain.repository.SettingsRepository
import com.jpt.todotogether.core.domain.repository.TodoRepository
import com.jpt.todotogether.logger.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// view model for the page, should contain all logic for handling events and updating state,
// and should be the only place that updates the state.
class HomePageViewModel(
    private val settingsRepository: SettingsRepository,
    private val todoRepository: TodoRepository,
    private val authRepository: AuthRepository,
): ViewModel() {
    // _state is the mutable version of our state and is only accessible in this file
    private val _state = MutableStateFlow(HomePageState())

    // the state we actually pass around must be immutable.
    val state = _state.asStateFlow()

    // on initalization, we want to load any data we need for the page,
    // in this case we want to load the settings from the repository to get the initial counter value.
    // (note, `init` is called directly after the constructor)
    init {
        load()
        loadTodos()
        loadSession()
    }

    // onAction is the only handler passed from the viewModel to the composable,
    // and is used to handle all events from the composable.
    // This way we can keep all logic in the viewModel and the composable focused on just ui.
    fun onAction(action: HomePageAction) {

        AppLogger.debug(
            tag = "HomePageViewModel",
            message = "OnAction: $action"
        )

        // exhaustive when statement to handle all possible actions, and update the state accordingly.
        when (action) {
            is HomePageAction.OnIncrementCounter -> {
                val newCount = state.value.count + action.amount
                _state.update { it.copy(count = newCount) }

                updateSettingsCounter(newCount)
            }
            HomePageAction.OnResetCounter -> {
                _state.update { it.copy(count = 0) }

                updateSettingsCounter(0L)
            }
            is HomePageAction.OnNewTodoTitleChanged -> {
                _state.update { it.copy(newTodoTitle = action.title) }
            }
            HomePageAction.OnAddTodo -> addTodo()
            is HomePageAction.OnToggleTodo -> toggleTodo(action.todo)
            is HomePageAction.OnDeleteTodo -> deleteTodo(action.id)
            is HomePageAction.OnGoogleSignInSucceeded -> completeGoogleSignIn(action.idToken)
            is HomePageAction.OnGoogleSignInFailed -> _state.update { it.copy(isSigningIn = false, authError = action.message) }
            HomePageAction.OnSignOutClicked -> signOut()
        }
    }

    // called on every app start (see init). authRepository.getSession() silently
    // refreshes an expired access token using the stored refresh token, so a
    // previously signed-in user is reauthenticated here without any user action.
    private fun loadSession() = viewModelScope.launch {
        _state.update { it.copy(isSessionLoading = true) }
        runCatching { authRepository.getSession() }
            .onSuccess { session ->
                _state.update { it.copy(authUser = session?.user, authSession = session, isSessionLoading = false) }
            }
            .onFailure {
                AppLogger.error(tag = "HomePageViewModel", message = "Error loading auth session", throwable = it)
                _state.update { it.copy(isSessionLoading = false) }
            }
    }

    private fun completeGoogleSignIn(idToken: String) = viewModelScope.launch {
        _state.update { it.copy(isSigningIn = true, authError = null) }
        runCatching { authRepository.signInWithGoogleIdToken(idToken) }
            .onSuccess { session ->
                _state.update { current -> current.copy(isSigningIn = false, authUser = session.user, authSession = session) }
            }
            .onFailure { throwable ->
                AppLogger.error(tag = "HomePageViewModel", message = "Error completing Google sign-in", throwable = throwable)
                _state.update { current -> current.copy(isSigningIn = false, authError = throwable.message ?: "Sign-in failed") }
            }
    }

    private fun signOut() = viewModelScope.launch {
        runCatching { authRepository.signOut() }
            .onSuccess { _state.update { it.copy(authUser = null, authSession = null) } }
            .onFailure {
                AppLogger.error(tag = "HomePageViewModel", message = "Error signing out", throwable = it)
            }
    }

    // helpers
    fun updateSettingsCounter(counterValue: Long) {
        viewModelScope.launch {
            runCatching {
                settingsRepository.updateSettings(
                    settings = settingsRepository.getSettings().copy(counter = counterValue)
                )
            }.onFailure {
                AppLogger.error(
                    tag = "HomePageViewModel",
                    message = "Error updating settings",
                    throwable = it,
                )
            }
        }
    }

    // simple test UI for the Todo API - loads the list, and always reloads it
    // from the server after a mutation rather than patching state locally.
    private fun loadTodos() = viewModelScope.launch {
        runCatching { todoRepository.getTodos() }
            .onSuccess { todos -> _state.update { it.copy(todos = todos) } }
            .onFailure {
                AppLogger.error(tag = "HomePageViewModel", message = "Error loading todos", throwable = it)
            }
    }

    private fun addTodo() {
        val title = state.value.newTodoTitle.trim()
        if (title.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(newTodoTitle = "") }
            runCatching { todoRepository.createTodo(title) }
                .onSuccess { loadTodos() }
                .onFailure {
                    AppLogger.error(tag = "HomePageViewModel", message = "Error creating todo", throwable = it)
                }
        }
    }

    private fun toggleTodo(todo: Todo) = viewModelScope.launch {
        runCatching { todoRepository.updateTodo(todo.copy(completed = !todo.completed)) }
            .onSuccess { loadTodos() }
            .onFailure {
                AppLogger.error(tag = "HomePageViewModel", message = "Error updating todo", throwable = it)
            }
    }

    private fun deleteTodo(id: Int) = viewModelScope.launch {
        runCatching { todoRepository.deleteTodo(id) }
            .onSuccess { loadTodos() }
            .onFailure {
                AppLogger.error(tag = "HomePageViewModel", message = "Error deleting todo", throwable = it)
            }
    }

    // the load function is used to asynchronously load from repositories (ie SQLDelight)
    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        runCatching {
            settingsRepository.getSettings()
        }.onSuccess { settings ->

            _state.update {
                it.copy(
                    count = settings.counter,
                    isLoading = false,
                )
            }
        }.onFailure {
            AppLogger.error(
                tag = "HomePageViewModel",
                message = "Error loading settings",
                throwable = it,
            )
        }
    }
}