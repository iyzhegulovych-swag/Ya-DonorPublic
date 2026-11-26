package com.example.manq.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manq.ui.Screen

@Composable
private fun iconFor(tab: Screen.Tab): ImageVector = when (tab) {
    Screen.Tab.KNOWLEDGE -> AppIcons.Book
    Screen.Tab.CENTERS -> AppIcons.Place
    Screen.Tab.DONATION -> AppIcons.Add
    Screen.Tab.CALENDAR -> AppIcons.Calendar
    Screen.Tab.PROFILE -> AppIcons.Person
}

@Composable
fun BottomBar(
    current: Screen.Tab,
    onSelect: (Screen.Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Screen.Tab.entries.forEach { tab ->
                if (tab == Screen.Tab.DONATION) {
                    DonationButton(onClick = { onSelect(tab) })
                } else {
                    TabItem(
                        tab = tab,
                        selected = tab == current,
                        onClick = { onSelect(tab) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TabItem(tab: Screen.Tab, selected: Boolean, onClick: () -> Unit) {
    val tint = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        androidx.compose.ui.graphics.Color.Transparent
                    },
                )
                .padding(horizontal = 14.dp, vertical = 4.dp),
        ) {
            Icon(iconFor(tab), contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
        }
        Text(
            text = tab.title,
            fontSize = 11.sp,
            color = tint,
            textAlign = TextAlign.Center,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

@Composable
private fun DonationButton(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
                .clickable(onClick = onClick),
        ) {
            Icon(
                AppIcons.Add,
                contentDescription = Screen.Tab.DONATION.title,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(26.dp),
            )
        }
        Text(
            text = Screen.Tab.DONATION.title,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
