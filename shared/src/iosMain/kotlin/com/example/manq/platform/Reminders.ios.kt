package com.example.manq.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.manq.data.SimpleDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

actual class Reminders {

    private val center get() = UNUserNotificationCenter.currentNotificationCenter()

    actual fun schedule(id: String, title: String, text: String, date: SimpleDate) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(text)
        }

        val components = NSDateComponents().apply {
            year = date.year.toLong()
            month = date.month.toLong()
            day = date.day.toLong()
            hour = 10
            minute = 0
            calendar = NSCalendar.currentCalendar
        }

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents = components,
            repeats = false,
        )

        // Запит з тим самим identifier замінює попередній.
        center.addNotificationRequest(
            UNNotificationRequest.requestWithIdentifier(id, content, trigger),
            withCompletionHandler = null,
        )
    }

    actual fun cancel(id: String) {
        center.removePendingNotificationRequestsWithIdentifiers(listOf(id))
    }
}

@Composable
actual fun rememberReminders(): Reminders = remember { Reminders() }

@Composable
actual fun rememberNotificationPermission(): NotificationPermissionState {
    var granted by remember { mutableStateOf(false) }
    val center = remember { UNUserNotificationCenter.currentNotificationCenter() }

    remember {
        center.getNotificationSettingsWithCompletionHandler { settings ->
            granted = settings?.authorizationStatus == UNAuthorizationStatusAuthorized
        }
        true
    }

    return remember {
        object : NotificationPermissionState {
            override val granted: Boolean get() = granted
            override fun request() {
                center.requestAuthorizationWithOptions(
                    UNAuthorizationOptionAlert or
                        UNAuthorizationOptionSound or
                        UNAuthorizationOptionBadge,
                ) { allowed, _ -> granted = allowed }
            }
        }
    }
}
