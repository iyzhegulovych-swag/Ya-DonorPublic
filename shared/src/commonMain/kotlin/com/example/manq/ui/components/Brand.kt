package com.example.manq.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Крапля крові. Малюється кодом, щоб не тягнути растрові ресурси. */
@Composable
fun BloodDrop(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w / 2f, 0f)
            cubicTo(w * 0.92f, h * 0.38f, w, h * 0.58f, w, h * 0.66f)
            cubicTo(w, h * 0.88f, w * 0.78f, h, w / 2f, h)
            cubicTo(w * 0.22f, h, 0f, h * 0.88f, 0f, h * 0.66f)
            cubicTo(0f, h * 0.58f, w * 0.08f, h * 0.38f, w / 2f, 0f)
            close()
        }
        drawPath(path, color)
        drawCircle(
            color = Color.White.copy(alpha = 0.85f),
            radius = w * 0.12f,
            center = Offset(w * 0.38f, h * 0.62f),
        )
    }
}

/** Шапка з назвою додатку, однакова на всіх екранах. */
@Composable
fun BrandHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BloodDrop(Modifier.size(22.dp, 26.dp))
        Text(
            text = "ЯДонор",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

/** Заголовок екрану: велика назва і підпис під нею. */
@Composable
fun ScreenTitle(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.layout.Column(modifier.padding(horizontal = 20.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
