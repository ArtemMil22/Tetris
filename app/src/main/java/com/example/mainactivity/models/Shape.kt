package com.example.mainactivity.models

enum class Shape (val frameCount: Int, val startPosition:Int) {
    /* (ФИГУРА)
    В конструкторе два аргумента, frameCount - указывает число возможных
    фреймов, в которых может находиться форма.
    startPosition - указывает предпологаемую начальную позицию в доль
    оси Х в поле игрового процесса. */

    /* Помни, мы применяем концепцию представления фреймов в виде двумерного
    массива байтов, и поэтому нам можно представить фрейм этой формы в виде
    двумерного массива байтов с четыремя строками и одним столбцом, каждая
    ячейка которого заполнена байтовым значением, равным 1.
    (это про протсую форму тетрамино - СТОЛБЕЦ)*/

    Tetromino4(2,2){
        // I - образная форма
/*Tetromino распологает более одного фрейма, для результата возможности вращения
 (Является одновременно и объектом и константой)))*/
        /* В приведенном блоке кода создается экземпляр класса enum, который
        обеспечивает реализацию объявленной асбстрактной функции getFrame().
        Идентификатором экземпляра является Tetromino4.
        Которая имеет 2 фрейма (гориз и верт) и 2 стартовые позиции

         */
        override fun getFrame(frameNumber: Int):Frame{
/*Реализация функции getFrame() в Tetromino использует целочисленную переменную
frameNumber, которая определяет фрейм Tetromino, который будет возвращаться.
Когда переменная frameNumber передает функции getFrame() значение 0,
 функция возвращает объект Frame, моделирующий фрейм в горизонтальном положении
 Если значение является 1, то функция возвратит объект фрейма, моделирующий форму
 в вертикальном положении
 Иначе исключение*/
            return when (frameNumber){
                0 -> Frame(4).addRow("1111")
                1-> Frame(1)
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")

            }
        }
    },
    Tetromino1 (1,1) {
        // Квадрат
        override fun getFrame(frameNumber:Int): Frame{
            return Frame(2)
                .addRow("11")
                .addRow("11")
        }
    },
    Tetromino2(2,1){
        // Z - образная форма
        override fun getFrame(frameNumber: Int):Frame{
            return when (frameNumber){
                0->Frame(3)
                    .addRow("110")
                    .addRow("011")
                1-> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("10")
                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },
    Tetromino3(2,1) {
        // S - образная форма
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){
                0-> Frame(3)
                    .addRow("011" )
                    .addRow("110")
                1->Frame(2)
                    .addRow("10")
                    .addRow("11")
                    .addRow("01")
                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },
    Tetromino5(4,1){
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){
                0->Frame(3)
                    .addRow("010")
                    .addRow("111")
                1->Frame(2)
                    .addRow("10")
                    .addRow("11")
                    .addRow("10")
                2-> Frame(3)
                    .addRow("111")
                    .addRow("010")
                3-> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("01")
                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },
    Tetromino6(4,1){
        // J - образная форма моделируемого тетрамино
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){
                0-> Frame(3)
                    .addRow("100")
                    .addRow("111")
                1-> Frame(2)
                    .addRow("11")
                    .addRow("10")
                    .addRow("10")
                2->Frame(3)
                    .addRow("111")
                    .addRow("001")
                3->Frame(2)
                    .addRow("01")
                    .addRow("01")
                    .addRow("11")
                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },
    Tetromino7(4,1) {
        // L - образная форма
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){
                0-> Frame(3)
                    .addRow("001")
                    .addRow("111")
                1->Frame(2)
                    .addRow("10")
                    .addRow("10")
                    .addRow("11")
                2-> Frame(3)
                    .addRow("111")
                    .addRow("100")
                3->Frame(2)
                    .addRow("11")
                    .addRow("01")
                    .addRow("01")
                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    };
    abstract fun getFrame(frameNumber: Int): Frame
    /* данная фун имеет существенные отличие от предыдущих функций
    имеет кл.сл. abstract ( не имеет реализации, следовательно тела)
    применяется для абстрагирования поведения, которое должно быть
    реализовано расширяющим классом.*/
}