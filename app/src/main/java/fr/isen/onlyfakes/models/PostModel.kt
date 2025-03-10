package fr.isen.onlyfakes.models

data class PostModel (
    val id: String,
    val authorUid: String,
    val title: String,
    val content: String,
    val date: String,
    val imageUrl: String,
    val likes: List<String>, // List of user ids who liked the post
    val comments: List<CommentModel>
)