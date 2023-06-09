package com.example.mainactivity.constants

enum class CellConstants (val value:Byte) {
    EMPTY(0),
    EPHEMERAL(1) // (недолговечный, преходящий)
    /* При создании класса Frame при моделировании фреймов блоков,
    определялась функция addRow(), которая в качетсве аргумента
    обрабатывает строку , состоящую из 1 и 0.
    Значение, равное 1, представляет соответсвующие фрейм ячейки, а
    значение равное 0, представляет ячейки, исключенные из фрейма, а затем
    преобразует данные значения, 1 и 0, в байтовые представления.

    Этими байтами мы будем манипулировать в функциях, и для них необходимо
    распалагать соответсвующими константами, представленные выше. */
}