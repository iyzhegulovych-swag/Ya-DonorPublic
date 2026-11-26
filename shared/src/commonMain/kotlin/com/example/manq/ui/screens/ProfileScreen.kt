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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.manq.data.BloodGroup
import com.example.manq.data.Donation
import com.example.manq.data.RhFactor
import com.example.manq.data.SimpleDate
import com.example.manq.data.UserProfile
import com.example.manq.data.today
import com.example.manq.ui.components.AppIcons
import com.example.manq.ui.components.InfoNote
import com.example.manq.ui.components.Picker
import com.example.manq.ui.components.ScreenTitle

@Composable
fun ProfileScreen(
    store: AppStore,
    repository: BloodCentersRepository,
    onOpenCalendar: () -> Unit = {},
) {
    var editing by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { ScreenTitle("Профіль", "Твій шлях до донорства") }

        item { Avatar(store.profile) }

        if (editing) {
            item {
                ProfileEditor(
                    profile = store.profile,
                    onCancel = { editing = false },
                    onSave = {
                        store.updateProfile(it)
                        editing = false
                    },
                )
            }
        } else {
            item {
                TextButton(
                    onClick = { editing = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(AppIcons.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("  Редагувати профіль")
                }
            }
            item { ProfileFacts(store.profile) }
        }

        item { Stats(completed = store.completedDonations.size, favorites = store.favorites.size) }

        item {
            Text(
                "Найближчий план",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            val next = store.nextPlanned()
            if (next == null) {
                InfoNote("Новий візит можна запланувати в розділі «Календар» або на сторінці обраного центру.")
            } else {
                NextPlanCard(
                    donation = next,
                    centerName = repository.centerById(next.centerId)?.name.orEmpty(),
                    onOpen = onOpenCalendar,
                )
            }
        }

        item {
            InfoNote("Профіль і плани зберігаються на цьому пристрої. Позначайте донацію завершеною після фактичного візиту.")
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ClearDataButton(onClear = { store.clearEverything() })
            }
        }
    }
}

@Composable
private fun Avatar(profile: UserProfile) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Text(
                text = profile.initial,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = profile.displayName ?: "Донор",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        if (profile.email.isNotBlank()) {
            Text(
                text = profile.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ProfileFacts(profile: UserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(vertical = 4.dp)) {
            Fact("ПІБ", profile.fullName ?: "Не вказане")
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            Fact("Група крові", profile.bloodGroup.title)
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            Fact("Резус-фактор", profile.rhFactor.title)
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            Fact(
                label = "Дата народження",
                value = SimpleDate.parse(profile.birthDate)?.formatted() ?: "Не вказана",
                hint = profile.ageOn(today())?.let { age ->
                    if (age < 18) "Донором можна стати з 18 років" else null
                },
            )
        }
    }
}

@Composable
private fun Fact(label: String, value: String, hint: String? = null) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (hint != null) {
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun ProfileEditor(
    profile: UserProfile,
    onCancel: () -> Unit,
    onSave: (UserProfile) -> Unit,
) {
    var surname by remember { mutableStateOf(profile.surname) }
    var name by remember { mutableStateOf(profile.name) }
    var patronymic by remember { mutableStateOf(profile.patronymic) }
    var email by remember { mutableStateOf(profile.email) }
    var birth by remember { mutableStateOf(profile.birthDate) }
    var group by remember { mutableStateOf(profile.bloodGroup) }
    var rh by remember { mutableStateOf(profile.rhFactor) }

    val birthValid = birth.isBlank() || SimpleDate.parse(birth) != null

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Прізвище") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ім'я") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = patronymic,
                onValueChange = { patronymic = it },
                label = { Text("По батькові") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Пошта") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = birth,
                onValueChange = { birth = it },
                label = { Text("Дата народження") },
                placeholder = { Text("12.11.2008") },
                isError = !birthValid,
                supportingText = if (!birthValid) {
                    { Text("Формат: 12.11.2008") }
                } else {
                    null
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Picker(
                label = "Група крові",
                value = group,
                options = BloodGroup.entries,
                titleOf = { it.title },
                onSelect = { group = it },
            )
            Picker(
                label = "Резус-фактор",
                value = rh,
                options = RhFactor.entries,
                titleOf = { it.title },
                onSelect = { rh = it },
            )
            Text(
                "Група крові потрібна, щоб показувати, де саме твоя кров потрібна найбільше. Якщо не знаєш — її визначать безкоштовно при першій донації.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        onSave(
                            profile.copy(
                                name = name.trim(),
                                surname = surname.trim(),
                                patronymic = patronymic.trim(),
                                email = email.trim(),
                                birthDate = birth.trim(),
                                bloodGroup = group,
                                rhFactor = rh,
                            ),
                        )
                    },
                    enabled = birthValid,
                    modifier = Modifier.weight(1f),
                ) { Text("Зберегти") }
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Скасувати")
                }
            }
        }
    }
}

@Composable
private fun Stats(completed: Int, favorites: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard("Завершені донації", completed, highlighted = true, modifier = Modifier.weight(1f))
        StatCard("Обрані центри", favorites, highlighted = false, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(label: String, value: Int, highlighted: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (highlighted) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun NextPlanCard(donation: Donation, centerName: String, onOpen: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clickable(onClick = onOpen),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                AppIcons.Calendar,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = donation.day?.formatted().orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = centerName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ClearDataButton(onClear: () -> Unit) {
    var confirming by remember { mutableStateOf(false) }

    if (!confirming) {
        OutlinedButton(
            onClick = { confirming = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            Icon(AppIcons.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
            Text("  Очистити профіль")
        }
    } else {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    "Профіль, обране та історія донацій зберігаються лише на цьому пристрої. Після очищення відновити їх буде неможливо.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            onClear()
                            confirming = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                        ),
                        modifier = Modifier.weight(1f),
                    ) { Text("Очистити") }
                    OutlinedButton(
                        onClick = { confirming = false },
                        modifier = Modifier.weight(1f),
                    ) { Text("Скасувати") }
                }
            }
        }
    }
}
