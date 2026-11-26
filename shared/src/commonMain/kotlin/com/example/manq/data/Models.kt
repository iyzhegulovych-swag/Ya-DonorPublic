package com.example.manq.data

import kotlinx.serialization.Serializable

/**
 * Центр крові. [id] не можна міняти після релізу: на нього зав'язані
 * обране, візити й потреба у крові. У [email], [facebook], [website]
 * може стояти "-", якщо дані невідомі.
 */
data class Center(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val facebook: String,
    val website: String,
    val address: String,
    val acceptsBlood: Boolean,
    val acceptsPlatelets: Boolean,
    val acceptsPlasma: Boolean,
) {
    /** Чи є в полі реальне значення, а не прочерк. */
    fun has(value: String): Boolean = value.isNotBlank() && value != "-"

    val facebookUrl: String?
        get() = if (!has(facebook)) {
            null
        } else if (facebook.startsWith("http")) {
            facebook
        } else {
            "https://facebook.com/${facebook.removePrefix("@")}"
        }

    val websiteUrl: String?
        get() = if (!has(website)) {
            null
        } else if (website.startsWith("http")) {
            website
        } else {
            "https://$website"
        }
}

data class City(
    val name: String,
    val centers: List<Center>,
)

data class Oblast(
    val name: String,
    val cities: List<City>,
)

/** Компонент крові, який приймає центр. Використовується у фільтрі. */
@Serializable
enum class BloodComponent(val title: String) {
    WHOLE_BLOOD("Цільна кров"),
    PLATELETS("Тромбоцити"),
    PLASMA("Плазма"),
    ;

    fun acceptedBy(center: Center): Boolean = when (this) {
        WHOLE_BLOOD -> center.acceptsBlood
        PLATELETS -> center.acceptsPlatelets
        PLASMA -> center.acceptsPlasma
    }
}
