package fr.isen.onlyfakes

import AuthService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.onlyfakes.enums.Routes
import fr.isen.onlyfakes.ui.theme.OnlyFakesTheme
import fr.isen.onlyfakes.view.LoginScreen
import fr.isen.onlyfakes.view.HomeScreen
import fr.isen.onlyfakes.view.component.headerBar
import fr.isen.onlyfakes.view.component.navigationBar
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            AuthService().logInUser("test@test.com", "123456")
        }
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            OnlyFakesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { headerBar() },
                    bottomBar = { navigationBar(navController) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        NavHost(navController = navController, startDestination = Routes.HOME.toString(), builder = {
                            composable(Routes.HOME.toString()) { HomeScreen(Modifier.padding(innerPadding)) }
                            composable(Routes.ADDPOST.toString()) { /*AddPostScreen(Modifier.padding(innerPadding))*/ }
                            composable(Routes.ACCOUNT.toString()) { /*AccountScreen(mod = Modifier.padding(innerPadding))*/ }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OnlyFakesTheme {
        LoginScreen()
    }
}