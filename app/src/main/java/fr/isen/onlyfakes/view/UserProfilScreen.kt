package fr.isen.onlyfakes.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import fr.isen.onlyfakes.R
import fr.isen.onlyfakes.models.PostModel
import fr.isen.onlyfakes.services.PostsService
import fr.isen.onlyfakes.services.instances.FirebaseAuthInstance
import fr.isen.onlyfakes.view.component.CardPostComponent


@Composable
fun UserProfilView( modifier: Modifier) {
    var username by remember { mutableStateOf("BOT") }
    var description by remember { mutableStateOf("I am just a bot") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
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
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LazyColumn(modifier = modifier
        .fillMaxSize()
    ) {
        item {
            // Conteneur Row pour aligner photo et informations utilisateur
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                // Image de profil avec possibilité de changer
                AsyncImage(
                    model = FirebaseAuthInstance.auth.currentUser?.photoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(40.dp),
                    placeholder = painterResource(id = R.drawable.ic_launcher_playstore),
                    error = painterResource(id = R.drawable.ic_launcher_playstore),
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Informations sur l'utilisateur (Nom et description)
                Column {
                    Text(text = FirebaseAuthInstance.auth.currentUser?.displayName.toString(), fontSize = 20.sp, color = Color.Black)
                    Text(text = description, fontSize = 14.sp, color = Color.Gray)
                    Button(onClick = { /* Modifier le profil */ }) {
                        Text("Modifier")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton pour changer la photo
            Button(onClick = { pickImageLauncher.launch("image/*") }, modifier = Modifier.padding(top = 16.dp)) {
                Text("Changer la photo")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Liste des posts
        items(posts) { posts ->
            CardPostComponent(postcard = posts, modifier = Modifier.padding(0.dp))
        }
    }
}




