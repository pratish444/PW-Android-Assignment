package com.example.quizzy.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizzy.presentation.home.HomeViewModel

data class NotificationItem(
    val title: String,
    val time: String,
    val backgroundColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val notifications = listOf(
        NotificationItem(
            "Missed quiz in physics in yesterday",
            "2 hours ago",
            Color(0xFFFFEBEE)
        ),
        NotificationItem(
            "Badge earned",
            "8 hours ago",
            Color(0xFFF3E5F5)
        ),
        NotificationItem(
            "Teacher Note",
            "1 day ago",
            Color(0xFFE8F5E9)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications & Settings",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notifications Section
            item {
                Text(
                    text = "Notifications",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(notifications) { notification ->
                NotificationCard(notification = notification)
            }

            // Settings Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = "Switch Child",
                    subtitle = "Change active child profile",
                    onClick = { }
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Default.Language,
                    title = "Language",
                    subtitle = "English",
                    onClick = { }
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Default.ExitToApp,
                    title = "Logout",
                    subtitle = "Sign out of your account",
                    onClick = { showLogoutDialog = true },
                    iconColor = Color(0xFFEF5350)
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.signOut()
                        onLogout()
                    }
                ) {
                    Text("Logout", color = Color(0xFFEF5350))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NotificationCard(notification: NotificationItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = notification.backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = notification.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.time,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    iconColor: Color = Color.Black
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = Color.Gray
            )
        }
    }
}