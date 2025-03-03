package com.example.cowrywisetest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette
import com.example.cowrywisetest.ui.theme.CowryWiseTestTheme
import com.example.cowrywisetest.ui.theme.LightWhiteColor
import com.example.cowrywisetest.ui.widget.calculator_page.CowryWiseAppBar
import com.example.cowrywisetest.ui.widget.calculator_page.BaseCurrencyTextInputField
import com.example.cowrywisetest.ui.widget.calculator_page.ConvertButton
import com.example.cowrywisetest.ui.widget.calculator_page.CurrencyConverterTittleView
import com.example.cowrywisetest.ui.widget.calculator_page.CurrencySelectorView
import com.example.cowrywisetest.ui.widget.calculator_page.RateInfoView
import com.example.cowrywisetest.ui.widget.calculator_page.TargetCurrencyInputField
import com.example.cowrywisetest.utils.CurrencyConverter
import com.example.cowrywisetest.utils.NetworkResult
import com.example.cowrywisetest.viewModel.CurrencyConverterViewModel
import dagger.hilt.android.AndroidEntryPoint

const val DEFAULT_BASE_CURRENCY = "EUR"
const val DEFAULT_TARGET_CURRENCY = "USD"

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    val viewModel by viewModels<CurrencyConverterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CowryWiseTestTheme {
                MainScreen()
            }
        }
    }


    @Composable
    fun MainScreen() {

        var populateDbProgress by remember { mutableStateOf<NetworkResult<String>?>(null) }

        var baseAmountInputText by remember { mutableStateOf("") }
        var targetAmountInputText by remember { mutableStateOf("") }


        LaunchedEffect(Unit) {
            viewModel.populateLatestRatesToDb()
        }

        LaunchedEffect(Unit) {
            viewModel.populateRatesDbLiveData.observe(this@MainActivity) { populateRatesDb ->
                populateRatesDb?.getContentIfNotHandled().let {
                    populateDbProgress = it
                    if (it is NetworkResult.Error) {
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        val accentColor = CowryWiseCustomColorsPalette.accentColor
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentColor = CowryWiseCustomColorsPalette.bgColor
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(43.dp))
                CowryWiseAppBar()
                Spacer(Modifier.height(65.dp))
                CurrencyConverterTittleView()
                Spacer(Modifier.height(55.dp))
                BaseCurrencyTextInputField {
                    baseAmountInputText = it
                }
                Spacer(Modifier.height(15.dp))
                TargetCurrencyInputField {
                    targetAmountInputText = it
                }
                Spacer(Modifier.height(35.dp))
                CurrencySelectorView(onBaseCurrencySelected = {
//                    baseCurrencySymbol = it
                }, onTargetCurrencySelected = {
//                    targetCurrencySymbol = it
                })
                Spacer(Modifier.height(35.dp))
                ConvertButton(populateDbProgress, onClick = {
                    val amount = baseAmountInputText.toDoubleOrNull()
                    if (amount != null) {
                        viewModel.convertCurrency(amount)
                    }
                })
                Spacer(Modifier.height(35.dp))
                RateInfoView(accentColor)

            }
            if (populateDbProgress is NetworkResult.Loading) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }
            } else {
                Box { }
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        CowryWiseTestTheme {
            MainScreen()
        }
    }
}
