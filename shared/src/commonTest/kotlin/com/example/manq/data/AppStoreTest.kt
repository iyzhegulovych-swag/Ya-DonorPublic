package com.example.manq.data

import com.example.manq.storage.InMemoryStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private fun inMemoryStore() = InMemoryStorage()

class AppStoreTest {

    private val today = SimpleDate(2026, 11, 13)

    private fun store() = AppStore(inMemoryStore())

    @Test
    fun favoritesToggle() {
        val s = store()
        assertTrue(!s.isFavorite("lviv-1"))
        s.toggleFavorite("lviv-1")
        assertTrue(s.isFavorite("lviv-1"))
        s.toggleFavorite("lviv-1")
        assertTrue(!s.isFavorite("lviv-1"))
    }

    @Test
    fun favoritesSurviveReload() {
        val storage = inMemoryStore()
        AppStore(storage).toggleFavorite("kyiv-1")
        assertTrue(AppStore(storage).isFavorite("kyiv-1"), "обране має читатись назад")
    }

    @Test
    fun profileSurvivesReload() {
        val storage = inMemoryStore()
        AppStore(storage).updateProfile(
            UserProfile(name = "Олена", bloodGroup = BloodGroup.A, rhFactor = RhFactor.POSITIVE),
        )
        val reloaded = AppStore(storage).profile
        assertEquals("Олена", reloaded.name)
        assertEquals(BloodGroup.A, reloaded.bloodGroup)
    }

    @Test
    fun completingFuturePlanRecordsToday() {
        val s = store()
        val future = today.plusDays(7)
        s.addDonation(Donation(id = "1", centerId = "vinnytsia", date = future.iso()))

        s.markCompleted("1", now = today)

        val done = s.completedDonations.single()
        assertEquals(today.iso(), done.date, "у історію має потрапити сьогоднішня дата")
    }

    @Test
    fun completingPastPlanKeepsItsDate() {
        val s = store()
        val past = today.plusDays(-10)
        s.addDonation(Donation(id = "1", centerId = "vinnytsia", date = past.iso()))

        s.markCompleted("1", now = today)

        assertEquals(past.iso(), s.completedDonations.single().date)
    }

    @Test
    fun nextAllowedCountsFromLastDonation() {
        val s = store()
        s.addDonation(
            Donation(id = "1", centerId = "lviv-1", date = today.iso(), completed = true),
        )
        assertEquals(today.plusDays(60), s.nextAllowedDate(BloodComponent.WHOLE_BLOOD))
        assertNull(s.nextAllowedDate(BloodComponent.PLASMA), "плазму ще не здавали")
    }

    @Test
    fun nextAllowedUsesMostRecentDonation() {
        val s = store()
        s.addDonation(Donation("1", "lviv-1", today.plusDays(-90).iso(), completed = true))
        s.addDonation(Donation("2", "lviv-1", today.plusDays(-10).iso(), completed = true))
        assertEquals(
            today.plusDays(-10).plusDays(60),
            s.nextAllowedDate(BloodComponent.WHOLE_BLOOD),
            "рахуємо від останньої, а не від першої донації",
        )
    }

    @Test
    fun plannedAndCompletedAreSeparate() {
        val s = store()
        s.addDonation(Donation("1", "lviv-1", today.plusDays(5).iso()))
        s.addDonation(Donation("2", "lviv-1", today.plusDays(-5).iso(), completed = true))
        assertEquals(1, s.plannedVisits.size)
        assertEquals(1, s.completedDonations.size)
    }

    @Test
    fun clearEverythingWipesAllData() {
        val storage = inMemoryStore()
        val s = AppStore(storage)
        s.updateProfile(UserProfile(name = "Олена"))
        s.toggleFavorite("lviv-1")
        s.addDonation(Donation("1", "lviv-1", today.iso()))

        s.clearEverything()

        assertTrue(s.profile.isEmpty)
        assertTrue(s.favorites.isEmpty())
        assertTrue(s.donations.isEmpty())
        assertTrue(AppStore(storage).profile.isEmpty, "після перезапуску теж порожньо")
    }

    @Test
    fun brokenStoredDataFallsBackToEmpty() {
        val storage = inMemoryStore()
        storage.putString("profile", "{ зламаний json")
        storage.putString("favorites", "не масив")
        val s = AppStore(storage)
        assertTrue(s.profile.isEmpty, "зіпсовані дані не повинні ламати запуск")
        assertTrue(s.favorites.isEmpty())
    }
}
