package com.example.calculator

sealed class CalcButton (
    val symbol: String
) {
    class Number(symbol: String): CalcButton(symbol)
    class Operator(symbol: String): CalcButton(symbol)

    object Clear : CalcButton("C")
    object Parentheses : CalcButton("( )")
    object Percent : CalcButton("%")
    object Divide : CalcButton("÷")
    object Multiply : CalcButton("×")
    object Minus : CalcButton("-")
    object Plus : CalcButton("+")
//    val Divide = Operator("÷")
//    val Multiply = Operator("×")
//    val Minus= Operator("-")
//    val Plus = Operator("+")


    object Sign : CalcButton("+/-")
    object Dot : CalcButton(".")
    object Equals : CalcButton("=")

    object Delete : CalcButton("←")
}