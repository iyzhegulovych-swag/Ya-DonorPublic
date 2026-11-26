package com.example.manq.platform

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.manq.data.SimpleDate
import java.util.Calendar

private const val CHANNEL_ID = "donation_reminders"
private const val EXTRA_TITLE = "title"
private const val EXTRA_TEXT = "text"
private const val EXTRA_ID = "id"

actual class Reminders(private val context: Context) {

    private val alarmManager: AlarmManager?
        get() = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    actual fun schedule(id: String, title: String, text: String, date: SimpleDate) {
        val at = Calendar.getInstance().apply {
            set(date.year, date.month - 1, date.day, 10, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        // Дата вже минула — нагадувати нема про що.
        if (at.timeInMillis <= System.currentTimeMillis()) return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(EXTRA_ID, id)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_TEXT, text)
        }
        // Неточний будильник не потребує дозволу SCHEDULE_EXACT_ALARM
        // і ощадливіше витрачає батарею.
        alarmManager?.set(AlarmManager.RTC_WAKEUP, at.timeInMillis, pendingIntent(id, intent))
    }

    actual fun cancel(id: String) {
        val intent = Intent(context, ReminderReceiver::class.java)
        alarmManager?.cancel(pendingIntent(id, intent))
    }

    private fun pendingIntent(id: String, intent: Intent): PendingIntent = PendingIntent.getBroadcast(
        context,
        id.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}

/** Приймає сигнал будильника й показує сповіщення. */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "Нагадування про донацію",
                    NotificationManager.IMPORTANCE_DEFAULT,
                ),
            )
        }

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(context)
        }

        val notification = builder
            .setContentTitle(intent.getStringExtra(EXTRA_TITLE) ?: "ЯДонор")
            .setContentText(intent.getStringExtra(EXTRA_TEXT).orEmpty())
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        val id = intent.getStringExtra(EXTRA_ID)?.hashCode() ?: 0
        runCatching { manager.notify(id, notification) }
    }
}

@Composable
actual fun rememberReminders(): Reminders {
    val context = LocalContext.current
    return remember(context) { Reminders(context) }
}

private class AndroidNotificationPermission(
    private val needed: Boolean,
    initiallyGranted: Boolean,
) : NotificationPermissionState {
    override var granted: Boolean by mutableStateOf(initiallyGranted)
    var askSystem: () -> Unit = {}

    override fun request() {
        if (needed) askSystem() else granted = true
    }
}

@Composable
actual fun rememberNotificationPermission(): NotificationPermissionState {
    val context = LocalContext.current

    // До Android 13 дозвіл на сповіщення не питали взагалі.
    val needed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    val state = remember(context) {
        AndroidNotificationPermission(
            needed = needed,
            initiallyGranted = !needed ||
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { result -> state.granted = result }

    state.askSystem = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }

    return state
}
