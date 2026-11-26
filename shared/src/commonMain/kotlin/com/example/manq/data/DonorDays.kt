package com.example.manq.data

import kotlinx.serialization.Serializable

/**
 * День донора: окремий день прийому в центрі або виїзна акція.
 *
 * [centerId] заповнюють для центру з довідника — тоді день донора видно
 * і на його картці. Для виїзної акції адресу беруть з [place].
 */
@Serializable
data class DonorDay(
    val id: String,
    val title: String,
    val date: String,
    val centerId: String = "",
    val place: String = "",
    val timeFrom: String = "",
    val timeTo: String = "",
    val details: String = "",
    val url: String = "",
) {
    val day: SimpleDate? get() = SimpleDate.parse(date)

    /** «09:00 – 14:00», «з 09:00» або null, якщо час не вказано. */
    val time: String?
        get() = when {
            timeFrom.isBlank() -> null
            timeTo.isBlank() -> "з $timeFrom"
            else -> "$timeFrom – $timeTo"
        }

    /** Минулі дні донора нікому не потрібні — показуємо лише майбутні. */
    fun isUpcoming(now: SimpleDate): Boolean = (day ?: return false) >= now
}

/** Зріз розкладу днів донора з датою останньої правки. */
@Serializable
data class DonorDaysSnapshot(
    val version: Int = 1,
    val updatedAt: String = "",
    val source: String = "",
    val days: List<DonorDay> = emptyList(),
) {
    val date: SimpleDate? get() = SimpleDate.parse(updatedAt)

    fun upcoming(now: SimpleDate = today()): List<DonorDay> = days.filter { it.isUpcoming(now) }.sortedBy { it.day?.toEpochDay() }

    /** Майбутні дні донора конкретного центру. */
    fun forCenter(centerId: String, now: SimpleDate = today()): List<DonorDay> = upcoming(now).filter { it.centerId == centerId }
}

/** Дата, яка повторюється щороку. */
@Serializable
data class AnnualEvent(
    val id: String,
    val title: String,
    val month: Int,
    val day: Int,
    val details: String = "",
    val reminder: String = "",
) {
    /** Найближче повторення: цьогорічне або наступного року. */
    fun next(now: SimpleDate = today()): SimpleDate {
        val thisYear = SimpleDate(now.year, month, day)
        return if (thisYear >= now) thisYear else SimpleDate(now.year + 1, month, day)
    }
}

/** Постійні дати донорського календаря. */
object AnnualDonorEvents {

    val worldDonorDay = AnnualEvent(
        id = "world-donor-day",
        title = "Всесвітній день донора крові",
        month = 6,
        day = 14,
        details = "Відзначається з 2004 року у день народження Карла Ландштайнера, " +
            "який відкрив групи крові. Гарна нагода здати кров уперше або " +
            "привести з собою когось, хто давно збирався.",
        reminder = "Сьогодні Всесвітній день донора крові. Гарний день, щоб здати кров.",
    )

    val all = listOf(worldDonorDay)
}
