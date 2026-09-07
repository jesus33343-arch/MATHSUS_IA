package com.example.mathsus_ia.data

import dev.insforge.database.database
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CalculationParams(
    val a: Double? = null,
    val b: Double? = null,
    val x0: Double? = null,
    val x1: Double? = null,
    val tolerance: Double? = null,
    @SerialName("max_iterations") val maxIterations: Int? = null
)

@Serializable
data class CalculationEventSubmission(
    val id: String = UUID.randomUUID().toString(),
    @SerialName("device_id") val deviceId: String,
    val method: String,
    @SerialName("function_expr") val functionExpr: String,
    val params: CalculationParams,
    @SerialName("root_value") val rootValue: Double? = null,
    val iterations: Int? = null,
    val converged: Boolean = true,
    @SerialName("exercise_label") val exerciseLabel: String? = null,
    @SerialName("locale_country") val localeCountry: String,
    val timezone: String,
    @SerialName("app_version") val appVersion: String? = null
)

@Serializable
data class CalculationSatisfactionSubmission(
    @SerialName("calculation_event_id") val calculationEventId: String,
    @SerialName("device_id") val deviceId: String,
    val satisfied: Boolean
)

@Serializable
data class ExerciseCommentSubmission(
    @SerialName("calculation_event_id") val calculationEventId: String?,
    @SerialName("device_id") val deviceId: String,
    val comment: String
)

@Serializable
data class AiQuerySubmission(
    val id: String = UUID.randomUUID().toString(),
    @SerialName("device_id") val deviceId: String,
    @SerialName("model_name") val modelName: String,
    val prompt: String,
    val response: String? = null,
    @SerialName("method_context") val methodContext: String? = null,
    @SerialName("locale_country") val localeCountry: String,
    val timezone: String,
    @SerialName("app_version") val appVersion: String? = null
)

@Serializable
data class AiResponseSatisfactionSubmission(
    @SerialName("ai_query_id") val aiQueryId: String,
    @SerialName("device_id") val deviceId: String,
    val satisfied: Boolean
)

@Serializable
data class AiQueryRow(
    val id: String,
    @SerialName("device_id") val deviceId: String,
    @SerialName("model_name") val modelName: String? = null,
    val prompt: String,
    val response: String? = null,
    @SerialName("method_context") val methodContext: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

/**
 * Logs a calculation and returns the id to use for a later satisfaction rating
 * or exercise comment (no need to re-send the function/inputs/result again).
 */
suspend fun logCalculationEvent(submission: CalculationEventSubmission): String {
    InsforgeClient.client.database
        .from("calculation_events")
        .insertTyped(listOf(submission))
        .execute<CalculationEventSubmission>()
    return submission.id
}

suspend fun submitCalculationSatisfaction(submission: CalculationSatisfactionSubmission) {
    InsforgeClient.client.database
        .from("calculation_satisfaction")
        .insertTyped(listOf(submission))
        .execute<CalculationSatisfactionSubmission>()
}

suspend fun submitExerciseComment(submission: ExerciseCommentSubmission) {
    InsforgeClient.client.database
        .from("exercise_comments")
        .insertTyped(listOf(submission))
        .execute<ExerciseCommentSubmission>()
}

/**
 * Logs an AI chat turn and returns its id, so a thumbs up/down can reference
 * this exact query without re-sending the prompt/response.
 */
suspend fun logAiQuery(submission: AiQuerySubmission): String {
    InsforgeClient.client.database
        .from("ai_queries")
        .insertTyped(listOf(submission))
        .execute<AiQuerySubmission>()
    return submission.id
}

suspend fun submitAiResponseSatisfaction(submission: AiResponseSatisfactionSubmission) {
    InsforgeClient.client.database
        .from("ai_response_satisfaction")
        .insertTyped(listOf(submission))
        .execute<AiResponseSatisfactionSubmission>()
}

/**
 * Reloads a device's own past AI chat turns so the conversation survives
 * leaving/reentering the screen. Goes through the get_conversation_history
 * RPC (SECURITY DEFINER, filtered by device_id) instead of a direct SELECT,
 * since ai_queries has no broad read policy.
 */
suspend fun fetchConversationHistory(deviceId: String): List<AiQueryRow> {
    return InsforgeClient.client.database.rpc(
        "get_conversation_history",
        mapOf("p_device_id" to deviceId)
    )
}
