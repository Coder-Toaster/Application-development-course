package com.example.opintotehtv3

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Profile(
    val id: Int,
    val name: String,
    val role: String,
    val school: String,
    val major: String,
    val interests: String,
    @param:StringRes val descriptionRes: Int,
    val isAvailableForProjects: Boolean,
    @param:DrawableRes val imageRes: Int = R.drawable.cat_5968876_960_720
)

// The original profile is first. The other four profiles are fictional exercise data.
// Keep these IDs stable: navigation and LazyColumn both use them to identify a profile.
val sampleProfiles = listOf(
    Profile(
        id = 1,
        name = "Patrik Verho",
        role = "Networking technologies student",
        school = "TAMK",
        major = "Networking technologies",
        interests = "Cybersecurity and IT-architecture",
        descriptionRes = R.string.person_description,
        isAvailableForProjects = true
    ),
    Profile(
        id = 2,
        name = "Aino Laine",
        role = "Software engineering student",
        school = "TAMK",
        major = "Software engineering",
        interests = "Android, Kotlin and accessible interfaces",
        descriptionRes = R.string.aino_description,
        isAvailableForProjects = true
    ),
    Profile(
        id = 3,
        name = "Mikko Niemi",
        role = "Networking technologies student",
        school = "TAMK",
        major = "Networking technologies",
        interests = "Linux, cloud services and IT-architecture",
        descriptionRes = R.string.mikko_description,
        isAvailableForProjects = false
    ),
    Profile(
        id = 4,
        name = "Sara Korhonen",
        role = "Cybersecurity student",
        school = "TAMK",
        major = "Information security",
        interests = "Cybersecurity, secure networks and privacy",
        descriptionRes = R.string.sara_description,
        isAvailableForProjects = true
    ),
    Profile(
        id = 5,
        name = "Elias Virtanen",
        role = "Software engineering student",
        school = "TAMK",
        major = "Software engineering",
        interests = "Android, testing and mobile applications",
        descriptionRes = R.string.elias_description,
        isAvailableForProjects = false
    )
)

fun filterProfiles(
    profiles: List<Profile>,
    searchText: String,
    availableOnly: Boolean
): List<Profile> {
    val query = searchText.trim()
    // Derived data: recompute from the original list and both current filter values.
    return profiles.filter { profile ->
        val matchesAvailability = !availableOnly || profile.isAvailableForProjects
        val matchesSearch = query.isEmpty() ||
            profile.name.contains(query, ignoreCase = true) ||
            profile.role.contains(query, ignoreCase = true) ||
            profile.school.contains(query, ignoreCase = true) ||
            profile.major.contains(query, ignoreCase = true) ||
            profile.interests.contains(query, ignoreCase = true)
        matchesAvailability && matchesSearch
    }
}

fun findProfileById(profiles: List<Profile>, profileId: Int): Profile? =
    profiles.firstOrNull { it.id == profileId }
