package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.CalcButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                CalculatorUI()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
//    CalculatorUI()
}

@Composable
fun CalculatorUI() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.fillMaxSize().padding(vertical = 30.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = input,
            fontSize = 32.sp,
            textAlign = TextAlign.End
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = result,
            fontSize = 24.sp,
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalculatorButtons { button ->
            when (button) {
                is CalcButton.Number -> {
                    /* 数字が押されたときの処理 */
                    if (input.isNotEmpty() && input.last() == ')') {
                        input += "×"
                        input += button.symbol
                    } else {
                        input += button.symbol
                    }
                }
                is CalcButton.Operator -> {
                    /* 演算子が押されたときの処理 */
                    if (canAppendOperator(input)) {
                        input += button.symbol
                    }
                }
                CalcButton.Clear -> {
                    /* クリア処理 */
                    input = ""
                    result = ""
                }
                CalcButton.Delete -> {
                    input = input.dropLast(1)
                }
                CalcButton.Parentheses -> {
                    //input.count <- 条件に合う要素の数を数える
                    val openCount = input.count { it == '(' }
                    val closeCount = input.count { it == ')' }
                    // 開く括弧を追加すべきか、閉じる括弧を追加すべきかを判定
                    val isOpening = openCount == closeCount || input.lastOrNull() in listOf('+','-','×','÷','(')
//                    if (canAppendParenthesis(input, isOpening)) {
//                        input += if (isOpening) "(" else ")"
//                    }
                    if (isOpening) {
                        if (canAppendParenthesis(input, true)) {
                            // 直前が数字や ) の場合は × を補完
                            if (input.isNotEmpty() && (input.last().isDigit() || input.last() == ')')) {
                                input += "×("
                            } else {
                                input += "("
                            }
                        }
                    } else {
                        if (canAppendParenthesis(input, false)) {
                            input += ")"
                        }
                    }
                }
                CalcButton.Percent -> {
                    //"%"が押されたときの処理
                    if (canAppendPercent(input)) {
                        input += "%"
                    }
                }
                CalcButton.Dot -> {
                    //"."が押されたときの処理
                    if (canAppendDot(input)) {
                        input += "."
                    }
                }
                CalcButton.Equals -> {
                    /* 計算実行処理 */

                }


                else -> {}
            }
        }
    }
}

@Composable
fun CalculatorButtons(
    onButtonClick: (CalcButton) -> Unit
) {
    val buttons = listOf(
        listOf(CalcButton.Clear, CalcButton.Parentheses, CalcButton.Percent, CalcButton.Divide),
        listOf(CalcButton.Number("7"), CalcButton.Number("8"), CalcButton.Number("9"), CalcButton.Multiply),
        listOf(CalcButton.Number("4"), CalcButton.Number("5"), CalcButton.Number("6"), CalcButton.Minus),
        listOf(CalcButton.Number("1"), CalcButton.Number("2"), CalcButton.Number("3"), CalcButton.Plus),
        listOf(CalcButton.Sign, CalcButton.Number("0"), CalcButton.Dot, CalcButton.Equals)
    )

    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton (
                onClick = { onButtonClick(CalcButton.Delete) },
                shape = CircleShape,
                border = null
            ) {
                Text(
                    text = CalcButton.Delete.symbol,
                    fontSize = 24.sp
                )
            }
        }
        buttons.forEach { row ->
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { button ->
                    Button (
                        onClick = { onButtonClick(button) },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            //背景色
                            containerColor = when (button) {
                                CalcButton.Equals -> Color(0, 255, 131, 206)
                                else -> Color(0, 0, 0, 96)
                            },

                            //文字の色
                            contentColor = when (button) {
                                CalcButton.Equals -> Color(255, 255, 255, 255)
                                CalcButton.Clear -> Color(255, 82, 82, 255)
                                else -> Color(0, 0, 0)
                            }
                        )
                    ) {
                        Text(
                            text = button.symbol,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}

/*
* 連続で演算子を追加できないようにする
* 追加できるならtrueを返す
* */
fun canAppendOperator(input: String) : Boolean {
    if (input.isEmpty()) {
        return false
    }

    val lastChar = input.last()
    return lastChar !in listOf('+', '-', '×', '÷', '.', '(')
}
/*
* 正しく"."を入力できるようにする
* 入力できるならtrueを返す
* */
fun canAppendDot(input: String) : Boolean {
    if (input.isEmpty()) {
        return false
    }

    val lastChar = input.last()
    if (lastChar in listOf('+', '-', '×', '÷', '(')) {
        return false
    }

    //直近の数値ブロックにすでに"."がある場合は打てない
    //123+456+6.78 -> lastNumber = 6.78
    val lastNumber = input.takeLastWhile { it.isDigit() || it == '.' }
    return !lastNumber.contains('.')
}
/*
* 正しく"()"を入力できるようにする
* 入力できるならtrueを返す
* */
fun canAppendParenthesis(input: String, isOpening: Boolean): Boolean {
    return if (isOpening) {
        // 開き括弧は空、または演算子の後ならOK
        // 「(」を追加していいか？
        input.isEmpty() || input.last().isDigit() || input.last() in listOf('+', '-', '×', '÷', '(')
    } else {
        // 閉じ括弧は空ではNG、最後が演算子ならNG
        // 「)」を追加していいか？
        if (input.isEmpty()) false
        else input.count { it == '(' } > input.count { it == ')' } &&
                input.last() !in listOf('+', '-', '×', '÷', '(')
    }
}
/*
* "正しく"%"を入力できるようにする
* 入力できるならtrueを返す
* */
fun canAppendPercent(input: String): Boolean {
    // 数字や閉じ括弧の直後ならOK
    if (input.isEmpty()) return false
    val lastChar = input.last()
    return lastChar.isDigit() || lastChar == ')'
}

