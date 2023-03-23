package com.example.mainactivity.models

import com.example.mainactivity.helper.array2dOfByte

class Frame(private val width: Int) {
    /* width задает необходимую ширину генерируемого фрейма
       (число столбцов в байтовом массиве фрейма)*/
    val data: ArrayList<ByteArray> = ArrayList()
        /* свойство data содержит список элементов массива
        в пространстве значений ByteArray */

    fun addRow(byteStr: String): Frame {
        val row = ByteArray(byteStr.length)

        for (index in byteStr.indices) {
            row[index] = "${byteStr[index]}".toByte()
        }
        data.add(row)
        return this
    }
    /* addRow() обрабатывает строку, преобразуя каждый отдельный
    символ строки в байтовое представление, и добавляет байтовые
    представление в байтовый массив, после чего байтовый массив
    добавляется в список данных */
      /* Функция get() преобразует список массива данных
    в байтовый массив, который затем возвращает.*/
    fun as2dByteArray(): Array<ByteArray> {
        val bytes = array2dOfByte(data.size, width)
        return data.toArray(bytes)
    }

}