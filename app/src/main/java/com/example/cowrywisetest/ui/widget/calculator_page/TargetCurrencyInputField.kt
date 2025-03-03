package com.example.cowrywisetest.ui.widget.calculator_page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette
import com.example.cowrywisetest.ui.widget.CowryWiseTextInputField
import java.util.Currency

@Composable
fun TargetCurrencyInputField(
    currency: String,
    onValueChanged: (value: String) -> Unit
) {
    var targetInputText by remember { mutableStateOf("") }
    CowryWiseTextInputField(
        onValueChange = {
            targetInputText = it
            onValueChanged(it)
        },
        value = targetInputText,
        labelText = "0.0",
        labelTextColor = CowryWiseCustomColorsPalette.hintColor,
        trailingContent = {
            Text(
                currency,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.Default,
                    color = CowryWiseCustomColorsPalette.hintColor,
                    fontWeight = FontWeight.W600,
                    letterSpacing = 0.5.sp, fontSize = 22.sp
                ),
                modifier = Modifier.padding(end = 12.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        keyboardType = KeyboardType.Number,
        containerColor = CowryWiseCustomColorsPalette.textInputContainerColor,
        borderShape = RoundedCornerShape(6.dp),
        indicatorColor = CowryWiseCustomColorsPalette.grey.copy(alpha = 0f),
        borderWidth = 0.dp,
        columnModifier = Modifier
            .height(70.dp)
            .fillMaxWidth(),
    )
}