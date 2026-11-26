package com.example.manq.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.manq.data.BloodCentersRepository
import com.example.manq.data.BloodComponent
import com.example.manq.data.BloodType
import com.example.manq.data.Center
import com.example.manq.data.DonorDay
import com.example.manq.data.NeedsSnapshot
import com.example.manq.data.today
import com.example.manq.platform.rememberPlatformActions
import com.example.manq.ui.components.AppIcons
import com.example.manq.ui.components.DonorDayCard
import com.example.manq.ui.components.NeedsBlock

@Composable
fun CenterDetailsScreen(
    center: Center?,
    location: BloodCentersRepository.Location?,
    needs: NeedsSnapshot? = null,
    donorDays: List<DonorDay> = emptyList(),
    userBloodType: BloodType? = null,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onPlanVisit: () -> Unit = {},
    onBack: () -> Unit,
) {
    if (center == null) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            BackRow(onBack)
            Text("Центр не знайдено", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    val actions = rememberPlatformActions()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { BackRow(onBack) }

        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = center.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (location != null) {
                    Text(
                        text = "${location.city}, ${location.oblast}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = onPlanVisit,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f),
                ) { Text("Запланувати візит", fontWeight = FontWeight.Bold) }

                OutlinedButton(
                    onClick = onToggleFavorite,
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(
                        if (isFavorite) AppIcons.Heart else AppIcons.HeartOutline,
                        contentDescription = if (isFavorite) "Прибрати з обраного" else "Додати в обране",
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BloodComponent.entries.forEach { component ->
                    ComponentBadge(
                        title = component.title,
                        accepted = component.acceptedBy(center),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(Modifier.padding(vertical = 4.dp)) {
                    if (center.has(center.phone)) {
                        ContactRow(
                            icon = AppIcons.Phone,
                            title = center.phone,
                            hint = "Відкрити набирач",
                            onClick = { actions.dial(center.phone) },
                        )
                    }
                    ContactRow(
                        icon = AppIcons.Place,
                        title = center.address,
                        hint = "Показати на карті",
                        onClick = { actions.openMap(center.address) },
                    )
                    if (center.has(center.email)) {
                        ContactRow(
                            icon = AppIcons.Email,
                            title = center.email,
                            hint = "Написати листа",
                            onClick = { actions.sendEmail(center.email) },
                        )
                    }
                    center.websiteUrl?.let { url ->
                        ContactRow(
                            icon = AppIcons.Globe,
                            title = center.website,
                            hint = "Відкрити сайт",
                            onClick = { actions.openUrl(url) },
                        )
                    }
                    center.facebookUrl?.let { url ->
                        ContactRow(
                            icon = AppIcons.Globe,
                            title = center.facebook,
                            hint = "Відкрити Facebook",
                            onClick = { actions.openUrl(url) },
                        )
                    }
                }
            }
        }

        val centerNeeds = needs?.forCenter(center.id)
        if (centerNeeds != null) {
            item {
                NeedsBlock(
                    needs = centerNeeds,
                    snapshot = needs,
                    highlight = userBloodType,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }

        if (donorDays.isNotEmpty()) {
            val now = today()
            item {
                Text(
                    text = "Дні донора",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            items(donorDays, key = { it.id }) { donorDay ->
                DonorDayCard(
                    donorDay = donorDay,
                    now = now,
                    centerName = center.name,
                    onAddToCalendar = {
                        actions.addToCalendar(
                            title = donorDay.title,
                            details = listOf(donorDay.time.orEmpty(), donorDay.details)
                                .filter { it.isNotBlank() }
                                .joinToString("\n"),
                            location = center.address,
                            date = donorDay.day ?: now,
                        )
                    },
                    onOpenUrl = { actions.openUrl(donorDay.url) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }

        item {
            Text(
                text = "Графік роботи та умови прийому уточнюйте в центрі перед візитом.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
    }
}

@Composable
private fun BackRow(onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 12.dp, top = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onBack)
            .padding(horizontal = 8.dp, vertical = 8.dp),
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

@Composable
private fun ComponentBadge(title: String, accepted: Boolean, modifier: Modifier = Modifier) {
    val bg = if (accepted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val fg = if (accepted) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = fg,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = if (accepted) "приймають" else "не приймають",
            style = MaterialTheme.typography.labelSmall,
            color = fg,
        )
    }
}

@Composable
private fun ContactRow(
    icon: ImageVector,
    title: String,
    hint: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
