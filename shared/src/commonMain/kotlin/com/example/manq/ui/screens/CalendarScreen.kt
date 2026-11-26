package com.example.manq.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.manq.data.AnnualDonorEvents
import com.example.manq.data.AppStore
import com.example.manq.data.BloodCentersRepository
import com.example.manq.data.BloodComponent
import com.example.manq.data.Donation
import com.example.manq.data.DonationIntervals
import com.example.manq.data.DonorDaysSnapshot
import com.example.manq.data.SimpleDate
import com.example.manq.data.today
import com.example.manq.platform.rememberNotificationPermission
import com.example.manq.platform.rememberPlatformActions
import com.example.manq.platform.rememberReminders
import com.example.manq.ui.components.AnnualEventCard
import com.example.manq.ui.components.AppIcons
import com.example.manq.ui.components.DonorDayCard
import com.example.manq.ui.components.InfoNote
import com.example.manq.ui.components.ScreenTitle

@Composable
fun CalendarScreen(
    store: AppStore,
    repository: BloodCentersRepository,
    donorDays: DonorDaysSnapshot? = null,
    onPlanVisit: () -> Unit,
) {
    val now = today()
    val reminders = rememberReminders()
    val permission = rememberNotificationPermission()
    val actions = rememberPlatformActions()
    val planned = store.plannedVisits
    val history = store.completedDonations

    /** Переносить запис у календар телефона. */
    fun exportToCalendar(donation: Donation) {
        val center = repository.centerById(donation.centerId)
        val day = donation.day ?: return
        actions.addToCalendar(
            title = "Донація крові: ${donation.component.title}",
            details = buildString {
                append(center?.name ?: "Центр крові")
                if (center != null && center.has(center.phone)) {
                    append("\nТелефон: ${center.phone}")
                }
                append("\n\nЗапис і час прийому підтвердь безпосередньо в центрі.")
            },
            location = center?.address.orEmpty(),
            date = day,
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { ScreenTitle("Календар", "Твої плани й історія донацій") }

        item {
            Button(
                onClick = onPlanVisit,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text("Запланувати візит", fontWeight = FontWeight.Bold)
            }
        }

        // Нагадування: коли мине мінімальний інтервал після останньої донації.
        val readiness = BloodComponent.entries.mapNotNull { component ->
            store.nextAllowedDate(component)?.let { component to it }
        }
        if (readiness.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "Коли можна знову",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    readiness.forEach { (component, date) ->
                        ReadyCard(component = component, allowedFrom = date, now = now)
                    }
                }
            }
        }

        if (!permission.granted && (planned.isNotEmpty() || history.isNotEmpty())) {
            item { NotificationsOffCard(onEnable = { permission.request() }) }
        }

        if (planned.isEmpty() && history.isEmpty()) {
            item { EmptyCalendar() }
        }

        if (planned.isNotEmpty()) {
            item { SectionTitle("Заплановані візити") }
            items(planned, key = { it.id }) { donation ->
                DonationCard(
                    donation = donation,
                    centerName = repository.centerById(donation.centerId)?.name.orEmpty(),
                    now = now,
                    onComplete = {
                        store.markCompleted(donation.id)
                        reminders.cancel("visit-${donation.id}")
                        // Після донації одразу ставимо нагадування на
                        // день, коли мине мінімальний інтервал.
                        store.nextAllowedDate(donation.component)?.let { ready ->
                            reminders.schedule(
                                id = "ready-${donation.component.name}",
                                title = "Можна знову здавати",
                                text = "${donation.component.title}: мінімальний інтервал минув.",
                                date = ready,
                            )
                        }
                    },
                    onRemove = { store.removeDonation(donation.id) },
                    onAddToCalendar = { exportToCalendar(donation) },
                )
            }
        }

        if (history.isNotEmpty()) {
            item { SectionTitle("Історія") }
            items(history, key = { it.id }) { donation ->
                DonationCard(
                    donation = donation,
                    centerName = repository.centerById(donation.centerId)?.name.orEmpty(),
                    now = now,
                    onComplete = null,
                    onRemove = { store.removeDonation(donation.id) },
                    onAddToCalendar = { exportToCalendar(donation) },
                )
            }
        }

        item { SectionTitle("Дні донора") }

        // Постійні дати: вони є в календарі завжди, навіть якщо
        // жодної акції поблизу не оголошено.
        items(AnnualDonorEvents.all, key = { it.id }) { event ->
            AnnualEventCard(
                event = event,
                now = now,
                onAddToCalendar = {
                    actions.addToCalendar(
                        title = event.title,
                        details = event.details,
                        location = "",
                        date = event.next(now),
                    )
                },
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        val upcoming = donorDays?.upcoming(now).orEmpty()
        if (upcoming.isEmpty()) {
            item {
                InfoNote("Найближчих днів донора та виїзних акцій поки не оголошено. Розклад оновлюється автоматично.")
            }
        } else {
            items(upcoming, key = { it.id }) { donorDay ->
                val center = repository.centerById(donorDay.centerId)
                DonorDayCard(
                    donorDay = donorDay,
                    now = now,
                    centerName = center?.name.orEmpty(),
                    onAddToCalendar = {
                        actions.addToCalendar(
                            title = donorDay.title,
                            details = listOf(donorDay.time.orEmpty(), donorDay.details)
                                .filter { it.isNotBlank() }
                                .joinToString("\n"),
                            location = center?.address ?: donorDay.place,
                            date = donorDay.day ?: now,
                        )
                    },
                    onOpenUrl = { actions.openUrl(donorDay.url) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }

        item {
            InfoNote("Це твій особистий календар. Підтверджуй запис і час прийому безпосередньо в центрі крові.")
        }
    }
}

@Composable
private fun NotificationsOffCard(onEnable: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                AppIcons.Bell,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
            )
            Column(Modifier.weight(1f)) {
                Text(
                    "Сповіщення вимкнені",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "Увімкни, щоб отримати нагадування напередодні візиту й коли мине інтервал після донації.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            TextButton(onClick = onEnable) { Text("Увімкнути") }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
}

@Composable
private fun EmptyCalendar() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Icon(
                AppIcons.Calendar,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(34.dp),
            )
        }
        Text(
            "Почнімо з першого плану",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            "Обери зручний центр, дату й компонент крові. Збережений план з'явиться тут.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ReadyCard(component: BloodComponent, allowedFrom: SimpleDate, now: SimpleDate) {
    val daysLeft = now.daysUntil(allowedFrom)
    val ready = daysLeft <= 0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (ready) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                if (ready) AppIcons.Check else AppIcons.Bell,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = component.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = if (ready) {
                        "Можна здавати вже зараз"
                    } else {
                        "Можна з ${allowedFrom.formatted()} — лишилось $daysLeft дн."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (DonationIntervals.isApproximate(component)) {
                    Text(
                        text = "Інтервал орієнтовний, уточнюйте в центрі",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun DonationCard(
    donation: Donation,
    centerName: String,
    now: SimpleDate,
    onComplete: (() -> Unit)?,
    onRemove: () -> Unit,
    onAddToCalendar: () -> Unit,
) {
    val day = donation.day

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = day?.let { "${it.day} ${it.monthName()} ${it.year}" }.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = centerName.ifBlank { "Центр не знайдено" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = donation.component.title,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                Icon(
                    AppIcons.Close,
                    contentDescription = "Видалити запис",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onRemove)
                        .padding(6.dp)
                        .size(18.dp),
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onComplete != null) {
                    val passed = day != null && day <= now
                    TextButton(onClick = onComplete) {
                        Text(if (passed) "Позначити завершеною" else "Я вже здав(ла)")
                    }
                }
                TextButton(onClick = onAddToCalendar, enabled = day != null) {
                    Icon(
                        AppIcons.Calendar,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Text("  У календар")
                }
            }
        }
    }
}
