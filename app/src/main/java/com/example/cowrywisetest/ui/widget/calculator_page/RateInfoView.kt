package com.example.cowrywisetest.ui.widget.calculator_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cowrywisetest.R
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette

@Composable
fun ColumnScope.RateInfoView(accentColor: Color) {
    Row(
        Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
    ) {
        Spacer(Modifier.weight(1f))
        Text(
            "Mid-market exchange rate at 13:38 UTC",
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Default,
                color = accentColor,
                fontWeight = FontWeight.W500,
                letterSpacing = 0.5.sp, fontSize = 15.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                //                        .fillMaxWidth()
                .padding(end = 12.dp)
                .drawBehind {
                    val strokeWidthPx = 1.dp.toPx()
                    val verticalOffset = size.height - 2.sp.toPx()
                    drawLine(
                        color = accentColor,
                        strokeWidth = strokeWidthPx,
                        start = Offset(0f, verticalOffset),
                        end = Offset(size.width, verticalOffset)
                    )
                }
        )
        Image(
            painter = painterResource(R.drawable.ic_info),
            contentDescription = "info",
            alignment = Alignment.TopStart,
            modifier = Modifier
                .weight(1f)
                .size(22.dp),
            contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(
                CowryWiseCustomColorsPalette.hintColor
            )
        )
//            Spacer(Modifier
//                .weight(1f)
//            )

    }
}
