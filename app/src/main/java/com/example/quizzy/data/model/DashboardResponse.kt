package com.example.quizzy.data.model

import com.google.gson.annotations.SerializedName

// Main response wrapper
data class DashboardResponse(
    @SerializedName("student") val student: Student,
    @SerializedName("todaySummary") val todaySummary: TodaySummary,
    @SerializedName("weeklyOverview") val weeklyOverview: WeeklyOverview
) {
    // Helper properties for easy access in UI
    val studentName: String get() = student.name
    val studentClass: String get() = student.`class`
    val availability: String get() = student.availability.status
    val quizAttempts: Int get() = student.quiz.attempts
    val accuracy: String get() = student.accuracy.current
    val quizStreak: QuizStreak get() = QuizStreak(weeklyOverview.quizStreak)
    val weeklyAccuracy: Int get() = weeklyOverview.overallAccuracy.percentage
    val performanceByTopic: List<TopicPerformance> get() = weeklyOverview.performanceByTopic
}

// Student data
data class Student(
    @SerializedName("name") val name: String,
    @SerializedName("class") val `class`: String,
    @SerializedName("availability") val availability: Availability,
    @SerializedName("quiz") val quiz: Quiz,
    @SerializedName("accuracy") val accuracy: Accuracy
)

data class Availability(
    @SerializedName("status") val status: String
)

data class Quiz(
    @SerializedName("attempts") val attempts: Int
)

data class Accuracy(
    @SerializedName("current") val current: String
)

// Today's Summary
data class TodaySummary(
    @SerializedName("mood") val mood: String,
    @SerializedName("description") val description: String,
    @SerializedName("recommendedVideo") val recommendedVideo: RecommendedVideo,
    @SerializedName("characterImage") val characterImage: String
) {
    // Helper properties for UI
    val focusStatus: String get() = mood
    val videoRecommendation: VideoRecommendation get() = VideoRecommendation(
        title = recommendedVideo.title,
        url = ""
    )
}

data class RecommendedVideo(
    @SerializedName("title") val title: String,
    @SerializedName("actionText") val actionText: String
)

// Weekly Overview
data class WeeklyOverview(
    @SerializedName("quizStreak") val quizStreak: List<StreakDay>,
    @SerializedName("overallAccuracy") val overallAccuracy: OverallAccuracy,
    @SerializedName("performanceByTopic") val performanceByTopic: List<TopicPerformance>
)

data class QuizStreak(
    val days: List<StreakDay>
)

data class OverallAccuracy(
    @SerializedName("percentage") val percentage: Int,
    @SerializedName("label") val label: String
)

data class StreakDay(
    @SerializedName("day") val day: String,
    @SerializedName("status") val status: String
) {
    val completed: Boolean get() = status == "done"
}

data class TopicPerformance(
    @SerializedName("topic") val topic: String,
    @SerializedName("trend") val trend: String
) {
    // Generate color based on trend
    val color: String get() = when (trend) {
        "up" -> "#4CAF50"
        "down" -> "#F44336"
        else -> "#9C27B0"
    }

    // Generate a sample score based on trend (since JSON doesn't have scores)
    val score: Int get() = when (trend) {
        "up" -> 85
        "down" -> 55
        else -> 70
    }
}

// Video Recommendation (for compatibility with UI)
data class VideoRecommendation(
    val title: String,
    val url: String
)