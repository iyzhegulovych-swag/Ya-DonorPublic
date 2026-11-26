package com.example.manq.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.manq.network.httpGetText
import com.example.manq.storage.Storage
import kotlinx.serialization.json.Json
import manq.shared.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

/** Пам'ятка донора: вшита копія, збережена версія і щоденна перевірка оновлень. */
class ArticlesRepository(private val storage: Storage) {

    private val json = Json { ignoreUnknownKeys = true }

    var snapshot: ArticlesSnapshot? by mutableStateOf(null)
        private set

    /** Викликається один раз при старті. */
    suspend fun load(now: SimpleDate = today()) {
        snapshot = pickNewer(readCached(), readBundled())
        if (shouldCheck(now)) refreshFromNetwork(now)
    }

    /** Чи вже перевіряли оновлення сьогодні. */
    private fun shouldCheck(now: SimpleDate): Boolean = storage.getString(KEY_CHECKED) != now.iso()

    /** Тихо перевіряє, чи є новіші статті. Помилка мережі нічого не ламає. */
    suspend fun refreshFromNetwork(now: SimpleDate = today()) {
        val body = httpGetText(REMOTE_URL) ?: return
        storage.putString(KEY_CHECKED, now.iso())
        val remote = parse(body) ?: return
        if (remote.articles.isEmpty()) return
        if (pickNewer(snapshot, remote) === remote) {
            snapshot = remote
            storage.putString(KEY_CACHE, body)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun readBundled(): ArticlesSnapshot? = runCatching {
        parse(Res.readBytes("files/articles.json").decodeToString())
    }.getOrNull()

    private fun readCached(): ArticlesSnapshot? = storage.getString(KEY_CACHE)?.let { parse(it) }

    private fun parse(text: String): ArticlesSnapshot? = runCatching { json.decodeFromString<ArticlesSnapshot>(text) }.getOrNull()

    /** З двох версій лишаємо ту, що має новішу дату правки. */
    private fun pickNewer(a: ArticlesSnapshot?, b: ArticlesSnapshot?): ArticlesSnapshot? {
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
        const val KEY_CACHE = "articles_cache"
        const val KEY_CHECKED = "articles_checked_on"

        const val REMOTE_URL =
            "https://raw.githubusercontent.com/morabatur/Ya-Donor-data/data/articles.json"
    }
}
