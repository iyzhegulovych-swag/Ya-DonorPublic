package com.example.manq.data

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ArticlesTest {

    private val json = Json { ignoreUnknownKeys = true }

    private fun parse(text: String) = json.decodeFromString<ArticlesSnapshot>(text)

    @Test
    fun readsArticlesFromJson() {
        val snapshot = parse(
            """
            {
              "version": 1,
              "updatedAt": "2026-11-13",
              "source": "donor.utcc.gov.ua",
              "articles": [
                {
                  "id": "prepare",
                  "title": "Як підготуватися",
                  "summary": "Коротко",
                  "sections": [
                    { "body": ["Перший абзац."] },
                    { "heading": "За 3 дні", "body": ["Без аспірину.", "І без алкоголю."] }
                  ]
                }
              ]
            }
            """.trimIndent(),
        )

        assertEquals(1, snapshot.articles.size)
        val article = snapshot.articles.single()
        assertEquals("Як підготуватися", article.title)
        assertEquals(2, article.sections.size)
        assertNull(article.sections.first().heading, "секція без підзаголовка")
        assertEquals("За 3 дні", article.sections[1].heading)
        assertEquals(2, article.sections[1].body.size)
        assertEquals(SimpleDate(2026, 11, 13), snapshot.date)
    }

    @Test
    fun findsArticleById() {
        val snapshot = ArticlesSnapshot(
            articles = listOf(
                Article(id = "why", title = "Навіщо"),
                Article(id = "rights", title = "Права"),
            ),
        )
        assertEquals("Права", snapshot.byId("rights")?.title)
        assertNull(snapshot.byId("no-such-article"))
    }

    @Test
    fun unknownFieldsDoNotBreakParsing() {
        val snapshot = parse(
            """{ "updatedAt": "2026-01-01", "newFieldFromFuture": 42, "articles": [] }""",
        )
        assertTrue(snapshot.articles.isEmpty(), "статей немає, але й падіння немає")
    }

    @Test
    fun emptyDateIsNull() {
        assertNull(ArticlesSnapshot().date, "без дати нічого порівнювати")
    }

    @Test
    fun sectionCanCarryPicture() {
        val snapshot = json.decodeFromString<ArticlesSnapshot>(
            """
            {
              "articles": [{
                "id": "x", "title": "x",
                "sections": [{
                  "picture": { "file": "images/a.jpg", "caption": "підпис", "link": "https://example.org" },
                  "body": ["абзац"]
                }]
              }]
            }
            """.trimIndent(),
        )
        val picture = snapshot.byId("x")!!.sections.single().picture!!
        assertEquals("images/a.jpg", picture.file)
        assertEquals("https://example.org", picture.link)
    }

    @Test
    fun sectionWithoutPictureStaysNull() {
        val snapshot = json.decodeFromString<ArticlesSnapshot>(
            """{ "articles": [{ "id": "x", "title": "x", "sections": [{ "body": ["а"] }] }] }""",
        )
        assertNull(snapshot.byId("x")!!.sections.single().picture)
    }
}
