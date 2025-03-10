import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import fr.isen.onlyfakes.models.UserModel
import kotlinx.coroutines.tasks.await

class UserServices {
    private val db = FirebaseFirestore.getInstance()

    suspend fun createUser(user: UserModel): Result<Unit> {
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

    suspend fun getUser(uid: String): Result<UserModel> {
        return try {
            val document = db.collection("users").document(uid).get().await()
            if (document.exists()) {
                val user = document.toObject(UserModel::class.java)
                user?.let { Result.success(it) } ?: Result.failure(Exception("User not found"))
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: UserModel): Result<Unit> {
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
