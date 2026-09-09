package com.example.mathsus_ia.data

import dev.insforge.createInsforgeClient
import dev.insforge.database.Database
import dev.insforge.database.database
import dev.insforge.functions.Functions
import dev.insforge.functions.functions
import io.github.jesusgurrute.mathsus_ia.BuildConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object InsforgeClient {
    val client = createInsforgeClient(
        baseURL = BuildConfig.insforgeBaseUrl,
        anonKey = BuildConfig.insforgeAnonKey
    ) {
        install(Database)
        install(Functions)
    }
}

@Serializable
data class AiChatTurn(val role: String, val content: String)

@Serializable
data class AiChatRequest(val prompt: String, val history: List<AiChatTurn>)

@Serializable
data class AiChatResponse(val text: String? = null, val model: String? = null, val error: String? = null)

suspend fun askMathsusAi(prompt: String, history: List<AiChatTurn> = emptyList()): AiChatResponse {
    return InsforgeClient.client.functions.invoke<AiChatResponse>(
        slug = "mathsus-ai-chat",
        body = AiChatRequest(prompt, history)
    )
}

@Serializable
data class FeedbackSubmission(
    val rating: Int? = null,
    val category: String,
    val message: String,
    @SerialName("contact_email") val contactEmail: String? = null,
    @SerialName("app_version") val appVersion: String? = null
)

@Serializable
data class AiResponseReportSubmission(
    val prompt: String,
    val response: String,
    val reason: String? = null,
    @SerialName("app_version") val appVersion: String? = null
)

suspend fun submitFeedback(feedback: FeedbackSubmission) {
    InsforgeClient.client.database
        .from("user_feedback")
        .insertTyped(listOf(feedback))
        .execute<FeedbackSubmission>()
}

@Serializable
data class IdeaRequestSubmission(
    val audience: String,
    val institution: String? = null,
    @SerialName("contact_email") val contactEmail: String? = null,
    val idea: String,
    @SerialName("app_version") val appVersion: String? = null
)

suspend fun submitIdeaRequest(request: IdeaRequestSubmission) {
    InsforgeClient.client.database
        .from("idea_requests")
        .insertTyped(listOf(request))
        .execute<IdeaRequestSubmission>()
}

suspend fun submitAiResponseReport(report: AiResponseReportSubmission) {
    InsforgeClient.client.database
        .from("ai_response_reports")
        .insertTyped(listOf(report))
        .execute<AiResponseReportSubmission>()
}
