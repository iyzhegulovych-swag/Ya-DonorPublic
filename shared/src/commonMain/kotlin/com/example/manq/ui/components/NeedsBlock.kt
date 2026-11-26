package com.example.manq.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.manq.data.BloodType
import com.example.manq.data.CenterNeeds
import com.example.manq.data.NeedLevel
import com.example.manq.data.NeedsSnapshot
import com.example.manq.ui.theme.NeedColors

fun colorOf(level: NeedLevel): Color = when (level) {
    NeedLevel.HIGH -> NeedColors.high
    NeedLevel.MEDIUM -> NeedColors.medium
    NeedLevel.LOW -> NeedColors.low
}

/** Потреба у крові по восьми групах. Рівень показано і кольором, і словом. */
@Composable
fun NeedsBlock(
    needs: CenterNeeds,
    snapshot: NeedsSnapshot,
    highlight: BloodType?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Потреба у крові",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        BloodType.entries.forEach { type ->
            val level = needs.needs[type] ?: return@forEach
            NeedRow(type = type, level = level, highlighted = type == highlight)
        }

        val date = snapshot.date?.formatted()
        Text(
            text = buildString {
                append("Дані donor.utcc.gov.ua")
                if (date != null) append(" станом на $date")
                if (snapshot.isStale()) append(" — можуть бути застарілі")
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun NeedRow(type: BloodType, level: NeedLevel, highlighted: Boolean) {
    val accent = colorOf(level)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (highlighted) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
            )
            .then(
                if (highlighted) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp),
                    )
                } else {
                    Modifier
                },
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(accent))
        Text(
            text = type.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (highlighted) {
            Text(
                text = "твоя",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = level.title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = accent,
        )
    }
}

/** Позначка «висока потреба» для карточки в списку. */
@Composable
fun HighNeedBadge(text: String = "Висока потреба", modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(NeedColors.high.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(NeedColors.high))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = NeedColors.high,
        )
    }
}

/** Банер на вкладці «Центри», якщо група крові донора відома. */
@Composable
fun YourBloodNeededBanner(
    type: BloodType,
    centersCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(NeedColors.high.copy(alpha = 0.10f))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BloodDrop(Modifier.size(20.dp, 24.dp), color = NeedColors.high)
        Column {
            Text(
                text = "Твоя група ${type.title} потрібна терміново",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Висока потреба у $centersCount " +
                    when {
                        centersCount % 10 == 1 && centersCount % 100 != 11 -> "центрі"
                        else -> "центрах"
                    },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
