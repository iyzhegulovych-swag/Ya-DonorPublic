package com.example.manq.data

import kotlinx.serialization.json.Json
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Перевіряє файли, вшиті в додаток: зламаний JSON на пристрої виглядав
 * би як порожній розділ без жодної помилки.
 */
class BundledResourcesTest {

    private val json = Json { ignoreUnknownKeys = true }

    private fun resource(name: String): String {
        val file = File("src/commonMain/composeResources/files/$name")
        assertTrue(file.exists(), "немає файлу ${file.path}")
        return file.readText()
    }

    @Test
    fun bundledArticlesAreValid() {
        val snapshot = json.decodeFromString<ArticlesSnapshot>(resource("articles.json"))

        assertTrue(snapshot.articles.isNotEmpty(), "у пам'ятці має бути хоч одна стаття")
        assertTrue(snapshot.source.isNotBlank(), "джерело має бути вказане")
        assertTrue(snapshot.date != null, "дата правки має читатись")

        val ids = snapshot.articles.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "id статей мають бути унікальні")

        snapshot.articles
            .flatMap { it.sections }
            .mapNotNull { it.picture }
            .forEach { picture ->
                assertTrue(
                    File("src/commonMain/composeResources/files/${picture.file}").exists(),
                    "немає знімка ${picture.file}",
                )
            }

        snapshot.articles.forEach { article ->
            assertTrue(article.title.isNotBlank(), "стаття ${article.id} без заголовка")
            assertTrue(article.sections.isNotEmpty(), "стаття ${article.id} без тексту")
            article.sections.forEach { section ->
                assertTrue(
                    section.body.isNotEmpty() || section.picture != null,
                    "порожня секція у ${article.id}",
                )
            }
        }
    }

    @Test
    fun bundledNeedsMatchDirectory() {
        val snapshot = json.decodeFromString<NeedsSnapshot>(resource("needs.json"))
        val repo = BloodCentersRepository()

        assertTrue(snapshot.centers.isNotEmpty(), "у зрізі має бути хоч один центр")
        assertTrue(snapshot.date != null, "дата збору має читатись")

        snapshot.centers.forEach { center ->
            assertTrue(
                repo.centerById(center.centerId) != null,
                "у довіднику немає центру ${center.centerId}",
            )
            assertTrue(center.needs.isNotEmpty(), "центр ${center.centerId} без груп крові")
        }
    }

    @Test
    fun bundledDonorDaysAreValid() {
        val snapshot = json.decodeFromString<DonorDaysSnapshot>(resource("donor-days.json"))
        val repo = BloodCentersRepository()

        assertTrue(snapshot.date != null, "дата правки має читатись")

        val ids = snapshot.days.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "id днів донора мають бути унікальні")

        snapshot.days.forEach { day ->
            assertTrue(day.title.isNotBlank(), "день донора ${day.id} без заголовка")
            assertTrue(day.day != null, "день донора ${day.id} з нечитабельною датою")
            if (day.centerId.isNotBlank()) {
                assertTrue(
                    repo.centerById(day.centerId) != null,
                    "у довіднику немає центру ${day.centerId}",
                )
            } else {
                assertTrue(day.place.isNotBlank(), "день донора ${day.id} без місця")
            }
        }
    }

    /** Кожна іконка з AppIcons має лежати у ресурсах. */
    @Test
    fun everyIconHasItsFile() {
        val drawables = File("src/commonMain/composeResources/drawable")
        val icons = File("src/commonMain/kotlin/com/example/manq/ui/components/AppIcons.kt")
            .readText()
            .let { Regex("""Res\.drawable\.(ic_\w+)""").findAll(it) }
            .map { it.groupValues[1] }
            .toList()

        assertTrue(icons.isNotEmpty(), "іконки мають читатись із ресурсів")
        icons.forEach { name ->
            assertTrue(File(drawables, "$name.xml").exists(), "немає $name.xml")
        }
    }
}
