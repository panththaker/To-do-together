package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.core.data.remote.TodoApiConfig
import com.jpt.todotogether.core.domain.model.Todo
import com.jpt.todotogether.core.domain.repository.TodoRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// mirrors the backend's createTodoRequest (api/todos.go) - kept private since
// it's just wire shape, not a domain type.
@OptIn(ExperimentalTime::class)
@Serializable
private data class CreateTodoRequest(
    val title: String,
    val dueDate: Instant? = null,
    val label: String? = null,
)

@OptIn(ExperimentalTime::class)
class KtorTodoRepository(
    private val client: HttpClient,
) : TodoRepository {

    override suspend fun getTodos(): List<Todo> =
        client.get("${TodoApiConfig.baseUrl}/todos").body()

    override suspend fun createTodo(title: String, dueDate: Instant?, label: String?): Todo =
        client.post("${TodoApiConfig.baseUrl}/todos") {
            contentType(ContentType.Application.Json)
            setBody(CreateTodoRequest(title = title, dueDate = dueDate, label = label))
        }.body()

    override suspend fun updateTodo(todo: Todo): Boolean =
        client.put("${TodoApiConfig.baseUrl}/todos/${todo.id}") {
            contentType(ContentType.Application.Json)
            setBody(todo)
        }.status.isSuccess()

    override suspend fun deleteTodo(id: Int): Boolean =
        client.delete("${TodoApiConfig.baseUrl}/todos/$id").status.isSuccess()
}
