package fr.isen.onlyfakes.view

import AuthService
import ImageService
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import fr.isen.onlyfakes.LoginActivity
import kotlinx.coroutines.launch


@Composable
fun UserProfilView(modifier: Modifier, navController: NavController, user_id : String) {
    val context = LocalContext.current

    val postsService = PostsService()
    var user_posts = remember { mutableStateListOf<PostModel>() }


    LaunchedEffect(Unit) {
        postsService.getPosts { result ->
            result.onSuccess { fetchedPosts ->
                for(post in fetchedPosts){
                    if (post.author["id"] == user_id){
                        user_posts.add(post)
                    }
                }
                Log.d("MainScreen", "Fetched posts: $fetchedPosts")
            }.onFailure { exception ->
                Log.e("MainScreen", "Error fetching posts: ${exception.message}")
            }
        }
    }


    if(user_id == FirebaseAuthInstance.auth.currentUser?.uid){

        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {
                currentUserHeadBand(modifier, navController)
            }
            if(!user_posts.isEmpty()) {
                items(user_posts) { post ->
                    CardPostComponent(
                        postcard = post,
                        modifier = Modifier.padding(0.dp),
                        navController
                    )
                }
            }

        }

    }
    else{
        if(!user_posts.isEmpty()){
            //otherUserHeadBand(modifier = modifier, navController = navController, user_posts[0])
            LazyColumn(modifier = modifier.fillMaxSize()) {
                item {
                    otherUserHeadBand(modifier = modifier, navController = navController, user_posts[0])
                }
                items(user_posts) { post ->
                    CardPostComponent(postcard = post, modifier = Modifier.padding(0.dp), navController)
                }
            }
        }
    }


}



@Composable
fun currentUserHeadBand(modifier: Modifier, navController: NavController){
    val context = LocalContext.current
    val imageService = remember { ImageService() }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()


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
                                PostsService().updateProfilePictureforPosts(it)
                            }
                        }
                    }
                }
            }
        }
    }



    Column() {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = FirebaseAuthInstance.auth.currentUser?.photoUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(60.dp),
                placeholder = painterResource(id = R.drawable.defaultprofilepic),
                error = painterResource(id = R.drawable.defaultprofilepic),
            )

            Spacer(modifier = Modifier.width(8.dp))

            FirebaseAuthInstance.auth.currentUser?.displayName?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.navigate(ProfileRoutes.MODIFY.toString()) }) {
                Text("Modifier")
            }
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
            }
            ) {
                Text("Changer la photo")
            }
        }

        Button(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                coroutineScope.launch {
                    val result = AuthService().signOutUser()
                    if (result.isSuccess) {
                        val intent = Intent(context, LoginActivity::class.java).apply {}
                        context.startActivity(intent)
                    } else {
                        val toast = Toast.makeText(
                            context,
                            result.exceptionOrNull()?.message,
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            },
        ) {
            Text(
                text = "Disconnect",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 6.dp)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Logout Icon",
                modifier = Modifier.size(30.dp)
            )
        }
        

    }


}


@Composable
fun otherUserHeadBand(modifier: Modifier, navController: NavController, postModel: PostModel){
    Column() {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = postModel.author["imageUrl"],
                contentDescription = "Profile Picture",
                modifier = Modifier.size(60.dp),
                placeholder = painterResource(id = R.drawable.defaultprofilepic),
                error = painterResource(id = R.drawable.defaultprofilepic),
            )

            Spacer(modifier = Modifier.width(8.dp))

            postModel.author["name"]?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Button(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            onClick = {
            navController.navigate(ProfileRoutes.PAYEMENT.toString())
        }
        ) {
            Text("Abonnement Premium")
        }
    }

}