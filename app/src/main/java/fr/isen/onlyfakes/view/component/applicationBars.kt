package fr.isen.onlyfakes.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import fr.isen.onlyfakes.R
import fr.isen.onlyfakes.enums.Routes

@Composable
@Preview
fun navigationBar(navController: NavController){
    Row(
        Modifier.fillMaxWidth()
            .padding(bottom = 40.dp)
            .padding(10.dp)
            .clip(shape = RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ){
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, "HomeLogo") },
            selected = false,
            onClick = {navController.navigate(Routes.HOME.toString())},
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.AddCircle, "AddPost") },
            selected = false,
            onClick = {navController.navigate(Routes.ADDPOST.toString())},
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.AccountCircle, "AddPost") },
            selected = false,
            onClick = {navController.navigate(Routes.ACCOUNT.toString())},
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun headerBar(){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically )
            {
                Image(
                    painter= painterResource(id = R.drawable.ic_launcher_playstore),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
            }
        }
    )
}