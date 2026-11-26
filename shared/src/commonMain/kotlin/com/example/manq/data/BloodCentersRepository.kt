package com.example.manq.data

/** Пошук і фільтри по довіднику центрів крові. */
class BloodCentersRepository(
    private val source: List<Oblast> = BloodCentersDb.oblasts,
) {

    /** Усі центри одним списком. */
    val allCenters: List<Center> by lazy {
        source.flatMap { oblast -> oblast.cities.flatMap { it.centers } }
    }

    /** Центр -> місто й область, у яких він розташований. */
    private val locations: Map<String, Location> by lazy {
        buildMap {
            source.forEach { oblast ->
                oblast.cities.forEach { city ->
                    city.centers.forEach { center ->
                        put(center.id, Location(oblast.name, city.name))
                    }
                }
            }
        }
    }

    data class Location(val oblast: String, val city: String)

    fun oblastNames(): List<String> = source.map { it.name }

    fun cityNames(oblastName: String? = null): List<String> {
        val oblasts = if (oblastName == null) {
            source
        } else {
            source.filter { it.name.equals(oblastName, ignoreCase = true) }
        }
        return oblasts.flatMap { it.cities }.map { it.name }.distinct().sorted()
    }

    fun centerById(id: String): Center? = allCenters.firstOrNull { it.id == id }

    fun locationOf(center: Center): Location? = locations[center.id]

    /**
     * Кожен фільтр необов'язковий: порожній [components] означає «будь-який
     * компонент», [query] шукає по назві, адресі й місту без урахування регістру.
     */
    fun search(
        query: String = "",
        oblast: String? = null,
        city: String? = null,
        components: Set<BloodComponent> = emptySet(),
        onlyIds: Set<String>? = null,
    ): List<Center> {
        val text = query.trim()
        return allCenters.filter { center ->
            val place = locations[center.id]
            val matchesOblast = oblast == null || place?.oblast.equals(oblast, ignoreCase = true)
            val matchesCity = city == null || place?.city.equals(city, ignoreCase = true)
            val matchesComponents = components.all { it.acceptedBy(center) }
            val matchesFavorites = onlyIds == null || center.id in onlyIds
            val matchesText = text.isEmpty() ||
                center.name.contains(text, ignoreCase = true) ||
                center.address.contains(text, ignoreCase = true) ||
                place?.city?.contains(text, ignoreCase = true) == true

            matchesOblast && matchesCity && matchesComponents && matchesFavorites && matchesText
        }
    }
}
