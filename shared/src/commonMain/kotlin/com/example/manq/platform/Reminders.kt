package com.example.manq.platform

import androidx.compose.runtime.Composable
import com.example.manq.data.SimpleDate

/**
 * Нагадування про візит і про те, що інтервал після донації минув.
 * Усе локальне: пуш-сервіс не потрібен.
 */
expect class Reminders {
    /** Спрацює о 10:00 вказаного дня; виклик з тим самим [id] замінює попереднє. */
    fun schedule(id: String, title: String, text: String, date: SimpleDate)

    fun cancel(id: String)
}

@Composable
expect fun rememberReminders(): Reminders

/** Дозвіл на показ сповіщень. */
interface NotificationPermissionState {
    val granted: Boolean
    fun request()
}

@Composable
expect fun rememberNotificationPermission(): NotificationPermissionState
