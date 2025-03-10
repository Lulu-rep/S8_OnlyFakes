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
                        "authorUid" to FirebaseAuthInstance.auth.uid,
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
                callback(Result.success(posts))
            }
        }
    }
}