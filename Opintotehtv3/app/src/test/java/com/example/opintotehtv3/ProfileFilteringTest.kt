package com.example.opintotehtv3

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileFilteringTest {
    @Test
    fun emptySearchShowsAllProfiles() {
        assertEquals(sampleProfiles, filterProfiles(sampleProfiles, "", availableOnly = false))
    }

    @Test
    fun searchIgnoresCaseAndSurroundingWhitespace() {
        val results = filterProfiles(sampleProfiles, "  pAtRiK  ", availableOnly = false)
        assertEquals(listOf(1), results.map { it.id })
    }

    @Test
    fun searchAlsoMatchesInterests() {
        val results = filterProfiles(sampleProfiles, "ANDROID", availableOnly = false)
        assertEquals(listOf(2, 5), results.map { it.id })
    }

    @Test
    fun textAndAvailabilityFiltersMustBothMatch() {
        val results = filterProfiles(sampleProfiles, "android", availableOnly = true)
        assertEquals(listOf(2), results.map { it.id })
        assertTrue(filterProfiles(sampleProfiles, "mikko", availableOnly = true).isEmpty())
    }

    @Test
    fun blankSearchStillAppliesAvailabilityFilter() {
        val results = filterProfiles(sampleProfiles, "   ", availableOnly = true)
        assertEquals(listOf(1, 2, 4), results.map { it.id })
    }

    @Test
    fun unmatchedSearchIsEmptyAndOriginalDataIsUnchanged() {
        assertTrue(filterProfiles(sampleProfiles, "no-such-person", availableOnly = false).isEmpty())
        assertEquals(5, sampleProfiles.size)
        assertFalse(sampleProfiles[2].isAvailableForProjects)
    }

    @Test
    fun profileIdsAreUniqueAndLookupHandlesMissingId() {
        assertEquals(sampleProfiles.size, sampleProfiles.map { it.id }.toSet().size)
        assertEquals("Mikko Niemi", findProfileById(sampleProfiles, 3)?.name)
        assertNull(findProfileById(sampleProfiles, -1))
    }
}
