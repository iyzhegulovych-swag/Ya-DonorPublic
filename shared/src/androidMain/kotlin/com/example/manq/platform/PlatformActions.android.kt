package com.example.manq.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.manq.data.SimpleDate

actual class PlatformActions(private val context: Context) {

    private fun open(intent: Intent) {
        runCatching {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    /** ACTION_DIAL лише відкриває набирач, тому дозвіл CALL_PHONE не потрібен. */
    actual fun dial(phone: String) {
        val digits = phone.filter { it.isDigit() || it == '+' }
        open(Intent(Intent.ACTION_DIAL, "tel:$digits".toUri()))
    }

    actual fun openMap(address: String) {
        open(Intent(Intent.ACTION_VIEW, "geo:0,0?q=${Uri.encode(address)}".toUri()))
    }

    actual fun openUrl(url: String) {
        open(Intent(Intent.ACTION_VIEW, url.toUri()))
    }

    actual fun sendEmail(address: String) {
        open(Intent(Intent.ACTION_SENDTO, "mailto:$address".toUri()))
    }

    /**
     * ACTION_INSERT відкриває екран нової події, тому дозвіл WRITE_CALENDAR
     * не потрібен. Для події на цілий день Android чекає північ за UTC.
     */
    actual fun addToCalendar(
        title: String,
        details: String,
        location: String,
        date: SimpleDate,
    ) {
        val start = date.toEpochDay() * MILLIS_IN_DAY
        open(
            Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, details)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, start + MILLIS_IN_DAY),
        )
    }

    private fun String.toUri(): Uri = Uri.parse(this)

    private companion object {
        const val MILLIS_IN_DAY = 24L * 60 * 60 * 1000
    }
}

@Composable
actual fun rememberPlatformActions(): PlatformActions {
    val context = LocalContext.current
    return remember(context) { PlatformActions(context) }
}
