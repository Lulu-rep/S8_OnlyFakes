import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val username: String = "",
)

class UserServices {
    private val db = FirebaseFirestore.getInstance()

    suspend fun createUser(user: User): Result<Unit> {
        return try {
            db.collection("users").document(user.uid).set(
                mapOf(
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "email" to user.email,
                    "username" to user.username,
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(uid: String): Result<User> {
        return try {
            val document = db.collection("users").document(uid).get().await()
            if (document.exists()) {
                val user = document.toObject(User::class.java)
                user?.let { Result.success(it) } ?: Result.failure(Exception("User not found"))
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            db.collection("users").document(user.uid).update(
                mapOf(
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "email" to user.email,
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun deleteUser(uid: String): Result<Unit> {
        return try {
            db.collection("users").document(uid).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
