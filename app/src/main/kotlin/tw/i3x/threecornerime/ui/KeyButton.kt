package tw.i3x.threecornerime.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: Int = 22,
) {
    Box(
        modifier = modifier
            .padding(2.dp)
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * A key button that shows two characters.
 * Tap → outputs the first character, long press → outputs the second character.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DualKeyButton(
    topLabel: String,
    bottomLabel: String,
    onTap: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Box(
        modifier = modifier
            .padding(2.dp)
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = onTap,
                onLongClick = onLongPress
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = topLabel,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )
            Text(
                text = bottomLabel,
                color = textColor.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 14.sp
            )
        }
    }
}
