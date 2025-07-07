package com.example.mathsus.ui.methods.secanteMethod

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SecanteViewModel : ViewModel() {
    var message by mutableStateOf("")
        private set

    fun llamarApiCoroutine() {
        viewModelScope.launch{
            llamarApi()

        }
    }

    private suspend fun llamarApi() {
        delay(5000)
        message = "Llamada a la API"
    }
}