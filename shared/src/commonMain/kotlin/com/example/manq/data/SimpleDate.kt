package com.example.manq.data

import kotlinx.serialization.Serializable

/**
 * Дата без часу й часових поясів. Перетворення йде через кількість днів
 * від 1970-01-01 за алгоритмом Говарда Хіннанта.
 */
@Serializable
data class SimpleDate(val year: Int, val month: Int, val day: Int) : Comparable<SimpleDate> {

    fun toEpochDay(): Long {
        val y = if (month <= 2) year - 1 else year
        val era = (if (y >= 0) y else y - 399) / 400
        val yoe = y - era * 400 // рік усередині ери [0, 399]
        val mp = (month + 9) % 12 // березень = 0
        val doy = (153 * mp + 2) / 5 + day - 1 // день усередині року
        val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy // день усередині ери
        return era * 146097L + doe - 719468L
    }

    fun plusDays(days: Int): SimpleDate = fromEpochDay(toEpochDay() + days)

    fun daysUntil(other: SimpleDate): Int = (other.toEpochDay() - toEpochDay()).toInt()

    /** Формат, звичний в Україні: 12.11.2026 */
    fun formatted(): String = "${pad(day)}.${pad(month)}.$year"

    /** Формат для зберігання: 2026-11-12 */
    fun iso(): String = "$year-${pad(month)}-${pad(day)}"

    fun monthName(): String = MONTHS.getOrElse(month - 1) { "" }

    override fun compareTo(other: SimpleDate): Int = toEpochDay().compareTo(other.toEpochDay())

    private fun pad(v: Int) = if (v < 10) "0$v" else "$v"

    companion object {
        private val MONTHS = listOf(
            "січня", "лютого", "березня", "квітня", "травня", "червня",
            "липня", "серпня", "вересня", "жовтня", "листопада", "грудня",
        )

        fun fromEpochDay(epochDay: Long): SimpleDate {
            val z = epochDay + 719468L
            val era = (if (z >= 0) z else z - 146096) / 146097
            val doe = z - era * 146097
            val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
            val y = yoe + era * 400
            val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
            val mp = (5 * doy + 2) / 153
            val d = doy - (153 * mp + 2) / 5 + 1
            val m = if (mp < 10) mp + 3 else mp - 9
            return SimpleDate((if (m <= 2) y + 1 else y).toInt(), m.toInt(), d.toInt())
        }

        /** Розбирає 2026-11-12 або 12.11.2026; повертає null, якщо не вийшло. */
        fun parse(text: String): SimpleDate? {
            val iso = Regex("""^(\d{4})-(\d{2})-(\d{2})$""").find(text.trim())
            if (iso != null) {
                val (y, m, d) = iso.destructured
                return SimpleDate(y.toInt(), m.toInt(), d.toInt()).takeIf { it.isValid() }
            }
            val dotted = Regex("""^(\d{1,2})\.(\d{1,2})\.(\d{4})$""").find(text.trim())
            if (dotted != null) {
                val (d, m, y) = dotted.destructured
                return SimpleDate(y.toInt(), m.toInt(), d.toInt()).takeIf { it.isValid() }
            }
            return null
        }
    }

    fun isValid(): Boolean {
        if (month !in 1..12 || day < 1 || year !in 1900..2200) return false
        return day <= daysInMonth(year, month)
    }
}

fun daysInMonth(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    else -> 0
}

/** Сьогоднішня дата за годинником пристрою. */
expect fun today(): SimpleDate
