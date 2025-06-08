package com.example.jettipapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.utils.calculateTotalPerPerson
import com.example.jettipapp.utils.calculateTotalTip
import com.example.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column {
                    MainContent()
                }

            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        Surface(modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colorScheme.surface) {
            content()
        }

    }
}


//@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.00) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {

    val splitByState = remember { mutableIntStateOf(1) }
    val tipAmountState = remember { mutableDoubleStateOf(0.0) }
    val intRange = 1..100

    Column(modifier = Modifier.padding(all = 12.dp)) {
        BillForm(splitByState = splitByState,
            tipAmountState = tipAmountState,
            range = intRange)
    }


}

@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    range: IntRange
) {
    val totalBillState = remember { mutableStateOf("") }
    val validState = remember(totalBillState.value.trim()) {
        totalBillState.value.trim().isNotEmpty()
                && totalBillState.value.toDoubleOrNull() != null
    }
    val sliderPositionState = remember { mutableFloatStateOf(0f) }
    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()
    val tipPercentageState = remember { mutableIntStateOf(tipPercentage) }
    val errorMessageState = remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    TopHeader(calculateTotalPerPerson(
        totalBill = totalBillState.value.toDoubleOrNull() ?: 0.0,
        splitBy = splitByState.value,
        tipPercentage = tipPercentageState.intValue
    ))

    Surface(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    )
    {
        // Start: Main Card
        Column(
            modifier = modifier.padding(5.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,

            )
        {
            InputField(modifier = modifier.fillMaxWidth(),
                valueState = totalBillState,
                labelId = "Enter Bill Amount",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) {
                        return@KeyboardActions
                    }
//                    captureAmount(totalBillState.value.trim())
                    focusManager.clearFocus()
                })
            if (validState) { // Start: Split and Round Buttons

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Split", modifier = modifier
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(120.dp))
                Row(
                    modifier = modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                ) {
                    RoundIconButton(
                        imageVector = Icons.Default.Remove,
                        onClick = {
//                            if (splitValue.intValue > 1) splitValue.intValue--
                            if(splitByState.value > range.first) {
                                splitByState.value--
                            }
                        })

                    Text(
                        text = "${splitByState.value}",
                        modifier = modifier
                            .padding(horizontal = 9.dp)
                            .align(Alignment.CenterVertically)
                    )
                    RoundIconButton(modifier = modifier,
                        imageVector = Icons.Default.Add,
                        onClick = {
//                            splitValue.intValue++
                            if(splitByState.value < range.last) {
                                splitByState.value++
                            }
                        })
                }
            }

            Row(modifier = Modifier
                .padding(horizontal = 3.dp,
                    vertical = 12.dp)) {
                Text("Tip", modifier = modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(200.dp))
                Text("$ ${tipAmountState.value}", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {


                Text(text = "${tipPercentageState.intValue} %",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier.padding(8.dp))
                Spacer(modifier = modifier.height(14.dp))

                Slider(modifier = modifier
                    .padding(horizontal = 16.dp),
                    steps = 5
                    , value = sliderPositionState.floatValue,
                    onValueChange = {sliderVal ->
                        if(totalBillState.value.isNotEmpty()) {
                            sliderPositionState.floatValue = sliderVal
                            // Set tip percentage state to the slider value converted to amount
                            tipPercentageState.intValue = (sliderPositionState.floatValue * 100).toInt()
                            // Calculate the total tip amount based on the bill and tip percentage
                            tipAmountState.value = calculateTotalTip(totalBill = totalBillState.value.toDouble(), tipPercentage = tipPercentageState.intValue)
                            errorMessageState.value = ""
                        } else {
                            //show inline error
                            errorMessageState.value = "Please enter a valid bill amount"
                        }
                    })

                if (errorMessageState.value.isNotEmpty()) {
                    Text(
                        text = errorMessageState.value,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = modifier.padding(top = 4.dp)
                    )
                }
            }
            } else {
                Box {}
            } // End: Split and Round Buttons

        } // End: Main Card
    }

}



@Preview(showBackground = true)
@Composable
fun TestCard() {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
//            Row(modifier = Modifier
//                .fillMaxWidth(),
//                verticalAlignment = Alignment.Bottom) {
                Text("Split")
//            }
        }

        Column(modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.fillMaxWidth()
                , horizontalArrangement = Arrangement.End) {
                RoundIconButton(
                    imageVector = Icons.Default.Remove,
                    onClick = { true})
                Text(
                    1.toString(),
                    modifier = Modifier
                        .padding(horizontal = 9.dp)
                        .align(Alignment.CenterVertically))
                RoundIconButton(modifier = Modifier,
                    imageVector = Icons.Default.Add,
                    onClick = { true })
            }
        }

    }
}





