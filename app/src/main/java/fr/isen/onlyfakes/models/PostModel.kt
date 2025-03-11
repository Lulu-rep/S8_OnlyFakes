package fr.isen.onlyfakes.models

import java.util.Date

data class PostModel(
    var id: String = "",
    val author: Map<String, String> = emptyMap(),
    val title: String = "",
    val content: String = "",
    val date: Date = Date(),
    val imageUrl: String = "",
    val likes: List<String> = emptyList(), // List of user ids who liked the post
    val comments: List<CommentModel> = emptyList()
)