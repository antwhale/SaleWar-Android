package io.github.antwhale.salewar.ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.antwhale.salewar.ui.theme.unselectedCategoryBG
import io.github.antwhale.salewar.ui.theme.unselectedCategoryTextColor

@Composable
fun CategoryList(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues( vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 선택 상태에 따른 색상 애니메이션
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else unselectedCategoryBG,
        label = "backgroundColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.Black else unselectedCategoryTextColor,
        label = "contentColor"
    )

    Surface(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current, // 명시적으로 현재 인디케이션 전달
                onClick = onClick
            )
            .clip(CircleShape),
        color = backgroundColor,
        contentColor = contentColor,
        shape = CircleShape,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Text(
            text = category,
            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}