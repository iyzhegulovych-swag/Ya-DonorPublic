package com.example.manq.storage

/** Сховище «ключ -> рядок» на самому пристрої. */
interface Storage {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}

/** Сховище в пам'яті — для тестів і як запасний варіант. */
class InMemoryStorage(
    private val values: MutableMap<String, String> = mutableMapOf(),
) : Storage {
    override fun getString(key: String): String? = values[key]
    override fun putString(key: String, value: String) {
        values[key] = value
    }
    override fun remove(key: String) {
        values.remove(key)
    }
}

expect class KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}

expect fun createKeyValueStore(): KeyValueStore

/** Платформенний клас не підміниш у тестах, тому код працює з [Storage]. */
fun KeyValueStore.asStorage(): Storage = object : Storage {
    override fun getString(key: String): String? = this@asStorage.getString(key)
    override fun putString(key: String, value: String) = this@asStorage.putString(key, value)
    override fun remove(key: String) = this@asStorage.remove(key)
}
