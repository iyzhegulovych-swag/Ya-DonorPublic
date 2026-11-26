package com.example.manq.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.manq.data.BloodCentersRepository
import com.example.manq.data.BloodComponent
import com.example.manq.data.BloodType
import com.example.manq.data.Center
import com.example.manq.data.NeedsSnapshot
import com.example.manq.ui.components.AppIcons
import com.example.manq.ui.components.HighNeedBadge
import com.example.manq.ui.components.Picker
import com.example.manq.ui.components.ScreenTitle
import com.example.manq.ui.components.YourBloodNeededBanner

private const val ANY_OBLAST = "Усі області"
private const val ANY_CITY = "Усі міста"

@Composable
fun CentersScreen(
    repository: BloodCentersRepository,
    favorites: Set<String> = emptySet(),
    needs: NeedsSnapshot? = null,
    userBloodType: BloodType? = null,
    onToggleFavorite: (Center) -> Unit = {},
    onOpenCenter: (Center) -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    var oblast by remember { mutableStateOf(ANY_OBLAST) }
    var city by remember { mutableStateOf(ANY_CITY) }
    var components by remember { mutableStateOf(emptySet<BloodComponent>()) }
    var onlyFavorites by remember { mutableStateOf(false) }

    val cities = remember(oblast) {
        repository.cityNames(oblast.takeIf { it != ANY_OBLAST })
    }
    val results = repository.search(
        query = query,
        oblast = oblast.takeIf { it != ANY_OBLAST },
        city = city.takeIf { it != ANY_CITY },
        components = components,
        onlyIds = if (onlyFavorites) favorites else null,
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenTitle(
                title = "Центри крові",
                subtitle = "Знайдіть заклад у зручному для вас місті",
            )
        }

        item { HelpNearbyCard() }

        // Персональний банер з'являється лише коли додаток знає групу
        // крові донора і десь справді є висока потреба саме в ній.
        if (needs != null && userBloodType != null) {
            val urgent = needs.centersNeeding(userBloodType)
            if (urgent.isNotEmpty()) {
                item { YourBloodNeededBanner(userBloodType, urgent.size) }
            }
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Місто, заклад або адреса") },
                leadingIcon = { Icon(AppIcons.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Picker(
                    label = "Область",
                    value = oblast,
                    options = listOf(ANY_OBLAST) + repository.oblastNames(),
                    titleOf = { it },
                    onSelect = {
                        oblast = it
                        city = ANY_CITY
                    },
                    modifier = Modifier.weight(1f),
                )
                Picker(
                    label = "Місто",
                    value = city,
                    options = listOf(ANY_CITY) + cities,
                    titleOf = { it },
                    onSelect = { city = it },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = onlyFavorites,
                    onClick = { onlyFavorites = !onlyFavorites },
                    label = { Text("Обране") },
                    leadingIcon = {
                        Icon(
                            if (onlyFavorites) AppIcons.Heart else AppIcons.HeartOutline,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                    },
                )
                BloodComponent.entries.forEach { component ->
                    FilterChip(
                        selected = component in components,
                        onClick = {
                            components = if (component in components) {
                                components - component
                            } else {
                                components + component
                            }
                        },
                        label = { Text(component.title) },
                    )
                }
            }
        }

        item {
            Text(
                text = "Знайдено центрів: ${results.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        if (results.isEmpty()) {
            item { EmptyResults() }
        }

        items(results, key = { it.id }) { center ->
            CenterCard(
                center = center,
                city = repository.locationOf(center)?.city.orEmpty(),
                highNeed = needs?.hasHighNeed(center.id) == true,
                isFavorite = center.id in favorites,
                onToggleFavorite = { onToggleFavorite(center) },
                onClick = { onOpenCenter(center) },
            )
        }
    }
}

@Composable
private fun HelpNearbyCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                AppIcons.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(32.dp),
            )
            Column {
                Text(
                    "Допомога починається поруч",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    "Оберіть центр і заплануйте візит",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
private fun CenterCard(
    center: Center,
    city: String,
    highNeed: Boolean,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = center.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = city,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(
                    imageVector = if (isFavorite) AppIcons.Heart else AppIcons.HeartOutline,
                    contentDescription = if (isFavorite) "Прибрати з обраного" else "Додати в обране",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable(onClick = onToggleFavorite)
                        .padding(4.dp)
                        .size(22.dp),
                )
            }

            Text(
                text = center.address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )

            if (highNeed) {
                HighNeedBadge(modifier = Modifier.padding(top = 10.dp))
            }

            val accepted = BloodComponent.entries.filter { it.acceptedBy(center) }
            if (accepted.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    accepted.forEach { ComponentTag(it.title) }
                }
            }
        }
    }
}

@Composable
private fun ComponentTag(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
private fun EmptyResults() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            "Нічого не знайшлося",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            "Спробуйте прибрати частину фільтрів або змінити запит",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
