package com.example.manq.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.manq.data.AppStore
import com.example.manq.data.BloodCentersRepository
import com.example.manq.data.BloodComponent
import com.example.manq.data.Center
import com.example.manq.data.Donation
import com.example.manq.data.SimpleDate
import com.example.manq.data.today
import com.example.manq.platform.rememberReminders
import com.example.manq.ui.components.AppIcons
import com.example.manq.ui.components.InfoNote
import com.example.manq.ui.components.ScreenTitle
import kotlin.random.Random

@Composable
fun PlanVisitScreen(
    store: AppStore,
    repository: BloodCentersRepository,
    preselectedCenterId: String?,
    onDone: () -> Unit,
    onBack: () -> Unit,
) {
    val now = remember { today() }
    val reminders = rememberReminders()

    var centerId by remember { mutableStateOf(preselectedCenterId) }
    var search by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf(now.plusDays(7).formatted()) }
    var component by remember { mutableStateOf(BloodComponent.WHOLE_BLOOD) }

    val chosen = centerId?.let { repository.centerById(it) }
    val date = SimpleDate.parse(dateText)
    val dateInPast = date != null && date < now
    val canSave = chosen != null && date != null && !dateInPast

    // Поки центр не обрано, показуємо обране, а далі — результати пошуку.
    val suggestions = remember(search, store.favorites) {
        if (search.isBlank()) {
            repository.allCenters.filter { it.id in store.favorites }.take(10)
        } else {
            repository.search(query = search).take(15)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { BackLink(onBack) }
        item { ScreenTitle("Запланувати візит", "Збережемо план у твоєму календарі") }

        item {
            Text(
                "1. Центр крові",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        if (chosen != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                chosen.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Text(
                                chosen.address,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                        Icon(
                            AppIcons.Close,
                            contentDescription = "Обрати інший центр",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable { centerId = null }
                                .padding(6.dp)
                                .size(18.dp),
                        )
                    }
                }
            }
        } else {
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text("Назва центру або місто") },
                    leadingIcon = { Icon(AppIcons.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                )
            }

            if (suggestions.isEmpty()) {
                item {
                    InfoNote(
                        if (search.isBlank()) {
                            "Додай центр в обране на вкладці «Центри» — він з'явиться тут одразу."
                        } else {
                            "За цим запитом нічого не знайшлося."
                        },
                    )
                }
            }

            items(suggestions.size) { index ->
                val center = suggestions[index]
                SuggestionRow(
                    center = center,
                    city = repository.locationOf(center)?.city.orEmpty(),
                    onClick = { centerId = center.id },
                )
            }
        }

        item {
            Text(
                "2. Дата",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            OutlinedTextField(
                value = dateText,
                onValueChange = { dateText = it },
                label = { Text("День візиту") },
                placeholder = { Text("12.11.2026") },
                isError = date == null || dateInPast,
                supportingText = {
                    Text(
                        when {
                            date == null -> "Формат: 12.11.2026"
                            dateInPast -> "Дата вже минула"
                            else -> "Через ${now.daysUntil(date)} дн."
                        },
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf(7, 14, 30).forEach { days ->
                    FilterChip(
                        selected = date == now.plusDays(days),
                        onClick = { dateText = now.plusDays(days).formatted() },
                        label = { Text("+$days дн.") },
                    )
                }
            }
        }

        item {
            Text(
                "3. Що здаєш",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BloodComponent.entries.forEach { option ->
                    FilterChip(
                        selected = component == option,
                        onClick = { component = option },
                        label = { Text(option.title) },
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    val center = chosen ?: return@Button
                    val day = date ?: return@Button
                    val donation = Donation(
                        id = "${day.iso()}-${center.id}-${Random.nextInt(100_000)}",
                        centerId = center.id,
                        date = day.iso(),
                        component = component,
                    )
                    store.addDonation(donation)

                    // Нагадуємо напередодні — щоб людина встигла
                    // підготуватися: без ліків, алкоголю й важкої їжі.
                    reminders.schedule(
                        id = "visit-${donation.id}",
                        title = "Завтра донація",
                        text = "${center.name}. Не забудь поснідати зранку.",
                        date = day.plusDays(-1),
                    )
                    onDone()
                },
                enabled = canSave,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            ) {
                Text("Зберегти план", fontWeight = FontWeight.Bold)
            }
        }

        item {
            InfoNote("Додаток не бронює місце. Запис і час прийому підтверджуй у центрі крові — телефон є на його сторінці.")
        }
    }
}

@Composable
private fun SuggestionRow(center: Center, city: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                center.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                city,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun BackLink(onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 12.dp, top = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onBack)
            .padding(8.dp),
    ) {
        Icon(
            AppIcons.ArrowBack,
            contentDescription = "Назад",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp),
        )
        Text("Назад", color = MaterialTheme.colorScheme.primary)
    }
}
