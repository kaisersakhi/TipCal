package com.kaisersakhi.tipcal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaisersakhi.tipcal.ui.theme.TipCalTheme
import com.kaisersakhi.tipcal.widgets.main_activity.RoundButton

const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalTheme {
                Scaffold {
                    MainBody()
                }
            }
        }
    }
}

@Preview
@Composable
fun MainBody() {
//
//    val tipObject: MutableState<TipModel> = remember {
//        mutableStateOf(TipModel("0", 1, 0.0f))
//    }

    val totalNumberOfPersons = remember {
        mutableStateOf(1)
    }

    val totalAmount = remember {
        mutableStateOf(0.0f)
    }

    val billAmount = remember {
        mutableStateOf("")
    }

    val tipPercentage = remember {
        mutableStateOf(0f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        //column content

        //Top Header fun , only reads totalAmount
        TopHeader(totalAmount)

        Spacer(modifier = Modifier.height(0.dp))

        Body(
            billAmount = billAmount,
            totalNumberOfPersons = totalNumberOfPersons,
            tipPercentage = tipPercentage,
            onNumberOfPersonsChange = {
                totalNumberOfPersons.value = it
                totalAmount.value = calculateTotalBillAmount(
                    billAmount = if (billAmount.value.isNotEmpty()) billAmount.value.toFloat() else 0f,
                    totalPersons = totalNumberOfPersons.value,
                    tipPercentage = tipPercentage.value
                )
            },
            onBillAmountChange = {
                //OutlineTextField.onValueChange
                billAmount.value = it

                totalAmount.value = calculateTotalBillAmount(
                    billAmount = if (billAmount.value.isNotEmpty()) billAmount.value.toFloat() else 0f,
                    totalPersons = totalNumberOfPersons.value,
                    tipPercentage = tipPercentage.value
                )


            },
            onTipPercentageChange = {
                tipPercentage.value = it
                totalAmount.value = calculateTotalBillAmount(
                    billAmount = if (billAmount.value.isNotEmpty()) billAmount.value.toFloat() else 0f,
                    totalPersons = totalNumberOfPersons.value,
                    tipPercentage = tipPercentage.value
                )
            }
        ) //body arguments end

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Made with \uD83C\uDFCB️ by Kaiser Sakhi",
                modifier = Modifier.padding(bottom = 10.dp),
                fontFamily = FontFamily.Cursive
            )
        }
    }
}


@Composable
fun TopHeader(totalAmount: MutableState<Float>) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
            .height(180.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(8.dp))),
        elevation = 2.dp,
        color = Color.LightGray
    ) {
        //Surface Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //TopHeader.Surface.Column

            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "₹ ${"%.2f".format(totalAmount.value)}",
                fontFamily = FontFamily.SansSerif,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(top = 15.dp),
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Body(
    billAmount: MutableState<String>,
    totalNumberOfPersons: MutableState<Int>,
    tipPercentage: MutableState<Float>,
    onBillAmountChange: (String) -> Unit,
    onNumberOfPersonsChange: (Int) -> Unit,
    onTipPercentageChange: (Float) -> Unit
) {

    val localKeyboard = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        elevation = 10.dp
    ) {
        //body content : Body.Surface

        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                //reads bill amount
                value = billAmount.value,
                label = { Text(text = "Enter Bill") },
                onValueChange = {
                    try {
                        onBillAmountChange(
                            it.trim().ifEmpty { "0" }
                        )
                    } catch (ignored: java.lang.Exception) {
                    }
                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 15.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.rupee),
                        contentDescription = "Money Sign"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        localKeyboard?.hide()
                    }
                )
            )

            //Split Row reads and writes tipObject
            SplitRow(totalNumberOfPersons) {
                //on button clicked , change total number of persons
                onNumberOfPersonsChange(it)
            }
            TipRow(billAmount = billAmount, tipPercentage = tipPercentage)
            TipPercentage(tipPercentage) {
                //onTipChange
                onTipPercentageChange(it)
            }
        }

    }
}

@Composable
fun SplitRow(numberOfPersons: MutableState<Int>, onNoPersonChange: (Int) -> Unit) {


    Row(
        modifier = Modifier
            .padding(start = 15.dp, end = 5.dp, top = 15.dp),
//        horizontalArrangement = Arrangement.Start,

    ) {
        Text(
            text = "Split",
            modifier = Modifier.padding(5.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
//        Spacer(modifier = Modifier.width(150.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(top = 5.dp, end = 20.dp)
                .fillMaxWidth()

        ) {
            RoundButton(
//                imagePainter = ImageBitmap.imageResource(id = R.drawable.plus),
                imageVector = ImageBitmap.imageResource(id = R.drawable.minus),
            ) {
                //onClick : minus button

                if (numberOfPersons.value > 1) {
                    onNoPersonChange(numberOfPersons.value - 1)
                }
            }

            Text(
                text = numberOfPersons.value.toString(),
                modifier = Modifier
                    .padding(start = 18.dp, end = 18.dp)
                    .align(Alignment.CenterVertically),
                fontSize = 25.sp
            )

            RoundButton(
                imageVector = ImageBitmap.imageResource(id = R.drawable.plus)
            ) {
                //onclick : plus button
//                ++tipObject.value.totalNumberOfPersons
                onNoPersonChange(numberOfPersons.value + 1)
            }

        }

    }
}


@Composable
fun TipRow(billAmount: MutableState<String>, tipPercentage: MutableState<Float>) {

    //this fun only reads from bill Amount and percentage
    val tipAmount: Float =
        if (billAmount.value.isNotEmpty()) billAmount.value.toFloat() * tipPercentage.value else 0f

    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(top = 20.dp, start = 19.dp)
    ) {
        Text(text = "Tip", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier
            .padding(end = 50.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,

        ) {

            Text(text = "₹ ${"%.2f".format(tipAmount)}", fontSize = 25.sp)
        }
    }
}

@Composable
fun TipPercentage(tipPercentage: MutableState<Float>, onTipPercentageChange: (Float) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "${"%.2f".format(tipPercentage.value * 100)}%",
            fontSize = 25.sp
        )


        Slider(
            value = tipPercentage.value,
            onValueChange = {
                onTipPercentageChange(it)
                Log.d(TAG, "TipPercentage: $it")
            },
            modifier = Modifier.padding(all = 10.dp),
        )
    }
}


fun calculateTotalBillAmount(billAmount: Float, totalPersons: Int, tipPercentage: Float) =
    (billAmount * tipPercentage + billAmount) / totalPersons

