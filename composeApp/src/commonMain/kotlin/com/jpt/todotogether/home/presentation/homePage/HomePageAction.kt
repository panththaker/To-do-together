package com.jpt.todotogether.home.presentation.homePage

import com.jpt.todotogether.core.domain.model.Todo
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// sealed interface HomePageAction is a sealed interface that defines the possible
// actions that can be taken on the HomePage.
//
// This is used to handle user interactions and update the state accordingly.
// By using a sealed interface, we can ensure that all possible actions are handled.
@OptIn(ExperimentalTime::class)
sealed interface HomePageAction {
    data class OnIncrementCounter(val amount: Int) : HomePageAction
    object OnResetCounter : HomePageAction

    data class OnNewTodoTitleChanged(val title: String) : HomePageAction
    data class OnNewTodoDueDateChanged(val dueDate: Instant?) : HomePageAction
    data class OnNewTodoLabelChanged(val label: String) : HomePageAction
    object OnAddTodo : HomePageAction
    data class OnToggleTodo(val todo: Todo) : HomePageAction
    data class OnDeleteTodo(val id: Int) : HomePageAction

    // idToken/message come from KMPAuth's rememberGoogleSignInState result,
    // handled in the composable since it's a Compose-only API.
    data class OnGoogleSignInSucceeded(val idToken: String) : HomePageAction
    data class OnGoogleSignInFailed(val message: String) : HomePageAction
    object OnSignOutClicked : HomePageAction
}