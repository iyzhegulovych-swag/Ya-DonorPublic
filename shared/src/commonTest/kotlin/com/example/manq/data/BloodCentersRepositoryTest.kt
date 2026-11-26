package com.example.manq.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BloodCentersRepositoryTest {

    private val repo = BloodCentersRepository()

    @Test
    fun directoryIsComplete() {
        assertEquals(24, repo.oblastNames().size, "областей")
        assertEquals(116, repo.allCenters.size, "центрів")
    }

    /**
     * Обласні центри з сайту УЦТК. Якщо такого id не стане, зіставлення
     * в utcc_mapping.json зламається.
     */
    @Test
    fun oblastCentersFromUtccExist() {
        val needed = listOf(
            "cherkasy",
            "kherson",
            "chernihiv",
            "chernivtsi",
            "kramatorsk",
            "sieverodonetsk",
        )
        needed.forEach { id ->
            assertTrue(repo.centerById(id) != null, "немає центру $id")
        }
    }

    @Test
    fun centerIdsAreUnique() {
        val ids = repo.allCenters.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "id центрів мають бути унікальні")
        assertTrue(ids.none { it.isBlank() }, "порожніх id бути не може")
    }

    @Test
    fun everyCenterKnowsItsLocation() {
        repo.allCenters.forEach { center ->
            val place = repo.locationOf(center)
            assertTrue(place != null, "центр ${center.id} без міста")
        }
    }

    @Test
    fun searchByCityFindsCenters() {
        val lviv = repo.search(city = "Львів")
        assertTrue(lviv.size >= 2, "у Львові очікуємо кілька центрів, знайдено ${lviv.size}")
        assertTrue(lviv.all { repo.locationOf(it)?.city == "Львів" })
    }

    @Test
    fun searchIgnoresCase() {
        assertEquals(repo.search(city = "Львів").size, repo.search(city = "львів").size)
    }

    @Test
    fun componentFilterNarrowsResults() {
        val all = repo.search(city = "Львів")
        val plasma = repo.search(city = "Львів", components = setOf(BloodComponent.PLASMA))
        assertTrue(plasma.size <= all.size)
        assertTrue(plasma.all { it.acceptsPlasma })
    }

    @Test
    fun textQueryMatchesNameAndAddress() {
        assertTrue(repo.search(query = "Пирогова").isNotEmpty(), "пошук по вулиці")
        assertTrue(repo.search(query = "обласний центр").isNotEmpty(), "пошук по назві")
    }

    @Test
    fun favoritesFilterKeepsOnlyChosen() {
        val chosen = repo.allCenters.take(3).map { it.id }.toSet()
        assertEquals(chosen, repo.search(onlyIds = chosen).map { it.id }.toSet())
    }

    @Test
    fun centerByIdRoundTrips() {
        val any = repo.allCenters.first()
        assertEquals(any, repo.centerById(any.id))
    }
}
