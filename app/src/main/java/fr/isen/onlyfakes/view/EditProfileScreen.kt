package fr.isen.onlyfakes.view

import AuthService
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.rpc.context.AttributeContext.Auth
import fr.isen.onlyfakes.enums.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EditProfileView(currentName: String, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var newName by remember { mutableStateOf("") }
    var context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp), // Ajoute un espacement entre les éléments
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(text = "Modifier le profil", fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }


        item {
            Text(text = "Nom actuel : $currentName",textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Nouveau nom") },
            )
        }

        item {
            Button(onClick = {
                coroutineScope.launch {
                    var response = AuthService().editUsername(newName)
                    if(response.isSuccess){
                        navController.navigate(Routes.HOME.toString())
                    } else {
                        val toast = Toast.makeText(context, response.exceptionOrNull()?.message, Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            }) {
                Text(text = "Enregistrer",
                    color = MaterialTheme.colorScheme.background)
            }
        }
    }
}
