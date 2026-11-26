package com.example.manq.data

import kotlinx.serialization.Serializable

/** Пам'ятка донора. Тексти лежать у ресурсі files/articles.json. */
@Serializable
data class Article(
    val id: String,
    val title: String,
    val summary: String = "",
    val sections: List<Section> = emptyList(),
) {
    /** [heading] може бути відсутнім — тоді це просто абзаци без підзаголовка. */
    @Serializable
    data class Section(
        val heading: String? = null,
        val body: List<String> = emptyList(),
        val picture: Picture? = null,
    )

    /**
     * Ілюстрація до розділу. [file] — шлях усередині composeResources/files,
     * [link] відкривається при дотику до знімка.
     */
    @Serializable
    data class Picture(
        val file: String,
        val caption: String = "",
        val link: String = "",
    )
}

/** Статті з датою правки: по [updatedAt] вибирається найновіша версія файлу. */
@Serializable
data class ArticlesSnapshot(
    val version: Int = 1,
    val updatedAt: String = "",
    val source: String = "",
    val articles: List<Article> = emptyList(),
) {
    fun byId(id: String): Article? = articles.firstOrNull { it.id == id }

    val date: SimpleDate? get() = SimpleDate.parse(updatedAt)
}
