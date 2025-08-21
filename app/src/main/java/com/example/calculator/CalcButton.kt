package com.example.calculator

sealed class CalcButton (
    val symbol: String
) {
    class Number(symbol: String): CalcButton(symbol)
    open class Operator(symbol: String): CalcButton(symbol)

    object Clear : CalcButton("C")
    object Parentheses : CalcButton("( )")
    object Percent : CalcButton("%")
    object Divide : Operator("÷")
    object Multiply : Operator("×")
    object Minus : Operator("-")
    object Plus : Operator("+")
    object Sign : CalcButton("+/-")
    object Dot : CalcButton(".")
    object Equals : CalcButton("=")

    object Delete : CalcButton("←")
}