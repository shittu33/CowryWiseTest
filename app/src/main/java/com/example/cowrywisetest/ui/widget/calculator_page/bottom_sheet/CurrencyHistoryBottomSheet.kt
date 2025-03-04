package com.example.cowrywisetest.ui.widget.calculator_page.bottom_sheet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.cowrywisetest.MainActivity
import com.example.cowrywisetest.ui.theme.CowryWiseCustomColorsPalette
import com.example.cowrywisetest.ui.theme.CowryWiseTestTheme
import java.util.Calendar

val containerColor = Color(0xFF175FC7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivity.CurrencyHistoryBottomSheet(sheetState: SheetState, onDismiss: () -> Unit) {


    CowryWiseTestTheme {
        ModalBottomSheet(
            modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            ),
            scrimColor = Color.Black.copy(alpha = .2f),
            onDismissRequest = onDismiss,
            dragHandle = {},
            windowInsets = BottomSheetDefaults.windowInsets,
//                properties = ModalBottomSheetProperties(),
        ) {
            Column(
                modifier = Modifier
                    .padding(
//                        horizontal = 24.dp,
                    )
                    .padding(top = 18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(30.dp))

                Row(
                    Modifier.padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Past 10 days",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.W400,
                                letterSpacing = 0.5.sp,
                                color = Color.White
                            )
                        )
                        Spacer(Modifier.height(10.dp))
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .background(
                                    CowryWiseCustomColorsPalette.successButtonColor1,
                                    shape = CircleShape
                                )
                                .size(8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        "Past 5 days",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W400,
                            letterSpacing = 0.5.sp,
                            color = CowryWiseCustomColorsPalette.grey.copy(alpha = 0.7f)
                        )
                    )
                    Spacer(Modifier.weight(1f))
                }

                Spacer(Modifier.height(50.dp))
//                Spacer(Modifier.weight(0.5f))
                CurrencyHistoryGraphView()
                Spacer(Modifier.height(50.dp))
                val greyColor = CowryWiseCustomColorsPalette.grey
                Text(
                    "Get rate alerts straight to your email inbox",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Default,
                        color = greyColor,
                        fontWeight = FontWeight.W500,
                        letterSpacing = 0.5.sp, fontSize = 15.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        //                        .fillMaxWidth()
                        .padding(end = 12.dp)
                        .align(Alignment.CenterHorizontally)
                        .drawBehind {
                            val strokeWidthPx = 1.dp.toPx()
                            val verticalOffset = size.height - 2.sp.toPx()
                            drawLine(
                                color = greyColor,
                                strokeWidth = strokeWidthPx,
                                start = Offset(0f, verticalOffset),
                                end = Offset(size.width, verticalOffset)
                            )
                        }
                )
                Spacer(Modifier.height(50.dp))

            }
        }

    }
}

@Composable
private fun MainActivity.CurrencyHistoryGraphView() {

//    val currencyHistoryData = viewModel.historicalRatesFlow.collectAsState()

//    var historicalRates by remember { mutableStateOf<List<Map.Entry<String,Double>>?>(emptyList()) }

//    var pointsData: List<Point> by remember { mutableStateOf<List<Point>>(emptyList()) }
    var pointsData: List<Point> = listOf(Point(1f, 40f), Point(2f, 60f), Point(3f, 50f), Point(4f, 40f), Point(5f, 30f))

//    LaunchedEffect(currencyHistoryData) {
//        historicalRates = currencyHistoryData.value?.entries?.toList()
//        pointsData= historicalRates?.mapIndexed { index, entry ->
//            Point(
//                (index + 1).toFloat(),
//                entry.value.toFloat()
//            )
//        }?.toList() ?: emptyList()
//
//        Log.d("CurrencyHistoryBottomSheet","pointsData" +pointsData.map { it.toString() })
//    }

//    if(pointsData.isEmpty()){
//        return Box {  }
//    }

    val steps = pointsData.size - 1

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(pointsData.size - 1)
//        .labelData { i -> "${historicalRates?.get(i-1)?.key}" }
        .labelData { i -> "Mar $i" }
        .axisLineColor(Color.White)
        .axisLabelColor(Color.White)
        .axisLineThickness(0.dp)
        .backgroundColor(containerColor)
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .backgroundColor(containerColor)
        .axisLineColor(Color.White)
        .axisLabelColor(Color.White)
        .axisLineThickness(0.dp)
        .labelData { i ->
            " "
        }.build()
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = containerColor)
            .height(300.dp),
        lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = pointsData,
                        LineStyle(width = 0.2f),
                        IntersectionPoint(
                            color = CowryWiseCustomColorsPalette.successButtonColor1, radius = 6.dp
                        ),
                        SelectionHighlightPoint(
                            isHighlightLineRequired = false,
                            color = Color.White
                        ),
                        ShadowUnderLine(color = Color.White.copy(alpha = 0.9f)),
                        SelectionHighlightPopUp(
                            backgroundColor = CowryWiseCustomColorsPalette.successButtonColor1,
                            labelColor = Color.White, backgroundCornerRadius = CornerRadius(8f, 8f),
                            labelSize = 14.sp, popUpLabel = { x, y -> "\n\tJan 1\t\n" }
                        )
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(enableHorizontalLines = false, enableVerticalLines = false),
            backgroundColor = containerColor,
            paddingRight = 0.dp
        )
    )
}