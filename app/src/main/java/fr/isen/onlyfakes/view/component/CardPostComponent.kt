package fr.isen.onlyfakes.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.onlyfakes.R
import fr.isen.onlyfakes.models.PostModel
import fr.isen.onlyfakes.services.PostsService
import fr.isen.onlyfakes.services.instances.FirebaseAuthInstance
import kotlinx.coroutines.launch

@Composable
fun CardPostComponent(postcard: PostModel, modifier: Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var userComment by remember { mutableStateOf("") }
    var isLiked by remember { mutableStateOf(postcard.likes.contains(FirebaseAuthInstance.auth.uid)) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_playstore),
                        contentDescription = "Profile picture",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("username", style = MaterialTheme.typography.bodyLarge)
                }
                if(postcard.authorUid == FirebaseAuthInstance.auth.uid) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                PostsService().deletePost(postcard.id)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Delete post")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_playstore),
                contentDescription = "Post picture",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(postcard.title, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(postcard.content, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            if (isLiked) {
                                PostsService().unlikePost(postcard.id)
                            } else {
                                PostsService().likePost(postcard.id)
                            }
                            isLiked = !isLiked
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like"
                    )
                }

                TextField(
                    value = userComment,
                    onValueChange = { userComment = it },
                    placeholder = { Text("Comment") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )

                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            PostsService().addComment(postcard.id, userComment)
                            userComment = ""
                        }

                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                postcard.comments.forEach { comment ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_playstore),
                                contentDescription = "Comment author picture",
                                modifier = Modifier.size(30.dp).clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(comment.content, style = MaterialTheme.typography.bodyMedium)
                                Text("username", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        if (comment.authorUid == FirebaseAuthInstance.auth.uid) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        PostsService().deleteComment(postcard.id, comment)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete comment")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}