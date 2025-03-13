package fr.isen.onlyfakes.view

import ImageService
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.userProfileChangeRequest
import fr.isen.onlyfakes.R
import fr.isen.onlyfakes.enums.ProfileRoutes
import fr.isen.onlyfakes.models.PostModel
import fr.isen.onlyfakes.services.PostsService
import fr.isen.onlyfakes.services.instances.FirebaseAuthInstance
import fr.isen.onlyfakes.view.component.CardPostComponent
import java.io.File
import androidx.core.net.toUri
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun UserProfilView(modifier: Modifier, navController: NavController) {
    val context = LocalContext.current
    val imageService = remember { ImageService() }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val postsService = PostsService()
    var posts by remember { mutableStateOf<List<PostModel>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

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
        Log.d("UserProfilView", "Selected image URI: $uri")
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            Log.d("UserProfilView", "Permission denied")
        }
    }

    // Trigger image upload when imageUri changes
    imageUri?.let { uri ->
        LaunchedEffect(uri) {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.let { stream ->
                val file = File(context.cacheDir, "upload_image.jpg")
                file.outputStream().use { output ->
                    stream.copyTo(output)
                }
                val url = imageService.uploadImage(file.path)
                Log.d("UserProfilView", "Uploaded image URL: $url")
                url?.let{
                    val user = FirebaseAuthInstance.auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        photoUri = it.toUri()
                    }
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("UserProfilView", "User profile updated.")
                            coroutineScope.launch{
                                postsService.updateProfilePictureforPosts(it)
                            }
                        }
                    }
                }
            }
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = FirebaseAuthInstance.auth.currentUser?.photoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(40.dp),
                    placeholder = painterResource(id = R.drawable.ic_launcher_playstore),
                    error = painterResource(id = R.drawable.ic_launcher_playstore),
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = FirebaseAuthInstance.auth.currentUser?.displayName.toString(),
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    Button(onClick = { /* Modifier le profil */ }) {
                        Text("Modifier")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        ) != PermissionChecker.PERMISSION_GRANTED
                    ) {
                        requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        pickImageLauncher.launch("image/*")
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PermissionChecker.PERMISSION_GRANTED
                    ) {
                        requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    } else {
                        pickImageLauncher.launch("image/*")
                    }
                }
            }, modifier = Modifier.padding(top = 16.dp)) {
                Text("Changer la photo")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(ProfileRoutes.PAYEMENT.toString())
                }
            ) {
                Text("Abonnement Premium")
            }
        }

        items(posts) { post ->
            if (FirebaseAuthInstance.auth.currentUser?.uid == post.author["id"]) {
                CardPostComponent(postcard = post, modifier = Modifier.padding(0.dp))
            }
        }
    }
}