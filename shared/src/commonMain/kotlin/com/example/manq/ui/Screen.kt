package com.example.manq.ui

/** Екрани додатку. Навігація тримається на одній змінній стану. */
sealed interface Screen {

    /** Вкладки нижньої панелі. */
    enum class Tab(val title: String) {
        KNOWLEDGE("Знання"),
        CENTERS("Центри"),
        DONATION("Донація"),
        CALENDAR("Календар"),
        PROFILE("Профіль"),
    }

    data class Tabs(val tab: Tab) : Screen

    /** Карточка одного центру, відкрита зі списку. */
    data class CenterDetails(val centerId: String) : Screen

    /** Стаття з розділу «Знання». */
    data class Article(val articleId: String) : Screen

    /** Планування візиту; центр може бути вже обраний. */
    data class PlanVisit(val centerId: String? = null) : Screen
}
