package fr.isen.onlyfakes.view

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import fr.isen.onlyfakes.R

@Composable
fun LoginScreen() {
    Column {
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically )
        {
            Image(
                painter= painterResource(id = R.drawable.onlyfakes_logo),
                contentDescription = "",
            )
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
    login_card()
}

@Composable
fun login_card() {
    var inputlogin by remember { mutableStateOf("") }
    var inputpassword by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) //
                .fillMaxWidth()
        ) {
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
                placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO */ },
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
                    .clickable { /* TODO */ }
                    .padding(8.dp)
            )
            Text(
                text = stringResource(id = R.string.register_option),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO */ }
                    .padding(8.dp))
        }
    }
}

@Composable
fun create_account_card() {
    var inputlogin by remember { mutableStateOf("") }
    var inputpassword by remember { mutableStateOf("") }
    var confirmationpassword by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) //
                .fillMaxWidth()
        ) {
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
                placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmationpassword,
                singleLine = true,
                onValueChange = { confirmationpassword = it },
                label = { Text(text = stringResource(id = R.string.confirmation_password)) },
                placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO */ },
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

@Composable
fun card_reset_password() {
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
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputlogin,
                singleLine = true,
                onValueChange = { inputlogin = it },
                label = { Text(text = stringResource(id = R.string.login_label)) },
                placeholder = { Text(text = stringResource(id = R.string.login_placeholder)) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Centrage correct du texte et du bouton
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.cancel_button),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { /* TODO */ }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { /* TODO */ },
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