package com.example.manq.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SimpleDateTest {

    @Test
    fun epochDayMatchesKnownValues() {
        assertEquals(0L, SimpleDate(1970, 1, 1).toEpochDay())
        assertEquals(1L, SimpleDate(1970, 1, 2).toEpochDay())
        assertEquals(-1L, SimpleDate(1969, 12, 31).toEpochDay())
        assertEquals(19723L, SimpleDate(2024, 1, 1).toEpochDay())
    }

    @Test
    fun epochDayRoundTripsOverDecades() {
        var day = SimpleDate(2000, 1, 1).toEpochDay()
        val end = SimpleDate(2035, 12, 31).toEpochDay()
        while (day <= end) {
            val date = SimpleDate.fromEpochDay(day)
            assertEquals(day, date.toEpochDay(), "не збігається на $date")
            assertTrue(date.isValid(), "невалідна дата $date")
            day++
        }
    }

    @Test
    fun plusDaysCrossesMonthAndYear() {
        assertEquals(SimpleDate(2026, 12, 1), SimpleDate(2026, 11, 30).plusDays(1))
        assertEquals(SimpleDate(2027, 1, 1), SimpleDate(2026, 12, 31).plusDays(1))
        assertEquals(SimpleDate(2027, 1, 11), SimpleDate(2026, 11, 12).plusDays(60))
    }

    @Test
    fun leapYearIsHandled() {
        assertEquals(SimpleDate(2024, 2, 29), SimpleDate(2024, 2, 28).plusDays(1))
        assertEquals(SimpleDate(2023, 3, 1), SimpleDate(2023, 2, 28).plusDays(1))
        assertEquals(29, daysInMonth(2024, 2))
        assertEquals(28, daysInMonth(1900, 2), "1900 не високосний")
        assertEquals(29, daysInMonth(2000, 2), "2000 високосний")
    }

    @Test
    fun daysUntilIsSymmetric() {
        val a = SimpleDate(2026, 11, 12)
        val b = a.plusDays(60)
        assertEquals(60, a.daysUntil(b))
        assertEquals(-60, b.daysUntil(a))
    }

    @Test
    fun parsesBothFormats() {
        assertEquals(SimpleDate(2026, 11, 12), SimpleDate.parse("2026-11-12"))
        assertEquals(SimpleDate(2026, 11, 12), SimpleDate.parse("12.11.2026"))
        assertEquals(SimpleDate(2008, 1, 5), SimpleDate.parse("5.1.2008"))
    }

    @Test
    fun rejectsNonsense() {
        assertNull(SimpleDate.parse("31.02.2026"), "31 лютого не існує")
        assertNull(SimpleDate.parse("2026-13-01"))
        assertNull(SimpleDate.parse("завтра"))
        assertNull(SimpleDate.parse(""))
    }

    @Test
    fun formatsForUkraine() {
        assertEquals("05.01.2026", SimpleDate(2026, 1, 5).formatted())
        assertEquals("2026-01-05", SimpleDate(2026, 1, 5).iso())
        assertEquals("листопада", SimpleDate(2026, 11, 12).monthName())
    }

    @Test
    fun comparesChronologically() {
        assertTrue(SimpleDate(2026, 1, 1) < SimpleDate(2026, 1, 2))
        assertTrue(SimpleDate(2027, 1, 1) > SimpleDate(2026, 12, 31))
    }

    @Test
    fun wholeBloodIntervalIsSixtyDays() {
        val donated = SimpleDate(2026, 11, 12)
        assertEquals(
            donated.plusDays(60),
            DonationIntervals.nextAllowed(donated, BloodComponent.WHOLE_BLOOD),
        )
        assertTrue(DonationIntervals.isApproximate(BloodComponent.PLASMA))
        assertTrue(!DonationIntervals.isApproximate(BloodComponent.WHOLE_BLOOD))
    }

    @Test
    fun ageIsCountedOnBirthday() {
        val profile = UserProfile(birthDate = "10.11.2008")
        assertEquals(18, profile.ageOn(SimpleDate(2026, 11, 10)), "у день народження")
        assertEquals(17, profile.ageOn(SimpleDate(2026, 11, 9)), "напередодні")
        assertEquals(18, profile.ageOn(SimpleDate(2026, 11, 12)))
    }
}
