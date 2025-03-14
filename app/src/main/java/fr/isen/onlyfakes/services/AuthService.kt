import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.userProfileChangeRequest
import fr.isen.onlyfakes.services.instances.FirebaseAuthInstance.auth
import kotlinx.coroutines.tasks.await

class AuthService {

    suspend fun registerUser(email: String, password: String, username: String): Result<Unit> {
        return try {
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            val user = userCredential.user

            if (user != null) {
                user.updateProfile(
                    userProfileChangeRequest {
                        displayName = username
                    }).await()
                    user.sendEmailVerification().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("User registration failed"))
            }
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_WEAK_PASSWORD" -> Result.failure(Exception("The password provided is too weak."))
                "ERROR_EMAIL_ALREADY_IN_USE" -> Result.failure(Exception("The account already exists for that email."))
                else -> Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logInUser(email: String, password: String): Result<Unit> {
        return try {
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            val user = userCredential.user

            if (user != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> Result.failure(Exception("No user found for that email."))
                "ERROR_WRONG_PASSWORD" -> Result.failure(Exception("Wrong password provided for that user."))
                else -> Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOutUser(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> Result.failure(Exception("No user found for that email."))
                else -> Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editUsername(username: String): Result<Unit>{
        return try{
            val user = auth.currentUser
            user?.updateProfile(
                userProfileChangeRequest {
                    displayName = username
                })?.await()
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> Result.failure(Exception("No user found for that email."))
                else -> Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}