package com.newritage.app.network

import com.newritage.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Groq(llama-3.1-8b-instant) Chat Completions API 호출만 담당하는 클라이언트.
 * 프롬프트 구성이나 DB 접근은 하지 않으며, 실패 시 예외를 던져 호출자([com.newritage.app.data.GroqRepository])가
 * 로컬 로직으로 폴백할 수 있게 한다.
 */
object GroqApi {

    private const val MODEL = "llama-3.1-8b-instant"
    private const val ENDPOINT = "https://api.groq.com/openai/v1/chat/completions"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    /** [systemPrompt]/[userPrompt]로 채팅 완료 요청을 보내고 첫 응답 메시지의 텍스트를 반환한다. */
    suspend fun chatCompletion(systemPrompt: String, userPrompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GROQ_API_KEY
        if (apiKey.isBlank()) {
            throw IllegalStateException("GROQ_API_KEY가 비어 있습니다. local.properties에 키를 설정해주세요.")
        }

        val requestBody = JSONObject().apply {
            put("model", MODEL)
            put("temperature", 0.5)
            put("max_tokens", 512)
            put("messages", JSONArray().apply {
                put(JSONObject().apply { put("role", "system"); put("content", systemPrompt) })
                put(JSONObject().apply { put("role", "user"); put("content", userPrompt) })
            })
        }.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(ENDPOINT)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            val bodyStr = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw IOException("Groq API 호출 실패 (${response.code}): $bodyStr")
            }
            JSONObject(bodyStr)
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim()
        }
    }
}
