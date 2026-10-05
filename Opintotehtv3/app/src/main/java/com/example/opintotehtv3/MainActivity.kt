package com.example.opintotehtv3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.opintotehtv3.ui.theme.Opintotehtävä3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Opintotehtävä3Theme {
                ProfileApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun ProfileApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ProfileListRoute,
        modifier = modifier
    ) {
        composable<ProfileListRoute> {
            ProfileListScreen(
                profiles = sampleProfiles,
                onProfileClick = { profileId ->
                    // Only the ID travels in the route, never the whole Profile object.
                    navController.navigate(ProfileDetailRoute(profileId)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<ProfileDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ProfileDetailRoute>()
            val profile = findProfileById(sampleProfiles, route.profileId)
            val onBackClick: () -> Unit = { navController.navigateUp() }

            if (profile != null) {
                ProfileDetailScreen(profile = profile, onBackClick = onBackClick)
            } else {
                ProfileNotFoundScreen(onBackClick = onBackClick)
            }
        }
    }
}
