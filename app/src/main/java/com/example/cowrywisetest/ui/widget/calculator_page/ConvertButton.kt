package com.example.cowrywisetest.ui.widget.calculator_page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette
import com.example.cowrywisetest.ui.theme.LightWhiteColor
import com.example.cowrywisetest.utils.NetworkResult

@Composable
fun ConvertButton(
    populateDbProgress: NetworkResult<String>?,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        enabled = populateDbProgress is NetworkResult.Success,
        colors = ButtonDefaults.buttonColors(containerColor = CowryWiseCustomColorsPalette.successButtonColor1),
        onClick = onClick
    ) {
        Text(
            "Convert",
            style = MaterialTheme.typography.titleMedium.copy(color = LightWhiteColor)
        )
    }
}
