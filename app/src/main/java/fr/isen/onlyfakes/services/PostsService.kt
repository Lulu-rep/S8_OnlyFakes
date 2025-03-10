package fr.isen.onlyfakes.services

import android.util.Log
import fr.isen.onlyfakes.models.PostModel
import fr.isen.onlyfakes.services.instances.FirebaseAuthInstance
import fr.isen.onlyfakes.services.instances.FirebaseFirestoreInstance.db
import kotlinx.coroutines.tasks.await

class PostsService {
    suspend fun createPost(post: PostModel): Result<Unit> {
        return try {
            db.collection("posts").add(
                mapOf(
                    "authorUid" to FirebaseAuthInstance.auth.uid,
                    "title" to post.title,
                    "content" to post.content,
                    "date" to post.date,
                    "imageUrl" to post.imageUrl,
                    "likes" to post.likes,
                    "comments" to post.comments
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PostsService", "createPost: $e")
            Result.failure(e)
        }
    }
}