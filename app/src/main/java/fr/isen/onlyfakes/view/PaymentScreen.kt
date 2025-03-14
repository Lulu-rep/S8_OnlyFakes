package fr.isen.onlyfakes.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PaymentView( mod: Modifier) {

    var isClicked by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = mod
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Abonnement Premium",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Profitez de tous les avantages exclusifs en vous abonnant dès maintenant !",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Liste des avantages du Premium
        items(
            listOf(
                "✅ Accès à des contenus exclusifs de créateurs",
                "✅ Accès aux messages privés et interaction en direct",
                "✅ Possibilité de rejoindre des lives privés et des événements exclusifs",
                "✅ Accès à des filtres et effets spéciaux pour vos photos et vidéos",
                "✅ Accès aux publications et stories sans publicité"
            )
        ) { avantage ->
            Text(
                text = avantage,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isClicked = !isClicked
                    //fct quand on clique pour s abonner
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isClicked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "S'abonner",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 18.sp
                )
            }
        }
    }
}

