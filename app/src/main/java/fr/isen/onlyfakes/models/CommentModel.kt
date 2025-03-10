package fr.isen.onlyfakes.models

import java.util.Date


data class CommentModel(
    val authorUid: String = "",
    val content: String = "",
    val date: Date = Date(),
)
