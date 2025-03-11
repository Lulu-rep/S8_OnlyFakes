package fr.isen.onlyfakes.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.onlyfakes.models.PostModel
import fr.isen.onlyfakes.services.PostsService
import fr.isen.onlyfakes.view.component.CardPostComponent

@Composable
fun HomeScreen(modifier: Modifier) {
    val postsService = PostsService()
    var posts by remember { mutableStateOf<List<PostModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        postsService.getPosts { result ->
            result.onSuccess { fetchedPosts ->
                posts = fetchedPosts
                Log.d("MainScreen", "Fetched posts: $fetchedPosts")
            }.onFailure { exception ->
                Log.e("MainScreen", "Error fetching posts: ${exception.message}")
            }
        }
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(posts) { posts ->
            CardPostComponent(postcard = posts, modifier = Modifier.padding(0.dp))
        }
    }
}