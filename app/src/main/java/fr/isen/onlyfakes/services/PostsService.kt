package fr.isen.onlyfakes.services

import android.util.Log
import com.google.firebase.firestore.FieldValue
import fr.isen.onlyfakes.models.CommentModel
import fr.isen.onlyfakes.models.PostModel
import fr.isen.onlyfakes.services.instances.FirebaseAuthInstance
import fr.isen.onlyfakes.services.instances.FirebaseFirestoreInstance.db
import kotlinx.coroutines.tasks.await
import java.util.Date

class PostsService {
    suspend fun createPost(post: PostModel): Result<Unit> {
        return try {
            db.collection("posts").add(
                mapOf(
                    "author" to mapOf(
                        "id" to FirebaseAuthInstance.auth.uid,
                        "name" to FirebaseAuthInstance.auth.currentUser?.displayName,
                        "imageUrl" to FirebaseAuthInstance.auth.currentUser?.photoUrl.toString()
                    ),
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

    suspend fun deletePost(postId: String): Result<Unit> {
        Log.e("PostsService", "deletePost: $postId")
        return try {
            db.collection("posts").document(postId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PostsService", "deletePost: $e")
            Result.failure(e)
        }
    }

    suspend fun likePost(postId: String): Result<Unit> {
        return try {
            db.collection("posts").document(postId).update(
                "likes", FieldValue.arrayUnion(FirebaseAuthInstance.auth.uid)
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PostsService", "likePost: $e")
            Result.failure(e)
        }
    }

    suspend fun unlikePost(postId: String): Result<Unit> {
        return try {
            db.collection("posts").document(postId).update(
                "likes", FieldValue.arrayRemove(FirebaseAuthInstance.auth.uid)
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PostsService", "unlikePost: $e")
            Result.failure(e)
        }
    }

    suspend fun addComment(postId: String, comment: String): Result<Unit> {
        return try {
            db.collection("posts").document(postId).update(
                "comments", FieldValue.arrayUnion(
                    mapOf(
                        "author" to mapOf(
                            "id" to FirebaseAuthInstance.auth.uid,
                            "name" to FirebaseAuthInstance.auth.currentUser?.displayName,
                            "imageUrl" to FirebaseAuthInstance.auth.currentUser?.photoUrl.toString()
                        ),
                        "content" to comment,
                        "date" to Date()
                    )
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PostsService", "addComment: $e")
            Result.failure(e)
        }
    }

    suspend fun deleteComment(postId: String, comment: CommentModel): Result<Unit> {
        Log.d("PostsService", "deleteComment: $comment")
        return try {
            db.collection("posts").document(postId).update(
                "comments", FieldValue.arrayRemove(comment)
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PostsService", "deleteComment: $e")
            Result.failure(e)
        }
    }

    fun getPosts(callback: (Result<List<PostModel>>) -> Unit) {
        db.collection("posts").addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("PostsService", "Listen failed.", e)
                callback(Result.failure(e))
                return@addSnapshotListener
            }

            if (snapshots != null) {
                val posts = mutableListOf<PostModel>()
                for (document in snapshots.documents) {
                    document.toObject(PostModel::class.java)?.let { post ->
                        post.id = document.id
                        posts.add(post)
                    }
                }
                Log.d("PostsService", "Posts: $posts")
                posts.sortByDescending { it.date }
                callback(Result.success(posts))
            }
        }
    }

    suspend fun updateProfilePictureforPosts(newUrl: String): Result<Unit>{
        return try{
            val userId = FirebaseAuthInstance.auth.uid
            val posts = db.collection("posts").whereEqualTo("author.id", userId).get().await()
            for (post in posts){
                db.collection("posts").document(post.id).update("author.imageUrl", newUrl).await()
            }
            Result.success(Unit)
        } catch (e: Exception){
            Log.e("PostsService", "updateProfilePictureforPosts: $e")
            Result.failure(e)
        }
    }

    suspend fun updateUserDisplayName(newName: String): Result<Unit>{
        return try{
            val userId = FirebaseAuthInstance.auth.uid
            val posts = db.collection("posts").whereEqualTo("author.id", userId).get().await()
            for (post in posts){
                db.collection("posts").document(post.id).update("author.name", newName).await()
            }
            Result.success(Unit)
        } catch (e: Exception){
            Log.e("PostsService", "updateUserDisplayName: $e")
            Result.failure(e)
        }
    }
}