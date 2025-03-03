package com.example.cowrywisetest.ui.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CowryWiseTextInputField(
    modifier: Modifier? = null,
    inputModifier: Modifier? = null,
    columnModifier: Modifier? = null,
    titleText: String? = null,
    labelText: String? = null,
    textStyle: TextStyle? = null,
    value: String? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minimumLines: Int = 1,
    containerColor: Color = Color.Transparent,
    indicatorColor: Color = Color.Transparent,
    labelTextColor: Color = CowryWiseCustomColorsPalette.hintColor,
    focusIndicatorColor: Color = Color.Transparent,
    borderShape: RoundedCornerShape = RoundedCornerShape(8.dp),
    borderWidth: Dp? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {}
) {
    var inputText by rememberSaveable { mutableStateOf(value ?: "") }

    LaunchedEffect(value) {
        inputText = value ?: ""
    }
    Column(
        modifier = columnModifier ?: Modifier
            .fillMaxWidth()
    ) {
        if (titleText != null) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = CowryWiseCustomColorsPalette.grey,
                    fontSize = 12.sp,
//                    fontFamily = FontFamily(Font(R.font.albert_sans_medium)),
                ),
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
        Box(
            modifier = if (!enabled && indicatorColor != Color.Transparent)
                Modifier.border(
                    width = borderWidth ?: 1.dp,
                    color = indicatorColor,
                    shape = borderShape
                ) else {
                modifier ?: Modifier

            }
        ) {
            SelectionContainer {
                OutlinedTextField(
                    value = value ?: inputText,
                    onValueChange = {
                        inputText = it
                        onValueChange.invoke(it)
                    },

                    placeholder = {
                        Text(
                            labelText ?: "",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = labelTextColor,
                                lineHeight = 0.8.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.W600,
                                letterSpacing = 0.5.sp, fontSize = 18.sp
//                                fontFamily = FontFamily(Font(R.font.albert_sans_medium)),
                            ).merge(textStyle)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Next,
                    ),
                    minLines = minimumLines,
                    maxLines = maxLines,
                    singleLine = singleLine,
                    readOnly = readOnly,
                    enabled = enabled,
                    leadingIcon = leadingContent,
                    trailingIcon = trailingContent,
                    shape = borderShape,
                    textStyle = textStyle ?: MaterialTheme.typography.bodyLarge.copy(
                        color = CowryWiseCustomColorsPalette.onSurface,
                        lineHeight = 14.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.W600,
                        letterSpacing = 0.5.sp, fontSize = 18.sp
                    ),
                    colors = TextFieldDefaults.colors().copy(
                        focusedIndicatorColor = focusIndicatorColor,
                        unfocusedIndicatorColor = indicatorColor,
                        disabledIndicatorColor = indicatorColor,
                        focusedContainerColor = containerColor,
                        unfocusedContainerColor = containerColor,
                        disabledContainerColor = containerColor,
                        cursorColor = CowryWiseCustomColorsPalette.onSurface
                    ),
                    modifier = modifier?.then(inputModifier ?: Modifier) ?: Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding()
                        .defaultMinSize(minHeight = 48.dp)
                        .then(inputModifier ?: Modifier)
                    //                    .padding(0.96884.dp),
                )
            }
        }
    }
}
