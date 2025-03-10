import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import fr.isen.onlyfakes.models.UserModel
import fr.isen.onlyfakes.services.UserServices
import fr.isen.onlyfakes.services.instances.FirebaseAuthInstance
import kotlinx.coroutines.tasks.await

class AuthService {
    private val auth: FirebaseAuth = FirebaseAuthInstance.auth

    suspend fun registerUser(email: String, password: String, firstName: String, lastName: String, username: String): Result<Unit> {
        return try {
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            val user = userCredential.user

            if (user != null) {
                UserServices().createUser(UserModel(
                    uid = user.uid,
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    email = email
                ))
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
}