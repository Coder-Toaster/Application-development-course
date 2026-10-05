package com.example.opintotehtv3

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileRoutesTest {
    @Test
    fun detailRouteSerializationPreservesTheSelectedId() {
        val route = ProfileDetailRoute(profileId = 3)
        val savedRoute = Json.encodeToString(route)
        assertEquals(route, Json.decodeFromString<ProfileDetailRoute>(savedRoute))
    }

    @Test
    fun listRouteHasAGeneratedSerializer() {
        val savedRoute = Json.encodeToString(ProfileListRoute)
        assertEquals(ProfileListRoute, Json.decodeFromString<ProfileListRoute>(savedRoute))
    }
}
