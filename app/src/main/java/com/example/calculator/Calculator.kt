package com.example.calculator

import android.annotation.SuppressLint

object Calculator {
    fun evaluate(expression: String): String {
        val tokens = tokenize(expression)

        //後置記法のトークンをためるリスト
        val output = mutableListOf<String>()
        //演算子や括弧を保持する スタック
        val operators = ArrayDeque<String>()

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]

            when {
                //正規表現で負の数と小数も認識 -> 数字ならそのまま追加
                token.matches(Regex("""-?\d+(\.\d+)?""")) -> output.add(token)
                token == "(" -> operators.addFirst(token)
                token == ")" -> {
                    while (operators.isNotEmpty() && operators.first() != "(") {
                        output.add(operators.removeFirst())
                    }
                    if (operators.isNotEmpty() && operators.first() == "(") operators.removeFirst()
                }

                token == "%" -> {
                    if (output.isNotEmpty()) {
                        val num = output.removeAt(output.lastIndex).toDouble() / 100.0
                        output.add(num.toString())
                    } else error("Unexpected % operator")
                }
                //優先順位が低ければoutputに追加
                else -> {
                    while (operators.isNotEmpty() &&
                        precedence(operators.first()) >= precedence(token)
                    ) {
                        output.add(operators.removeFirst())
                    }
                    operators.addFirst(token)
                }
            }
            i++
        }

        while (operators.isNotEmpty()) {
            output.add(operators.removeFirst())
        }

        // 後置記法計算
        val stack = ArrayDeque<Double>()
        for (token in output) {
            when {
                token.matches(Regex("""-?\d+(\.\d+)?""")) -> stack.addFirst(token.toDouble())
                else -> {
                    if (stack.size < 2) {
                        return "無効な式です"
                    }
                    val b = stack.removeFirst()
                    val a = stack.removeFirst()
                    val res = when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> if (b == 0.0) return "0で割ることはできません" else a / b
                        else -> error("Unknown operator: $token")
                    }
                    stack.addFirst(res)
                }
            }
        }

        val result = stack.removeFirst()
        return if (result % 1.0 == 0.0) {
            "%d".format(result.toLong())
        } else {
            "%.10f".format(result).trimEnd('0').trimEnd('.')
        }
    }

    // 優先順位
    private fun precedence(op: String) = when (op) {
        "+", "-" -> 1
        "*", "/" -> 2
        else -> 0
    }

    // トークナイズ（小数・負数・%・括弧対応）
    private fun tokenize(expression: String): List<String> {
        // 負数対応のため、先頭の-または(の直後の-も数字に含める
        val regex = Regex("""(?<=^|\(|\+|\-|\*|/)-?\d+(\.\d+)?|[+\-*/()%]""")
        return regex.findAll(expression.replace(" ", ""))
            .map { it.value }
            .toList()
    }
}