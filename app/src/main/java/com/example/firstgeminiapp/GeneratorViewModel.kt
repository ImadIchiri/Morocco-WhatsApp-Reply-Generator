package com.example.firstgeminiapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface GeneratorUiState {
    object Initial : GeneratorUiState
    object Loading : GeneratorUiState
    data class Success(
        val short: String,
        val professional: String,
        val friendly: String
    ) : GeneratorUiState
    data class Error(val errorMessage: String) : GeneratorUiState
}

class GeneratorViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<GeneratorUiState> =
        MutableStateFlow(GeneratorUiState.Initial)
    val uiState: StateFlow<GeneratorUiState> =
        _uiState.asStateFlow()

    private val generativeModel = Firebase.ai.generativeModel(
        modelName = "gemini-2.5-flash",
    )

    fun generateReplies(receivedMessage: String, goal: String, tone: String, context: String) {
        _uiState.value = GeneratorUiState.Loading

        val prompt = """
            You are a WhatsApp Reply Generator for Moroccan users, freelancers, sellers, and small businesses.
            Your job is to generate clear, natural, human WhatsApp replies based on the user's input.

            Rules:
            - Always reply in the same language as the original message unless the user asks otherwise.
            - If the message is in Moroccan Darija, reply in natural Moroccan Darija.
            - If the message is in French, reply in professional natural French.
            - If the message is in English, reply in natural English.
            - Keep the reply short, clear, and suitable for WhatsApp.
            - Do not sound robotic.
            - Do not over-explain.
            - Do not invent facts, prices, dates, promises, or guarantees.
            - If important information is missing, write a reply that asks for it politely.
            - Make the reply respectful, helpful, and professional.
            - Use emojis only when suitable and not too much.
            - Never mention that you are AI.
            - Never add explanations outside the final message.

            Input Details:
            Received message: $receivedMessage
            Goal of my reply: $goal
            Tone: $tone
            Context: $context

            Generate 3 reply options exactly in this format:
            Short: [reply]
            Professional: [reply]
            Friendly: [reply]
        """.trimIndent()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    parseResponse(outputContent)
                } ?: run {
                    _uiState.value = GeneratorUiState.Error("Empty response from AI")
                }
            } catch (e: Exception) {
                _uiState.value = GeneratorUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private fun parseResponse(text: String) {
        try {
            val short = text.substringAfter("Short:", "").substringBefore("Professional:").trim()
            val professional = text.substringAfter("Professional:", "").substringBefore("Friendly:").trim()
            val friendly = text.substringAfter("Friendly:", "").trim()
            
            if (short.isNotEmpty() && professional.isNotEmpty() && friendly.isNotEmpty()) {
                _uiState.value = GeneratorUiState.Success(short, professional, friendly)
            } else {
                // Fallback or better parsing if needed
                 _uiState.value = GeneratorUiState.Error("Could not parse AI response correctly")
            }
        } catch (e: Exception) {
            _uiState.value = GeneratorUiState.Error("Parsing error: ${e.localizedMessage}")
        }
    }
    
    fun reset() {
        _uiState.value = GeneratorUiState.Initial
    }
}