package com.example.manq.data

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserProfileTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun fullNameUsesDocumentOrder() {
        val profile = UserProfile(name = "Тарас", surname = "Шевченко", patronymic = "Григорович")
        assertEquals("Шевченко Тарас Григорович", profile.fullName)
        assertEquals("Тарас Шевченко", profile.displayName)
    }

    @Test
    fun partiallyFilledNameSkipsBlanks() {
        assertEquals("Тарас", UserProfile(name = "Тарас").fullName)
        assertEquals("Шевченко Тарас", UserProfile(name = "Тарас", surname = "Шевченко").fullName)
        assertNull(UserProfile().fullName)
        assertNull(UserProfile().displayName)
    }

    @Test
    fun initialFallsBackToSurname() {
        assertEquals("Т", UserProfile(name = "Тарас", surname = "Шевченко").initial)
        assertEquals("Ш", UserProfile(surname = "Шевченко").initial)
        assertEquals("?", UserProfile().initial)
    }

    @Test
    fun profileWithOnlySurnameIsNotEmpty() {
        assertTrue(UserProfile().isEmpty)
        assertTrue(!UserProfile(surname = "Шевченко").isEmpty)
    }

    /** Профілі, збережені до появи ПІБ, мають читатись далі. */
    @Test
    fun oldStoredProfileStillReads() {
        val stored = """{"name":"Олена","email":"o@example.org","bloodGroup":"A","rhFactor":"POSITIVE","birthDate":"2008-01-01"}"""
        val profile = json.decodeFromString<UserProfile>(stored)
        assertEquals("Олена", profile.name)
        assertEquals("", profile.surname)
        assertEquals("Олена", profile.fullName)
    }

    @Test
    fun fullNameSurvivesReload() {
        val storage = com.example.manq.storage.InMemoryStorage()
        AppStore(storage).updateProfile(
            UserProfile(name = "Тарас", surname = "Шевченко", patronymic = "Григорович"),
        )
        assertEquals("Шевченко Тарас Григорович", AppStore(storage).profile.fullName)
    }
}
