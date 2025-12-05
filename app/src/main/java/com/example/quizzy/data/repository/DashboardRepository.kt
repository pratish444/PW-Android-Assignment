package com.example.quizzy.data.repository

import com.example.quizzy.data.model.DashboardResponse
import com.example.quizzy.data.remote.QuizzyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor(
    private val apiService: QuizzyApiService
) {
    suspend fun getDashboardData(): Result<DashboardResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getDashboardData()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}