package com.example.manq.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.manq.data.SimpleDate
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.EventKit.EKEntityType
import platform.EventKit.EKEvent
import platform.EventKit.EKEventStore
import platform.EventKit.EKSpan
import platform.Foundation.NSCalendar
import platform.Foundation.NSCharacterSet
import platform.Foundation.NSDateComponents
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.URLQueryAllowedCharacterSet
import platform.Foundation.create
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.UIKit.UIApplication

actual class PlatformActions {

    private fun open(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        val app = UIApplication.sharedApplication
        if (app.canOpenURL(nsUrl)) {
            app.openURL(nsUrl, options = emptyMap<Any?, Any>(), completionHandler = null)
        }
    }

    /** Percent-encoding є лише на NSString, а кастом її з рядка не отримати. */
    @OptIn(BetaInteropApi::class)
    private fun encode(value: String): String = NSString.create(string = value)
        .stringByAddingPercentEncodingWithAllowedCharacters(
            NSCharacterSet.URLQueryAllowedCharacterSet,
        ) ?: value

    actual fun dial(phone: String) {
        val digits = phone.filter { it.isDigit() || it == '+' }
        open("tel://$digits")
    }

    /** На iOS немає схеми geo:, тому відкриваємо Apple Maps. */
    actual fun openMap(address: String) {
        open("http://maps.apple.com/?q=${encode(address)}")
    }

    actual fun openUrl(url: String) = open(url)

    actual fun sendEmail(address: String) = open("mailto:$address")

    /**
     * Схеми для екрана нової події на iOS немає, тому подію створює сам
     * додаток через EventKit. Текст запиту дозволу — в Info.plist.
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun addToCalendar(
        title: String,
        details: String,
        location: String,
        date: SimpleDate,
    ) {
        val store = EKEventStore()
        store.requestAccessToEntityType(EKEntityType.EKEntityTypeEvent) { granted, _ ->
            if (!granted) return@requestAccessToEntityType

            val components = NSDateComponents().apply {
                year = date.year.toLong()
                month = date.month.toLong()
                day = date.day.toLong()
                hour = 9
                minute = 0
                calendar = NSCalendar.currentCalendar
            }
            val start = components.date ?: return@requestAccessToEntityType
            val calendar = store.defaultCalendarForNewEvents
                ?: return@requestAccessToEntityType

            val event = EKEvent.eventWithEventStore(store).apply {
                setTitle(title)
                setNotes(details)
                setLocation(location)
                setAllDay(true)
                setStartDate(start)
                setEndDate(start)
                setCalendar(calendar)
            }
            store.saveEvent(event, EKSpan.EKSpanThisEvent, true, null)
        }
    }
}

@Composable
actual fun rememberPlatformActions(): PlatformActions = remember { PlatformActions() }
