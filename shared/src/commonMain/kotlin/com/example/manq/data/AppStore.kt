package com.example.manq.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.manq.storage.Storage
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

/** Стан, який переживає перезапуск: профіль, обране, донації. */
class AppStore(private val storage: Storage) {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    var profile: UserProfile by mutableStateOf(read(KEY_PROFILE, UserProfile()))
        private set

    var favorites: Set<String> by mutableStateOf(
        readOrNull(KEY_FAVORITES, SetSerializer(String.serializer())) ?: emptySet(),
    )
        private set

    var donations: List<Donation> by mutableStateOf(
        readOrNull(KEY_DONATIONS, ListSerializer(Donation.serializer())) ?: emptyList(),
    )
        private set

    // --- профіль ---

    fun updateProfile(updated: UserProfile) {
        profile = updated
        storage.putString(KEY_PROFILE, json.encodeToString(UserProfile.serializer(), updated))
    }

    /** Повне очищення. Акаунта немає, тому «вийти» нема звідки — є що стерти. */
    fun clearEverything() {
        profile = UserProfile()
        favorites = emptySet()
        donations = emptyList()
        listOf(KEY_PROFILE, KEY_FAVORITES, KEY_DONATIONS).forEach(storage::remove)
    }

    // --- обране ---

    fun isFavorite(centerId: String): Boolean = centerId in favorites

    fun toggleFavorite(centerId: String) {
        favorites = if (centerId in favorites) favorites - centerId else favorites + centerId
        storage.putString(
            KEY_FAVORITES,
            json.encodeToString(SetSerializer(String.serializer()), favorites),
        )
    }

    // --- донації ---

    fun addDonation(donation: Donation) {
        donations = (donations + donation).sortedBy { it.date }
        persistDonations()
    }

    fun removeDonation(id: String) {
        donations = donations.filterNot { it.id == id }
        persistDonations()
    }

    /**
     * Позначити донацію завершеною. Візиту з майбутньою датою ставимо
     * сьогоднішню, інакше інтервал до наступної донації рахувався б
     * не з того дня.
     */
    fun markCompleted(id: String, completed: Boolean = true, now: SimpleDate = today()) {
        donations = donations.map { donation ->
            if (donation.id != id) return@map donation
            val actualDate = when {
                !completed -> donation.date
                (donation.day ?: now) > now -> now.iso()
                else -> donation.date
            }
            donation.copy(completed = completed, date = actualDate)
        }
        persistDonations()
    }

    private fun persistDonations() {
        storage.putString(
            KEY_DONATIONS,
            json.encodeToString(ListSerializer(Donation.serializer()), donations),
        )
    }

    // --- похідні дані для екранів ---

    val completedDonations: List<Donation>
        get() = donations.filter { it.completed }.sortedByDescending { it.date }

    val plannedVisits: List<Donation>
        get() = donations.filterNot { it.completed }.sortedBy { it.date }

    fun nextPlanned(from: SimpleDate = today()): Donation? = plannedVisits.firstOrNull { (it.day ?: return@firstOrNull false) >= from }

    /** Коли можна знову здавати компонент; null — таких донацій ще не було. */
    fun nextAllowedDate(component: BloodComponent): SimpleDate? {
        val last = completedDonations
            .filter { it.component == component }
            .mapNotNull { it.day }
            .maxOrNull() ?: return null
        return DonationIntervals.nextAllowed(last, component)
    }

    private inline fun <reified T> read(key: String, fallback: T): T = runCatching {
        storage.getString(key)?.let { json.decodeFromString<T>(it) } ?: fallback
    }.getOrDefault(fallback)

    private fun <T> readOrNull(
        key: String,
        serializer: kotlinx.serialization.KSerializer<T>,
    ): T? = runCatching {
        storage.getString(key)?.let { json.decodeFromString(serializer, it) }
    }.getOrNull()

    private companion object {
        const val KEY_PROFILE = "profile"
        const val KEY_FAVORITES = "favorites"
        const val KEY_DONATIONS = "donations"
    }
}
