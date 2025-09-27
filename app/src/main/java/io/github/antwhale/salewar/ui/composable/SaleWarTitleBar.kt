package io.github.antwhale.salewar.ui.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.antwhale.salewar.ui.theme.Orange
import io.github.antwhale.salewar.ui.theme.SaleWarTheme

@Composable
fun SaleWarTitleBar(modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Text(
            style = TextStyle(
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            text = "세일 전쟁")

        Button(
            onClick = {
                // This is where you would place your click logic, like a log statement or a function call.
                // For example:
                 Log.d("SaleWarTitleBar", "click favorite icon")
                // onClickFavoriteMenu()
            },
            modifier = Modifier
                .padding(end = 16.dp)
                .align(Alignment.CenterEnd)
                .size(30.dp) // Sets the frame size of the button
                .background(Color.Unspecified) // We set the background inside the Button's content to clip correctly.
                .clip(CircleShape), // Clips the button to a circle.
            // We set the colors of the button to transparent to let our custom background handle the color.
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                contentColor = Color.White
            ),
            // No padding on the button itself so the icon fills the space.
            contentPadding = PaddingValues(0.dp)
        ) {
            // The Icon composable is the equivalent of SwiftUI's Image.
            Icon(
                imageVector = Icons.Filled.Favorite, // Equivalent of "heart.fill" from SF Symbols.
                contentDescription = "Favorite",
                modifier = Modifier.size(15.dp) // Sets the icon's size, equivalent to frame.
                // The tint parameter is used to change the icon color, equivalent to foregroundColor.
            )

        }


    }
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun GreetingPreview() {
    SaleWarTheme {
        SaleWarTitleBar(Modifier.fillMaxWidth())
    }
}