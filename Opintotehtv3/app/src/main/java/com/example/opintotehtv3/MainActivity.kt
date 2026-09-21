package com.example.opintotehtv3

import android.R.attr.onClick
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.opintotehtv3.ui.theme.Opintotehtävä3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Opintotehtävä3Theme {
                ProfileScreen(
                    modifier = Modifier.fillMaxSize()
                )
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
                title = {
                    Text(text = stringResource(R.string.profile))
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Functionality comes here
                        },
                        modifier = Modifier.height(44.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),

                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat, // Used to AI to find this dependency to add icons
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.padding(end=1.dp))

                    IconButton(
                        onClick= {
                            // Login functionality -> Open new activity
                        },
                        modifier = Modifier.height(45.dp)
                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Login,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer ( modifier = Modifier.width(16.dp) )
                    }
                    // Add little space between edge of the screen and login button for ease of use
                    Spacer( modifier = Modifier.padding(end = 5.dp))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
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
        // Person's name and profile picture
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painterResource(id = R.drawable.cat_5968876_960_720),
                contentDescription = "Profile picture of me",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop

            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
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
            Text(text = stringResource(R.string.contact_button))
        }
        Button(
            onClick = onFollowClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(text=stringResource(R.string.socials_button))
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
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column (modifier = Modifier.padding(16.dp)) {
                // These text are to be in english as it is the standard CS language...
                ProfileHeader(
                    name = "Patrik Verho",
                    role = "Networking technologies student",
                    description = stringResource(R.string.person_description)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                // Info rows
                InfoRow(stringResource(R.string.school_variable), "TAMK")
                InfoRow(stringResource(R.string.major_variable), "Networking technologies")
                InfoRow(stringResource(R.string.intrests_variable), "Cybersecurity and IT-architecture")

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

// Default language preview
@Preview(name="Complete profile screen", showBackground=true, showSystemUi=true)
@Composable
fun CompleteProfileScreenPreview(){
    Opintotehtävä3Theme {
        ProfileScreen()
    }
}

// Finnish language preview
@Preview(
    name = "Finnish language Complete profile screen",
    showBackground = true,
    showSystemUi = true,
    locale = "fi"
)
@Composable
fun FinnishCompleteProfileScreenPreview(){
    Opintotehtävä3Theme {
        ProfileScreen()
    }
}