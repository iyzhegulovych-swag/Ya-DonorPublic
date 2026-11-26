package com.example.manq

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.manq.data.AnnualDonorEvents
import com.example.manq.data.AppStore
import com.example.manq.data.ArticlesRepository
import com.example.manq.data.BloodCentersRepository
import com.example.manq.data.BloodType
import com.example.manq.data.DonorDaysRepository
import com.example.manq.data.NeedsRepository
import com.example.manq.data.today
import com.example.manq.platform.rememberReminders
import com.example.manq.storage.asStorage
import com.example.manq.storage.createKeyValueStore
import com.example.manq.ui.Screen
import com.example.manq.ui.components.BottomBar
import com.example.manq.ui.components.BrandHeader
import com.example.manq.ui.screens.ArticleScreen
import com.example.manq.ui.screens.CalendarScreen
import com.example.manq.ui.screens.CenterDetailsScreen
import com.example.manq.ui.screens.CentersScreen
import com.example.manq.ui.screens.KnowledgeScreen
import com.example.manq.ui.screens.PlanVisitScreen
import com.example.manq.ui.screens.ProfileScreen
import com.example.manq.ui.theme.YaDonorTheme

@Composable
fun App() {
    YaDonorTheme {
        val repository = remember { BloodCentersRepository() }
        val storage = remember { createKeyValueStore().asStorage() }
        val store = remember { AppStore(storage) }
        val needsRepository = remember { NeedsRepository(storage) }
        val articlesRepository = remember { ArticlesRepository(storage) }
        val donorDaysRepository = remember { DonorDaysRepository(storage) }
        val reminders = rememberReminders()

        // Окремі ефекти, щоб довге очікування одного не тримало інші.
        LaunchedEffect(Unit) { needsRepository.load() }
        LaunchedEffect(Unit) { articlesRepository.load() }
        LaunchedEffect(Unit) { donorDaysRepository.load() }

        // Ставимо при кожному запуску: будильники не переживають
        // перезавантаження телефона.
        LaunchedEffect(Unit) {
            val now = today()
            AnnualDonorEvents.all.forEach { event ->
                reminders.schedule(
                    id = event.id,
                    title = event.title,
                    text = event.reminder,
                    date = event.next(now),
                )
            }
        }

        val needs = needsRepository.snapshot
        val knowledge = articlesRepository.snapshot
        val donorDays = donorDaysRepository.snapshot
        val userBloodType = BloodType.of(store.profile.bloodGroup, store.profile.rhFactor)

        // Навігація тримається на одній змінній: екранів мало, окрема
        // бібліотека навігації тут нічого б не спростила.
        var screen by remember { mutableStateOf<Screen>(Screen.Tabs(Screen.Tab.CENTERS)) }
        var lastTab by remember { mutableStateOf(Screen.Tab.CENTERS) }

        fun openTab(tab: Screen.Tab) {
            lastTab = tab
            screen = Screen.Tabs(tab)
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    BottomBar(
                        current = lastTab,
                        onSelect = { tab ->
                            // Кнопка «Донація» — це не вкладка, а дія:
                            // вона веде одразу до планування візиту.
                            if (tab == Screen.Tab.DONATION) {
                                screen = Screen.PlanVisit()
                            } else {
                                openTab(tab)
                            }
                        },
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                        .statusBarsPadding(),
                ) {
                    BrandHeader()

                    when (val current = screen) {
                        is Screen.Tabs -> when (current.tab) {
                            Screen.Tab.KNOWLEDGE -> KnowledgeScreen(
                                articles = knowledge?.articles.orEmpty(),
                                source = knowledge?.source.orEmpty(),
                                onOpenArticle = { screen = Screen.Article(it.id) },
                            )

                            Screen.Tab.CENTERS -> CentersScreen(
                                repository = repository,
                                favorites = store.favorites,
                                needs = needs,
                                userBloodType = userBloodType,
                                onToggleFavorite = { store.toggleFavorite(it.id) },
                                onOpenCenter = { screen = Screen.CenterDetails(it.id) },
                            )

                            Screen.Tab.DONATION,
                            Screen.Tab.CALENDAR,
                            -> CalendarScreen(
                                store = store,
                                repository = repository,
                                donorDays = donorDays,
                                onPlanVisit = { screen = Screen.PlanVisit() },
                            )

                            Screen.Tab.PROFILE -> ProfileScreen(
                                store = store,
                                repository = repository,
                                onOpenCalendar = { openTab(Screen.Tab.CALENDAR) },
                            )
                        }

                        is Screen.CenterDetails -> {
                            val center = repository.centerById(current.centerId)
                            CenterDetailsScreen(
                                center = center,
                                location = center?.let { repository.locationOf(it) },
                                needs = needs,
                                donorDays = donorDays
                                    ?.forCenter(current.centerId)
                                    .orEmpty(),
                                userBloodType = userBloodType,
                                isFavorite = center?.id?.let { store.isFavorite(it) } ?: false,
                                onToggleFavorite = { center?.let { store.toggleFavorite(it.id) } },
                                onPlanVisit = { screen = Screen.PlanVisit(current.centerId) },
                                onBack = { screen = Screen.Tabs(lastTab) },
                            )
                        }

                        is Screen.Article -> ArticleScreen(
                            article = knowledge?.byId(current.articleId),
                            source = knowledge?.source.orEmpty(),
                            onBack = { openTab(Screen.Tab.KNOWLEDGE) },
                        )

                        is Screen.PlanVisit -> PlanVisitScreen(
                            store = store,
                            repository = repository,
                            preselectedCenterId = current.centerId,
                            onDone = { openTab(Screen.Tab.CALENDAR) },
                            onBack = { screen = Screen.Tabs(lastTab) },
                        )
                    }
                }
            }
        }
    }
}
