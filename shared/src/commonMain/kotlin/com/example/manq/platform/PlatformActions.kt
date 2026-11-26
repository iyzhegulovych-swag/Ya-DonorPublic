package com.example.manq.platform

import androidx.compose.runtime.Composable
import com.example.manq.data.SimpleDate

/**
 * Дії, які вміє тільки операційна система: подзвонити, відкрити карти,
 * сайт, пошту або календар. Реалізації — в androidMain та iosMain.
 */
expect class PlatformActions {
    fun dial(phone: String)
    fun openMap(address: String)
    fun openUrl(url: String)
    fun sendEmail(address: String)

    /** Додає подію на цілий день у календар пристрою. */
    fun addToCalendar(title: String, details: String, location: String, date: SimpleDate)
}

@Composable
expect fun rememberPlatformActions(): PlatformActions
