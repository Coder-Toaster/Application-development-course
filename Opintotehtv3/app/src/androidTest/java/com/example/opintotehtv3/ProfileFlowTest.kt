package com.example.opintotehtv3

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFlowTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchAndBooleanFilterWorkTogether() {
        searchFor("aNdRoId")
        compose.onNodeWithText("Aino Laine").assertIsDisplayed()
        compose.onNodeWithText("Patrik Verho").assertDoesNotExist()
        compose.onNodeWithTag("available_filter").performClick()
        compose.onNodeWithText("Aino Laine").assertIsDisplayed()
        compose.onNodeWithText("Elias Virtanen").assertDoesNotExist()
        compose.onNodeWithText(compose.activity.getString(R.string.profiles_count, 1, 5))
            .assertIsDisplayed()
    }

    @Test
    fun unmatchedSearchShowsEmptyMessage() {
        searchFor("zzzz")
        compose.onNodeWithText(compose.activity.getString(R.string.no_matching_profiles))
            .assertIsDisplayed()
    }

    @Test
    fun toolbarAndSystemBackRestoreSearchAndFilter() {
        searchFor("android")
        compose.onNodeWithTag("available_filter").performClick()
        compose.onNodeWithText("Aino Laine").performClick()
        compose.onNodeWithText(compose.activity.getString(R.string.school_variable))
            .assertIsDisplayed()
        compose.onNodeWithContentDescription(compose.activity.getString(R.string.navigate_back))
            .performClick()
        compose.onNodeWithTag("profile_search").assertTextContains("android")
        compose.onNodeWithTag("available_filter").assertIsOn()
        compose.onNodeWithText("Aino Laine").performClick()
        compose.waitForIdle()
        Espresso.pressBack()
        compose.onNodeWithTag("profile_search").assertTextContains("android")
        compose.onNodeWithTag("available_filter").assertIsOn()
    }

    @Test
    fun activityRecreationRestoresSearchAndFilter() {
        searchFor("android")
        compose.onNodeWithTag("available_filter").performClick()
        compose.waitForIdle()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithTag("profile_search").assertTextContains("android")
        compose.onNodeWithTag("available_filter").assertIsOn()
        compose.onNodeWithText(compose.activity.getString(R.string.profiles_count, 1, 5))
            .assertIsDisplayed()
    }

    private fun searchFor(text: String) {
        compose.onNodeWithTag("profile_search").performTextInput(text)
        compose.onNodeWithTag("profile_search").performImeAction()
    }
}
