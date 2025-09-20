package io.github.antwhale.salewar.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    modifier : Modifier
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
            value = "",
            onValueChange = { newText ->
//                searchText.value = newText
            },
            placeholder = { Text("Search", color = Color.White) },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp, horizontal = 10.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
//                textColor = Color.White,
                cursorColor = Color.White
            ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun SaleWarSearchBarPreview() {
    SaleWarTheme {
        SaleWarSearchBar(Modifier)
    }
}