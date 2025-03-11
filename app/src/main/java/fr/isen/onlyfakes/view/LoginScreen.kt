package fr.isen.onlyfakes.view

import AuthService
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import fr.isen.onlyfakes.R
import kotlinx.coroutines.launch
import fr.isen.onlyfakes.MainActivity

@Composable
fun LoginScreen() {
    var currentScreen by remember { mutableStateOf("login") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.onlyfakes_logo),
            contentDescription = stringResource(R.string.logo_description),
        )
        Spacer(modifier = Modifier.height(32.dp))
        when (currentScreen) {
            "login" -> LoginCard(
                onRegisterClick = { currentScreen = "register" },
                onForgotPasswordClick = { currentScreen = "reset_password" }
            )

            "register" -> CreateAccountCard(
                onBackToLogin = { currentScreen = "login" }
            )

            "reset_password" -> CardResetPassword(
                onBackToLogin = { currentScreen = "login" }
            )
        }
    }
}

@Composable
fun LoginCard(onRegisterClick: () -> Unit, onForgotPasswordClick: () -> Unit) {
    var inputlogin by remember { mutableStateOf("") }
    var inputpassword by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputlogin,
                singleLine = true,
                onValueChange = { inputlogin = it },
                label = { Text(text = stringResource(id = R.string.login_label)) },
                placeholder = { Text(text = stringResource(id = R.string.login_placeholder)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputpassword,
                singleLine = true,
                onValueChange = { inputpassword = it },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        val result = AuthService().logInUser(inputlogin.trim(), inputpassword.trim())
                        if (result.isSuccess) {
                            val intent = Intent(context, MainActivity::class.java).apply {}
                            context.startActivity(intent)
                        }else{
                            val toast = Toast.makeText(context, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Text(stringResource(id = R.string.sign_in_button))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.forget_password_option),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onForgotPasswordClick() }
                    .padding(8.dp)
            )
            Text(
                text = stringResource(id = R.string.register_option),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRegisterClick() }
                    .padding(8.dp))
        }
    }
}

@Composable
fun CreateAccountCard(onBackToLogin: () -> Unit) {
    var inputlogin by remember { mutableStateOf("") }
    var inputpassword by remember { mutableStateOf("") }
    var inputusername by remember { mutableStateOf("") }
    var confirmationpassword by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) //
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.register_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputlogin,
                singleLine = true,
                onValueChange = { inputlogin = it },
                label = { Text(text = stringResource(id = R.string.login_label)) },
                placeholder = { Text(text = stringResource(id = R.string.login_placeholder)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputusername,
                singleLine = true,
                onValueChange = { inputusername = it },
                label = { Text(text = stringResource(id = R.string.username_label)) },
                placeholder = { Text(text = stringResource(id = R.string.username_placeholder)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputpassword,
                singleLine = true,
                onValueChange = { inputpassword = it },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmationpassword,
                singleLine = true,
                onValueChange = { confirmationpassword = it },
                label = { Text(text = stringResource(id = R.string.confirmation_password)) },
                placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.cancel_button),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { onBackToLogin() }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if(inputpassword == confirmationpassword){
                                val result = AuthService().registerUser(inputlogin.trim(), inputpassword.trim(), inputusername.trim());
                                if (result.isSuccess) {
                                    val intent = Intent(context, MainActivity::class.java).apply {}
                                    context.startActivity(intent)
                                }else{
                                    val toast = Toast.makeText(context, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT)
                                    toast.show()
                                }
                            }else{
                                val toast = Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(stringResource(id = R.string.creation_account_button))
                }
            }
        }
    }
}

@Composable
fun CardResetPassword(onBackToLogin: () -> Unit) {
    var inputlogin by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.reset_password),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputlogin,
                singleLine = true,
                onValueChange = { inputlogin = it },
                label = { Text(text = stringResource(id = R.string.login_label)) },
                placeholder = { Text(text = stringResource(id = R.string.login_placeholder)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.cancel_button),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { onBackToLogin() }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(stringResource(id = R.string.reset_password))
                }
            }
        }
    }
}

@Composable
fun CardChangePassword(onBackToLogin: () -> Unit) {
    var inputpassword by remember { mutableStateOf("") }
    var passwordconfirmation by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.reset_password),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 24.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputpassword,
                singleLine = true,
                onValueChange = { inputpassword = it },
                label = { Text(text = stringResource(id = R.string.newpassword_label)) },
                placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = passwordconfirmation,
                singleLine = true,
                onValueChange = { passwordconfirmation = it },
                label = { Text(text = stringResource(id = R.string.confirmation_password)) },
                placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.cancel_button),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { onBackToLogin() }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(stringResource(id = R.string.change_password_button))
                }
            }
        }
    }
}