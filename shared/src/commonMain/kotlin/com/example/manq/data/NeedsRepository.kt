package com.example.manq.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.manq.network.httpGetText
import com.example.manq.storage.Storage
import kotlinx.serialization.json.Json
import manq.shared.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

/** Потреба у крові по центрах: вшита копія, збережений зріз, оновлення з мережі. */
class NeedsRepository(private val storage: Storage) {

    private val json = Json { ignoreUnknownKeys = true }

    var snapshot: NeedsSnapshot? by mutableStateOf(null)
        private set

    /** Викликається один раз при старті. */
    suspend fun load() {
        snapshot = pickNewer(readCached(), readBundled())
        refreshFromNetwork()
    }

    /** Тихо перевіряє, чи є новіші дані. Помилка мережі нічого не ламає. */
    suspend fun refreshFromNetwork() {
        val body = httpGetText(REMOTE_URL) ?: return
        val remote = parse(body) ?: return
        val newer = pickNewer(snapshot, remote) ?: return
        if (newer === remote) {
            snapshot = remote
            storage.putString(KEY_CACHE, body)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun readBundled(): NeedsSnapshot? = runCatching {
        parse(Res.readBytes("files/needs.json").decodeToString())
    }.getOrNull()

    private fun readCached(): NeedsSnapshot? = storage.getString(KEY_CACHE)?.let { parse(it) }

    private fun parse(text: String): NeedsSnapshot? = runCatching { json.decodeFromString<NeedsSnapshot>(text) }.getOrNull()

    /** З двох зрізів лишаємо новіший за датою збору. */
    private fun pickNewer(a: NeedsSnapshot?, b: NeedsSnapshot?): NeedsSnapshot? {
        if (a == null) return b
        if (b == null) return a
        val dateA = a.date
        val dateB = b.date
        return when {
            dateA == null -> b
            dateB == null -> a
            dateB > dateA -> b
            else -> a
        }
    }

    private companion object {
        const val KEY_CACHE = "needs_cache"

        const val REMOTE_URL =
            "https://raw.githubusercontent.com/morabatur/Ya-Donor-data/data/needs.json"
    }
}
