package com.example.manq.network

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSData
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.dataTaskWithURL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
actual suspend fun httpGetText(url: String): String? = suspendCoroutine { continuation ->
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl == null) {
        continuation.resume(null)
        return@suspendCoroutine
    }

    NSURLSession.sharedSession.dataTaskWithURL(nsUrl) { data, response, _ ->
        val code = (response as? NSHTTPURLResponse)?.statusCode?.toInt()
        val body = if (code != null && code in 200..299 && data != null) {
            decode(data)
        } else {
            null
        }
        continuation.resume(body)
    }.resume()
}

/**
 * Читає тіло відповіді як UTF-8. Байти беремо напряму: приведення
 * NSString до String у Kotlin/Native дає null.
 */
@OptIn(ExperimentalForeignApi::class)
private fun decode(data: NSData): String? {
    val bytes = data.bytes?.reinterpret<ByteVar>() ?: return null
    return bytes.readBytes(data.length.toInt()).decodeToString()
}
