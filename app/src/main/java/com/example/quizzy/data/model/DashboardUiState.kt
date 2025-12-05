package com.example.quizzy.data.model

data class DashboardUiState(
    val isLoading: Boolean = false,
    val data: DashboardResponse? = null,
    val error: String? = null
)