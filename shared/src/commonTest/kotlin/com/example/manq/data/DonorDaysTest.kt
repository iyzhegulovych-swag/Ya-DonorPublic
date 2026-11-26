package com.example.manq.data

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DonorDaysTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val now = SimpleDate(2026, 11, 16)

    private val sample = """
        {
          "version": 1,
          "updatedAt": "2026-11-16",
          "source": "Оголошення центрів",
          "days": [
            {
              "id": "lviv-1-2026-12-05",
              "title": "День донора у центрі",
              "date": "2026-12-05",
              "centerId": "lviv-1",
              "timeFrom": "09:00",
              "timeTo": "14:00",
              "details": "Візьміть паспорт.",
              "url": "https://example.org"
            },
            {
              "id": "kyiv-mobile-2026-11-20",
              "title": "Виїзна акція в університеті",
              "date": "2026-11-20",
              "place": "Київ, вул. Володимирська, 60",
              "timeFrom": "10:00"
            },
            {
              "id": "old",
              "title": "Акція, що вже минула",
              "date": "2026-10-01",
              "centerId": "lviv-1"
            }
          ]
        }
    """.trimIndent()

    private val snapshot = json.decodeFromString<DonorDaysSnapshot>(sample)

    @Test
    fun upcomingIsSortedAndSkipsPast() {
        val upcoming = snapshot.upcoming(now)
        assertEquals(
            listOf("kyiv-mobile-2026-11-20", "lviv-1-2026-12-05"),
            upcoming.map { it.id },
            "минулі акції не показуємо, решту — від найближчої",
        )
    }

    @Test
    fun forCenterKeepsOnlyItsOwnUpcomingDays() {
        val days = snapshot.forCenter("lviv-1", now)
        assertEquals(listOf("lviv-1-2026-12-05"), days.map { it.id })
        assertTrue(snapshot.forCenter("kyiv-1", now).isEmpty(), "у цього центру акцій немає")
    }

    @Test
    fun timeIsFormattedForHumans() {
        val full = snapshot.days.first { it.id == "lviv-1-2026-12-05" }
        val openEnded = snapshot.days.first { it.id == "kyiv-mobile-2026-11-20" }
        assertEquals("09:00 – 14:00", full.time)
        assertEquals("з 10:00", openEnded.time)
        assertNull(DonorDay(id = "x", title = "x", date = "2026-12-01").time)
    }

    @Test
    fun todayIsStillUpcoming() {
        val day = DonorDay(id = "x", title = "x", date = now.iso())
        assertTrue(day.isUpcoming(now), "сьогоднішня акція ще не минула")
    }

    @Test
    fun brokenDateDoesNotCrash() {
        val day = DonorDay(id = "x", title = "x", date = "колись")
        assertNull(day.day)
        assertTrue(!day.isUpcoming(now))
    }

    @Test
    fun emptyScheduleIsAValidState() {
        val empty = json.decodeFromString<DonorDaysSnapshot>(
            """{ "version": 1, "updatedAt": "2026-11-16", "days": [] }""",
        )
        assertTrue(empty.upcoming(now).isEmpty())
        assertEquals(now, empty.date)
    }

    @Test
    fun worldDonorDayIsFourteenthOfJune() {
        val event = AnnualDonorEvents.worldDonorDay
        assertEquals(6, event.month)
        assertEquals(14, event.day)
    }

    @Test
    fun annualEventJumpsToNextYearWhenPassed() {
        val event = AnnualDonorEvents.worldDonorDay
        assertEquals(SimpleDate(2027, 6, 14), event.next(SimpleDate(2026, 11, 16)))
        assertEquals(SimpleDate(2026, 6, 14), event.next(SimpleDate(2026, 1, 1)))
        assertEquals(
            SimpleDate(2026, 6, 14),
            event.next(SimpleDate(2026, 6, 14)),
            "у сам день свята нагадування ще актуальне",
        )
    }
}
