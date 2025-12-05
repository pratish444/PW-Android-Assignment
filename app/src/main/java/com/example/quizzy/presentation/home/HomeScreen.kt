package com.example.quizzy.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizzy.data.model.DashboardResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Error: ${uiState.error}", color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadDashboardData() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            uiState.data != null -> {
                DashboardContent(
                    data = uiState.data!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun DashboardContent(data: DashboardResponse, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Hello ${data.studentName ?: "Student"}!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = data.studentClass ?: "Class",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // Stats Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsCard(
                    title = "Availability",
                    value = data.availability ?: "N/A",
                    icon = Icons.Default.CheckCircle,
                    backgroundColor = Color(0xFFE8F5E9),
                    iconColor = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    title = "Quiz",
                    value = "${data.quizAttempts ?: 0} Attempt",
                    icon = Icons.Default.Edit,
                    backgroundColor = Color(0xFFFFF3E0),
                    iconColor = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    title = "Accuracy",
                    value = "${data.accuracy ?: 0}%",
                    icon = Icons.Default.Star,
                    backgroundColor = Color(0xFFFFEBEE),
                    iconColor = Color(0xFFF44336),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Today's Summary
        item {
            TodaySummaryCard(
                focusStatus = data.todaySummary?.focusStatus ?: "Focused",
                description = data.todaySummary?.description ?: "No description available",
                videoTitle = data.todaySummary?.videoRecommendation?.title ?: "No video"
            )
        }

        // Weekly Overview Title
        item {
            Text(
                text = "Weekly Overview",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Quiz Streak
        if (data.quizStreak?.days?.isNotEmpty() == true) {
            item {
                QuizStreakCard(streakDays = data.quizStreak.days)
            }
        }

        // Accuracy Card
        item {
            AccuracyCard(accuracy = data.weeklyAccuracy ?: 0)
        }

        // Performance by Topic
        if (data.performanceByTopic?.isNotEmpty() == true) {
            item {
                PerformanceByTopicCard(topics = data.performanceByTopic)
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun TodaySummaryCard(
    focusStatus: String,
    description: String,
    videoTitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Today's Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF9C27B0).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔍", fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = focusStatus,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9C27B0)
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Watch: $videoTitle", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun QuizStreakCard(streakDays: List<com.example.quizzy.data.model.StreakDay>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Quiz Streak",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(streakDays) { day ->
                    StreakDayItem(day = day)
                }
            }
        }
    }
}

@Composable
fun StreakDayItem(day: com.example.quizzy.data.model.StreakDay) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (day.completed) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (day.completed) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Text(
            text = day.day,
            fontSize = 12.sp,
            color = if (day.completed) Color.Black else Color.Gray
        )
    }
}

@Composable
fun AccuracyCard(accuracy: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Accuracy",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Accuracy",
                    tint = Color(0xFFFF9800)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$accuracy% correct",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { accuracy / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFFFF5252)
            )
        }
    }
}

private fun ColumnScope.LinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier,
    color: Color
) {
}

@Composable
fun PerformanceByTopicCard(topics: List<com.example.quizzy.data.model.TopicPerformance>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Performance by Topic",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = Color(0xFF2196F3)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            topics.forEach { topic ->
                TopicPerformanceItem(topic = topic)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TopicPerformanceItem(topic: com.example.quizzy.data.model.TopicPerformance) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = topic.topic ?: "Unknown Topic",
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    try {
                        Color(android.graphics.Color.parseColor(topic.color ?: "#9C27B0"))
                    } catch (e: Exception) {
                        Color(0xFF9C27B0)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${topic.score ?: 0}%",
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}