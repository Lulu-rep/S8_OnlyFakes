package fr.isen.onlyfakes.models

data class CommentModel(
    val id: String,
    val authorUid: String,
    val postId: String,
    val content: String,
    val date: String
)
