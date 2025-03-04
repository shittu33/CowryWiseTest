package com.example.cowrywisetest.ui.widget.calculator_page

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cowrywisetest.MainActivity
import com.example.cowrywisetest.R
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette

@Composable
fun MainActivity.CowryWiseAppBar() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.ic_menu),
            contentDescription = "image menu",
            Modifier
                .size(45.dp)
                .clickable(role = Role.Button) {
                    Toast.makeText(this@CowryWiseAppBar, "Coming soon...", Toast.LENGTH_SHORT)
                        .show()
                }
        )
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = {
                Toast.makeText(this@CowryWiseAppBar,"Coming soon...",Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = CowryWiseCustomColorsPalette.successButtonColor1
            )
        ) {
            Text(
                "Sign up",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W600,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}