package fr.isen.onlyfakes.models

import java.util.Date


data class CommentModel(
    val id: String = "",
    val authorUid: String = "",
    val postId: String = "",
    val content: String = "",
    val date: Date = Date(),
)
