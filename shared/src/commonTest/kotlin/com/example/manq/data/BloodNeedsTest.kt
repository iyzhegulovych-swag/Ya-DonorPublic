package com.example.manq.data

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BloodNeedsTest {

    private val json = Json { ignoreUnknownKeys = true }

    private val sample = """
        {
          "version": 1,
          "generatedAt": "2026-11-12",
          "source": "https://donor.utcc.gov.ua/service_center",
          "centers": [
            {
              "centerId": "kyiv-1",
              "needs": {
                "O_POS": "HIGH", "O_NEG": "HIGH", "A_POS": "HIGH", "A_NEG": "HIGH",
                "B_POS": "HIGH", "B_NEG": "HIGH", "AB_POS": "HIGH", "AB_NEG": "HIGH"
              }
            },
            {
              "centerId": "dnipro",
              "needs": {
                "O_POS": "MEDIUM", "O_NEG": "LOW", "A_POS": "LOW", "A_NEG": "LOW",
                "B_POS": "LOW", "B_NEG": "LOW", "AB_POS": "LOW", "AB_NEG": "LOW"
              }
            }
          ]
        }
    """.trimIndent()

    private val snapshot: NeedsSnapshot get() = json.decodeFromString(sample)

    @Test
    fun parsesSnapshot() {
        val s = snapshot
        assertEquals(1, s.version)
        assertEquals(2, s.centers.size)
        assertEquals(SimpleDate(2026, 11, 12), s.date)
    }

    @Test
    fun readsLevelsForCenter() {
        val kyiv = snapshot.forCenter("kyiv-1")
        assertEquals(NeedLevel.HIGH, kyiv?.needs?.get(BloodType.O_POS))
        assertEquals(8, kyiv?.needs?.size)
    }

    @Test
    fun unknownCenterIsNull() {
        assertNull(snapshot.forCenter("не-існує"))
        assertTrue(!snapshot.hasHighNeed("не-існує"))
    }

    @Test
    fun highNeedIsDetected() {
        val s = snapshot
        assertTrue(s.hasHighNeed("kyiv-1"))
        assertTrue(!s.hasHighNeed("dnipro"), "у Дніпрі високої потреби немає")
    }

    @Test
    fun findsCentersNeedingType() {
        assertEquals(listOf("kyiv-1"), snapshot.centersNeeding(BloodType.A_POS))
        assertEquals(emptyList(), snapshot.centersNeeding(BloodType.AB_NEG).filter { it == "dnipro" })
    }

    @Test
    fun bloodTypeIsBuiltFromProfile() {
        assertEquals(BloodType.A_POS, BloodType.of(BloodGroup.A, RhFactor.POSITIVE))
        assertEquals(BloodType.AB_NEG, BloodType.of(BloodGroup.AB, RhFactor.NEGATIVE))
        assertNull(BloodType.of(BloodGroup.UNKNOWN, RhFactor.POSITIVE))
        assertNull(BloodType.of(BloodGroup.A, RhFactor.UNKNOWN))
    }

    @Test
    fun dataGoesStaleAfterTwoWeeks() {
        val s = snapshot
        assertTrue(!s.isStale(SimpleDate(2026, 11, 20)), "8 днів — ще свіжі")
        assertTrue(s.isStale(SimpleDate(2026, 12, 1)), "19 днів — вже застарілі")
    }

    @Test
    fun brokenJsonDoesNotCrash() {
        val result = runCatching { json.decodeFromString<NeedsSnapshot>("{ не json }") }
        assertTrue(result.isFailure, "зламаний файл має давати помилку, а не тихо порожній зріз")
    }

    @Test
    fun emptySnapshotIsSafe() {
        val empty = NeedsSnapshot()
        assertNull(empty.forCenter("kyiv-1"))
        assertTrue(empty.isStale(), "без дати вважаємо застарілими")
    }
}
