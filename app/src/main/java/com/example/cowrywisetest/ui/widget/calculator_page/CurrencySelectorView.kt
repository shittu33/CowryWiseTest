package com.example.cowrywisetest.ui.widget.calculator_page

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.example.cowrywisetest.DEFAULT_BASE_CURRENCY
import com.example.cowrywisetest.DEFAULT_TARGET_CURRENCY
import com.example.cowrywisetest.MainActivity
import com.example.cowrywisetest.R
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette
import com.example.cowrywisetest.ui.theme.LightWhiteColor
import com.example.cowrywisetest.ui.widget.CowryWiseTextInputField
import com.example.cowrywisetest.utils.NetworkResult
import com.example.cowrywisetest.viewModel.CurrencyConverterViewModel
import com.mynameismidori.currencypicker.CurrencyPicker
import com.mynameismidori.currencypicker.ExtendedCurrency


@Composable
fun MainActivity.CurrencySelectorView(
    onBaseCurrencySelected: (String) -> Unit,
    onTargetCurrencySelected: (String) -> Unit,
) {
    var selectedBaseCurrency by remember {
        mutableStateOf<ExtendedCurrency>(
            ExtendedCurrency.getCurrencyByISO(
                DEFAULT_BASE_CURRENCY
            )
        )
    }
    var selectedTargetCurrency by remember {
        mutableStateOf<ExtendedCurrency>(
            ExtendedCurrency.getCurrencyByISO(
                DEFAULT_TARGET_CURRENCY
            )
        )
    }


    var supportedCurrencies by remember { mutableStateOf<List<ExtendedCurrency>>(emptyList()) }

    var showLoader by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.getCurrencySymbols()
    }

    LaunchedEffect(Unit) {
        viewModel.symbolsLiveData.observe(this@CurrencySelectorView) { currencySymbols ->
            currencySymbols?.getContentIfNotHandled().let { symbolsFromDb ->
                showLoader = symbolsFromDb is NetworkResult.Loading
                if (symbolsFromDb is NetworkResult.Success) {
                    supportedCurrencies = ExtendedCurrency.getAllCurrencies()
                        .filter { symbolsFromDb.data.symbols.containsKey(it.code) }
                } else if (symbolsFromDb is NetworkResult.Error) {
                    Toast.makeText(
                        this@CurrencySelectorView,
                        symbolsFromDb.toString(),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    LaunchedEffect(selectedBaseCurrency) {
        onBaseCurrencySelected(selectedBaseCurrency.code)
    }
    LaunchedEffect(selectedTargetCurrency) {
        onTargetCurrencySelected(selectedTargetCurrency.code)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        CowryWiseTextInputField(
            labelText = selectedBaseCurrency.code,
            readOnly = true,
            enabled = false,
            labelTextColor = CowryWiseCustomColorsPalette.hintColor,
            leadingContent = {
                Image(
                    painter = painterResource(selectedBaseCurrency.flag),
                    contentDescription = "flag",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
            },
            trailingContent = {
                Icon(
                    Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "arrow down",
                    tint = CowryWiseCustomColorsPalette.hintColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            containerColor = CowryWiseCustomColorsPalette.bgColor.copy(alpha = 0.5f),
            borderShape = RoundedCornerShape(8.dp),
            indicatorColor = CowryWiseCustomColorsPalette.textInputContainerColor,
            borderWidth = 2.dp,
            columnModifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .weight(0.3f)
                .clickable {
                    val picker = CurrencyPicker.newInstance("Select Currency") // dialog title
                    picker.setListener { _, code, _, _ ->
                        selectedBaseCurrency = ExtendedCurrency.getCurrencyByISO(code)
                        picker.dismiss()
                    }
                    picker.setCurrenciesList(supportedCurrencies)
                    picker.show(this@CurrencySelectorView.supportFragmentManager, "CURRENCY_PICKER")
                },
        )
        Spacer(Modifier.width(15.dp))
        Image(
            painter = painterResource(R.drawable.ic_exchange),
            contentDescription = "exchange icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(45.dp)
                .width(35.dp)
                .align(Alignment.Top)
        )
        Spacer(Modifier.width(15.dp))
        CowryWiseTextInputField(
            labelText = selectedTargetCurrency.code,
            readOnly = true,
            enabled = false,
            labelTextColor = CowryWiseCustomColorsPalette.hintColor,
            leadingContent = {
                Image(
                    painter = painterResource(selectedTargetCurrency.flag),
                    contentDescription = "flag",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
            },
            trailingContent = {
                Icon(
                    Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "arrow down",
                    tint = CowryWiseCustomColorsPalette.hintColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            containerColor = CowryWiseCustomColorsPalette.bgColor.copy(alpha = 0.5f),
            borderShape = RoundedCornerShape(8.dp),
            indicatorColor = CowryWiseCustomColorsPalette.textInputContainerColor,
            borderWidth = 2.dp,
            columnModifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .weight(0.3f)
                .clickable {
                    val picker = CurrencyPicker.newInstance("Select Currency") // dialog title
                    picker.setListener { _, code, _, _ ->
                        selectedTargetCurrency = ExtendedCurrency.getCurrencyByISO(code)
                        picker.dismiss()
                    }
                    picker.setCurrenciesList(supportedCurrencies)
                    picker.show(this@CurrencySelectorView.supportFragmentManager, "CURRENCY_PICKER")
                },
        )
    }
}