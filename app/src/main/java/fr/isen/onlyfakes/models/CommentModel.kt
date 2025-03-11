package fr.isen.onlyfakes.models

import java.util.Date


data class CommentModel(
    val author: Map<String, String> = emptyMap(),
    val content: String = "",
    val date: Date = Date(),
)
