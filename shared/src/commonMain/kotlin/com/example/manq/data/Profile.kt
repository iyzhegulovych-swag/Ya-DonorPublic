package com.example.manq.data

import kotlinx.serialization.Serializable

/** Група крові за системою AB0. */
@Serializable
enum class BloodGroup(val title: String) {
    UNKNOWN("Не знаю"),
    O("0 (I)"),
    A("A (II)"),
    B("B (III)"),
    AB("AB (IV)"),
}

@Serializable
enum class RhFactor(val title: String) {
    UNKNOWN("Не знаю"),
    POSITIVE("Rh (+)"),
    NEGATIVE("Rh (−)"),
}

/** Профіль донора. Зберігається лише на цьому пристрої, заповнювати не обов'язково. */
@Serializable
data class UserProfile(
    val name: String = "",
    val surname: String = "",
    val patronymic: String = "",
    val email: String = "",
    val bloodGroup: BloodGroup = BloodGroup.UNKNOWN,
    val rhFactor: RhFactor = RhFactor.UNKNOWN,
    val birthDate: String = "",
) {
    val isEmpty: Boolean
        get() = name.isBlank() && surname.isBlank() && patronymic.isBlank() &&
            email.isBlank() &&
            bloodGroup == BloodGroup.UNKNOWN && rhFactor == RhFactor.UNKNOWN

    val initial: String
        get() = name.ifBlank { surname }.trim().firstOrNull()?.uppercase() ?: "?"

    /** ПІБ у порядку «Шевченко Тарас Григорович», або null. */
    val fullName: String?
        get() = listOf(surname, name, patronymic)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { null }

    /** Коротке звертання для екрана профілю: «Тарас Шевченко». */
    val displayName: String?
        get() = listOf(name, surname)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { null }

    /** Повна група у вигляді «A (II) Rh (+)», якщо обидві частини відомі. */
    val fullBloodType: String?
        get() = if (bloodGroup == BloodGroup.UNKNOWN || rhFactor == RhFactor.UNKNOWN) {
            null
        } else {
            "${bloodGroup.title} ${rhFactor.title}"
        }

    fun ageOn(date: SimpleDate): Int? {
        val born = SimpleDate.parse(birthDate) ?: return null
        var age = date.year - born.year
        if (date.month < born.month || (date.month == born.month && date.day < born.day)) age--
        return age.takeIf { it in 0..130 }
    }
}

/**
 * Запис про донацію: запланований візит або вже здана кров.
 * Додаток нічого не бронює, [completed] людина відмічає сама.
 */
@Serializable
data class Donation(
    val id: String,
    val centerId: String,
    val date: String,
    val component: BloodComponent = BloodComponent.WHOLE_BLOOD,
    val completed: Boolean = false,
) {
    val day: SimpleDate? get() = SimpleDate.parse(date)
}

/**
 * Мінімальні інтервали між донаціями в днях за даними Українського
 * центру трансплант-координації. Для плазми й тромбоцитів інтервал
 * залежить від процедури, тому показується як орієнтовний.
 */
object DonationIntervals {
    fun days(component: BloodComponent): Int = when (component) {
        BloodComponent.WHOLE_BLOOD -> 60
        BloodComponent.PLASMA -> 14
        BloodComponent.PLATELETS -> 14
    }

    fun isApproximate(component: BloodComponent): Boolean = component != BloodComponent.WHOLE_BLOOD

    /** Коли можна знову здавати цей компонент після донації [from]. */
    fun nextAllowed(from: SimpleDate, component: BloodComponent): SimpleDate = from.plusDays(days(component))
}
