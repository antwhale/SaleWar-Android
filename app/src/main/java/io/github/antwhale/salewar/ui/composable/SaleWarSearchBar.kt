package io.github.antwhale.salewar.ui.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.antwhale.salewar.ui.theme.SaleWarTheme
import io.github.antwhale.salewar.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleWarSearchBar(
    modifier : Modifier,
    onTextChanged : (String) -> Unit,
    searchKeyword: String
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .background(Yellow, RoundedCornerShape(30.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.padding(start = 10.dp)
        )

        TextField(
            value = searchKeyword,
            onValueChange = { newText ->
                onTextChanged.invoke(newText)
            },
            placeholder = { Text("Search", color = Color.White) },
            modifier = Modifier
                .weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,

                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,

                focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = Color.White.copy(alpha = 0.7f)

                ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun SaleWarSearchBarPreview() {
    SaleWarTheme {
        SaleWarSearchBar(Modifier, {}, "하나 둘 셋")
    }
}