package com.example.opintotehtv3

import kotlinx.serialization.Serializable

@Serializable
data object ProfileListRoute

@Serializable
data class ProfileDetailRoute(val profileId: Int)
