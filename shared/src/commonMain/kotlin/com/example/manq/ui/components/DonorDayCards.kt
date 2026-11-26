package com.example.manq.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.manq.data.AnnualEvent
import com.example.manq.data.DonorDay
import com.example.manq.data.SimpleDate

/** «сьогодні», «завтра», «через 5 дн.»; для минулих дат — null. */
private fun countdown(now: SimpleDate, date: SimpleDate): String? = when (val days = now.daysUntil(date)) {
    0 -> "сьогодні"
    1 -> "завтра"
    in 2..365 -> "через $days дн."
    else -> null
}

@Composable
private fun DateLine(date: SimpleDate?, now: SimpleDate) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = date?.let { "${it.day} ${it.monthName()} ${it.year}" } ?: "Дата невідома",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        date?.let { countdown(now, it) }?.let { left ->
            Text(
                text = left,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

/** Один день донора. [centerName] передає екран: у файлі лежить тільки id. */
@Composable
fun DonorDayCard(
    donorDay: DonorDay,
    now: SimpleDate,
    centerName: String = "",
    onAddToCalendar: () -> Unit,
    onOpenUrl: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val where = centerName.ifBlank { donorDay.place }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            DateLine(donorDay.day, now)

            Text(
                text = donorDay.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (where.isNotBlank()) {
                Text(
                    text = where,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            donorDay.time?.let { time ->
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            if (donorDay.details.isNotBlank()) {
                Text(
                    text = donorDay.details,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onAddToCalendar) {
                    Icon(
                        AppIcons.Calendar,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Text("  У календар")
                }
                if (onOpenUrl != null && donorDay.url.isNotBlank()) {
                    TextButton(onClick = onOpenUrl) { Text("Детальніше") }
                }
            }
        }
    }
}

/** Постійна дата календаря, наприклад Всесвітній день донора крові. */
@Composable
fun AnnualEventCard(
    event: AnnualEvent,
    now: SimpleDate,
    onAddToCalendar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            val date = event.next(now)
            DateLine(date, now)

            Text(
                text = event.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (event.details.isNotBlank()) {
                Text(
                    text = event.details,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            TextButton(onClick = onAddToCalendar) {
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
