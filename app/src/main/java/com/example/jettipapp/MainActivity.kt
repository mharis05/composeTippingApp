package com.example.jettipapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column {
                    TopHeader()
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
        Surface(color = MaterialTheme.colorScheme.background) {
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
    BillForm { billAmt ->
        Log.d("AMT", "MainContent: $billAmt")

    }

}

@Composable
fun BillForm(modifier: Modifier = Modifier, captureAmount: (String) -> Unit) {
    val totalBillState = remember { mutableStateOf("") }
    val splitValue = remember { mutableIntStateOf(1) }
    val validState = remember(totalBillState.value.trim()) {
        totalBillState.value.trim().isNotEmpty()
                && totalBillState.value.toDoubleOrNull() != null
    }

    val focusManager = LocalFocusManager.current

    Surface(
        Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    )
    {
        // Start: Main Card
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            )
        {
            InputField(modifier = Modifier.fillMaxWidth(),
                valueState = totalBillState,
                labelId = "Enter Bill Amount",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) {
                        return@KeyboardActions
                    }
                    captureAmount(totalBillState.value.trim())
                    focusManager.clearFocus()
                })
//            if (validState) { // Start: Split and Round Buttons

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Split", modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(120.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    RoundIconButton(
                        imageVector = Icons.Default.Remove,
                        onClick = { if (splitValue.intValue > 1) splitValue.intValue-- })
                    Text(
                        splitValue.intValue.toString(),
                        modifier = Modifier
                            .padding(horizontal = 9.dp)
                            .align(Alignment.CenterVertically)
                    )
                    RoundIconButton(modifier = Modifier,
                        imageVector = Icons.Default.Add,
                        onClick = { splitValue.intValue++ })
                }
            }
//            } else {
//                Box {}
//            } // End: Split and Round Buttons
            Row(                modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start) {
                Text("Tip", modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(120.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text("33", modifier = Modifier.align(Alignment.CenterVertically))

                }

            }
        } // End: Main Card
    }

}



