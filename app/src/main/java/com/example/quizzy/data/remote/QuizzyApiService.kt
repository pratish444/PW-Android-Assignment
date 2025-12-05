package com.example.quizzy.data.remote

import com.example.quizzy.data.model.DashboardResponse
import retrofit2.http.GET

interface QuizzyApiService {
    @GET("student_dashboard.json?alt=media&token=0091b4c2-2ee2-4326-99cd-96d5312b34bd")
    suspend fun getDashboardData(): DashboardResponse
}
