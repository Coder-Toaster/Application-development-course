package com.example.oppimistehtv2

import android.R.attr.padding
import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oppimistehtv2.ui.theme.Oppimistehtävä2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Oppimistehtävä2Theme {
                ProfileScreen( modifier = Modifier.fillMaxWidth() )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen (
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {Text("Profile")}
            )
        }
    ) { innerPadding ->
        ProfileContent(
            modifier = Modifier.padding(innerPadding)
        )

    }
}




@Composable
fun InfoRow (
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            // Todo here just want to see how this works
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = typography.bodyMedium
        )
    }
}


@Composable
fun ProfileHeader(
    name: String,
    role: String,
    description: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp) // Adds 6 dp gap between child items
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = role,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ProfileActions (
    // Button "funcition" callbacks
    onContactClick: () -> Unit, //Expl. for myself: Function, takes no arguments, returns nothing.
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onContactClick,
            modifier = Modifier.weight(1f)
        ){
            Text("Contact me")
        }
        Button(
            onClick = onFollowClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("Socials")
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(modifier = Modifier.fillMaxWidth())
        {
            Column (modifier = Modifier.padding(16.dp))
            {
                ProfileHeader(
                    name = "Patrik Verho",
                    role = "Networking technologies student",
                    description = """
                        I am studying 3rd year Networking technologies at Tampere University
                        of Applied Sciences. I am going to graduate in late 2027 if everything goes as
                        planned!""".trimIndent().replace("\n", " ")
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                // Info rows
                InfoRow("School", "TAMK")
                InfoRow("Major", "Networking technologies")
                InfoRow("Academic Interests", "Cybersecurity and IT-architecture")

                Spacer(modifier = Modifier.height(20.dp))

                // Call button functions - To be finished in the future
                ProfileActions(
                    onContactClick = {},
                    onFollowClick = {}
                )
            }
        }

    }
}
@Preview(name = "Complete profile screen",showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    Oppimistehtävä2Theme() {
        ProfileScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun InfoRowPreview() {
    Oppimistehtävä2Theme {
        InfoRow(
            label = "name",
            value = "Patrik"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview(){
    Oppimistehtävä2Theme {
        ProfileHeader(
            name = "patrik",
            role = "Tietotekniikan opiskelija",
            description = "Description comes here"
        )
    }
}