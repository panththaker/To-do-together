@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.jpt.todotogether.home.presentation.homePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpt.todotogether.core.domain.model.Todo
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
            is HomePageAction.OnNewTodoDueDateChanged -> {
                _state.update { it.copy(newTodoDueDate = action.dueDate) }
            }
            is HomePageAction.OnNewTodoLabelChanged -> {
                _state.update { it.copy(newTodoLabel = action.label) }
            }
            HomePageAction.OnAddTodo -> addTodo()
            is HomePageAction.OnToggleTodo -> toggleTodo(action.todo)
            is HomePageAction.OnDeleteTodo -> deleteTodo(action.id)
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

        val dueDate = state.value.newTodoDueDate
        val label = state.value.newTodoLabel.trim().ifBlank { null }

        viewModelScope.launch {
            _state.update { it.copy(newTodoTitle = "", newTodoDueDate = null, newTodoLabel = "") }
            runCatching { todoRepository.createTodo(title, dueDate, label) }
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