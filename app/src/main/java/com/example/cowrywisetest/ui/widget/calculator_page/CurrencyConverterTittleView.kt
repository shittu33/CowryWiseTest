package com.example.cowrywisetest.ui.widget.calculator_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cowrywisetest.R
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette

@Composable
fun CurrencyConverterTittleView() {

    Row {
        Text(
            "Currency\nCalculator",
            style = MaterialTheme.typography.bodyLarge.copy(color = CowryWiseCustomColorsPalette.accentColor),
            modifier = Modifier.padding(end = 1.dp)
        )
        Box(
            Modifier
                .clip(CircleShape)
                .background(CowryWiseCustomColorsPalette.successButtonColor1, shape = CircleShape)
                .size(8.dp).align(BiasAlignment.Vertical(0.82f))
        )
    }
//    Image(
//        painter = painterResource(R.drawable.img_title),
//        contentDescription = "page title"
//    )
}