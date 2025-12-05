package com.example.quizzy.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun isUserAuthenticated(): Boolean = currentUser != null

    suspend fun signInWithCustomToken(schoolId: String, studentId: String): Result<FirebaseUser> {
        return try {
            // For demo purposes, creating a mock email/password auth
            // In production, you'd validate schoolId/studentId with backend
            val email = "${studentId}@${schoolId}.school.com"
            val password = "Student@123" // In production, get from secure backend

            try {
                // Try to sign in first
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                // If sign in fails, create new user
                val createResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                Result.success(createResult.user!!)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }

    fun getAuthStateFlow(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }
}
