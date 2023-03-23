package com.example.mainactivity.models

import android.graphics.Point
import com.example.mainactivity.constants.CellConstants
import com.example.mainactivity.constants.FieldConstants
import com.example.mainactivity.helper.array2dOfByte
import com.example.mainactivity.storage.AppPreferences

/* Отдельный файл класса, для создания модели приложения,
реализующего необходимую логику игрового поля Tetris, а также
обслущивает промежуточный интерфейс между представлениями и
созданными компонентами блока.
Представление отправит модели приложения запрос на выполнение,
модель выполнит действие, если оно допустимо, и отправит отзыв
представителю.
Некоторые функции класса отслеживают:
1. Текущий счет,
2. Состояние игрового поля приложения,
3. Текущий блок,
4. Текущее состояние игры,
5. Текущий статус игры и движений, который выполняет текущий блок. */
class AppModel {
    var score: Int = 0

    /* Константа score относится к целочисленному свойству, применяемому
    для сохранения текущего счета игрока в игровом сеансе */
    private var preferences: AppPreferences? = null
    /* Константа preferences относится к частному свойству, которое будет
    содержать объект AppPreferences для обеспечения непосредственного доступа
    к файлу SharedPreferences приложения.*/

    var currentBlock: Block? = null

    /*Константа currentBlock - относится к свойству, которое будет содержать
текущий блок трансляции через игровое поле.*/
    var currentState: String = Statuses.AWAITING_START.name

    // AWAITING_START (ОЖИДАНИЕ_ НАЧАЛА)
    /*Константа currentState содержит состояние игры, а константа
    Statues.AWAITING_START.name возвращает имя Statues.AWAITING_START в
    форме строки AWAITING_START, текущее состояние игры немедленно
    инициализируется значением AWAITING_START, поскольку это первое
    состояние, в которое должно перейти действие GameActivity после запуска.*/
    private var field: Array<ByteArray> = array2dOfByte(
        FieldConstants.ROW_COUNT.value,
        FieldConstants.COLUMN_COUNT.value
        /*Константа field представляет двумерный массив, служащий в качестве
        игрового поля.*/
    )

    enum class Statuses {
        AWAITING_START, ACTIVE, INACTIVE, OVER
        /* Здесь мы создали 4 константы состояния.
        AWAITING_START - представляет состояние игры, до её запуска
        ACTIVE и INACTIVE -представляют состояние игрового процесса
        (если этот процесс выполняется или нет),
        OVER - статус, принимаемый игрой на момент ее завершения. */
    }

    enum class Motions {
        LEFT, RIGHT, DOWN, ROTATE
        /* Данные костанты определены для представления различных движений
        блока - вправо, влево, вверх/вниз, выполнять вращение.*/
    }

    /*Следует еще добавить несколько методов сеттеров и геттеров.*/
    fun setPreferences(preferences: AppPreferences?) {
        this.preferences = preferences
        /* Метод setPreferences устанавливает свойство предпочтений для
        AppModel, что позволит передать экземпляр класса AppPreferences в
        виде аргумента этой функции.*/
    }

    fun getCellStatus(row: Int, column: Int): Byte? {
        return field[row][column]
        /* Метод getCellStatus возвращает состояние ячейки, имеющейся
        в указанной позиции столбца строки в двумерном массиве поля. */
    }

    private fun setCellStatus(row: Int, column: Int, status: Byte?) {
        if (status != null) {
            field[row][column] = status
        }
        /* Метод setCellStatus устанавливает состояние имеющейся в поле ячейки
        равным указанному байту.*/
    }

    /* В нашей модели также необходимы Функции для проверки состояния, которые
    служат средой для подтверждения состояния, в котором находится игра
    в текущий момент. Так как мы имеем три возможных статуса игры, которым
    соответсвуют три игровых состояния, то для каждого из них необходимо
    наличие трех функций проверки. */
    /* Все три метода будут возвращать логические значения true и false
    в зависимости от соответсвующего состояния, в котором находится игра.*/
    fun isGameOver(): Boolean {
        return currentState == Statuses.OVER.name
    }

    fun isGameActive(): Boolean {
        return currentState == Statuses.ACTIVE.name
    }

    fun isGameAwaitingStart(): Boolean {
        return currentState == Statuses.AWAITING_START.name
    }

    /* Далее добавляем функцию, которую можно будет использовать для
    увеличения значения очков, находящегося в некотором диапазоне.
    При вызове функция boostScore увеличивает текущий счет игрока на 10,
    после чего проверяется, не превышает ли текущий счет игрока уже
    установленный рекорд, записанный в файле настроек. Если текущая оценка
    больше сохраненного рекорда, рекорд переписываетя с учетом
    текущего значения очков*/
    private fun boostScore() {
        score += 10
        if (score > preferences?.getHighScore() as Int)
            preferences?.saveHighScore(score)
    }

    /* Далее перейдем к созданию более сложных функций стр.131 */
    private fun generateNextBlock() {
        currentBlock = Block.createBlock()
        /*Функция generateNextBlock() будет создавать новый экземпляр блока и
        устананвливает значение currentBlock равным вновь созданному экземпляру*/
    }

    private fun validTranslation(position: Point, shape: Array<ByteArray>): Boolean {
        return if (position.y < 0 || position.x < 0) {
            false
        } else if (position.y + shape.size > FieldConstants.ROW_COUNT.value) {
            false
        } else if (position.x + shape[0].size > FieldConstants
                .COLUMN_COUNT.value
        ) {
            false
        } else {
            for (i in 0 until shape.size) {
                for (j in 0 until shape[i].size) {
                    val y = position.y + i
                    val x = position.x + j
                    if (CellConstants.EMPTY.value != shape[i][j]
                        && CellConstants.EMPTY.value != field[y][x]
                    ) {
                        return false
                    }
                }
            }
            true
        }
    }
    /* Данная функция служит для проверки допустимости поступательного
    движения тетрамино в игровом поле на основе набора условий.
    Метод возвращает логическое значение true, если трансляция корректна, и
    false - в противном случае.
    Первые три условия проверяют, находится ли в поле позиция, в которую
    переводиться тетрамино. Блок else проверяет, свободны ли клетки,
    в которые пытается перейти тетрамино. Если это не так, возвращается
    значение false.
    Но для использования метода validTranslation() еще нужна функция вызова.
    Которую объявим ниже */

    private fun moveValid(position: Point, frameNumber: Int?): Boolean {
        val shape: Array<ByteArray>? =
            currentBlock?.getShape(frameNumber as Int)
        return validTranslation(position, shape as Array<ByteArray>)
        /* функция вызова moveValid() применяет функцию validTranslation(), которая
        проверяет, разрешен ли выполненный игроком ход. Если перемещение разрешено,
        возвращается значение true, в противном случае - значение false */
    }
    // и еще нужно создать несколько важных методов, поехали братишка!

    fun generateField(action: String) {
        /*Данный метод генерирует обновления поля. Это обновление поля определяется
        действием, которое передается в качестве аргумента action.*/
        if (isGameActive()) {
            /* Сначала метод generateField() проверяет, находится ли игра при вызове
            в активном состоянии.Если игра активна, извлекаются номер фрейма и
            координаты блока. */
            resetField()
            var frameNumber: Int? = currentBlock?.frameNumber
            val coordinate: Point? = Point()
            coordinate?.x = currentBlock?.position?.x
            coordinate?.y = currentBlock?.position?.y

            when (action) {
                /*Теперь с помощью выражения when определяется запрашиваемое действие.
                После идентификации запрошенного действия
                (движение влево, вправо или вниз)
                 координаты блока соответствующим образом изменяются.Если запрашивается
                 вращательное движение, значение frameNumber изменяется с учетом
                 соответствующего номера фрейма, который представляет вращающееся
                 тетрамино. */
                Motions.LEFT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.minus(1)
                }
                Motions.RIGHT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.plus(1)
                }
                Motions.DOWN.name -> {
                    coordinate?.y = currentBlock?.position?.y?.plus(1)
                }
                Motions.ROTATE.name -> {
                    frameNumber = frameNumber?.plus(1)
                    if (frameNumber != null) {
                        if (frameNumber >= currentBlock?.frameCount as Int) {
                            frameNumber = 0
                        }
                    }
                }
            }
            if (!moveValid(coordinate as Point, frameNumber)) {
                /* Теперь метод generateField() посредством метода moveValid()
                проверяет, является ли запрошенное движение действительным.
                Если это движение не являетсяя действительным, текущий блок фиксируется
                в поле в его текущей позиции с помощью метода translateBlock().*/
                translateBlock(
                    currentBlock?.position as Point,
                    currentBlock?.frameNumber as Int
                )
                if (Motions.DOWN.name == action) {
                    boostScore()
                    persistCellData()
                    assessField()
                    generateNextBlock()
                    if (!blockAdditionPossible()) {
                        currentState = Statuses.OVER.name;
                        currentBlock = null;
                        resetField(false);
                    }
                }
            } else {
                if (frameNumber != null) {
                    translateBlock(coordinate, frameNumber)
                    currentBlock?.setState(frameNumber, coordinate)
                }
            }

        }
    }

    private fun resetField(ephemeraCellsOnly: Boolean = true) {
        for (i in 0 until FieldConstants.ROW_COUNT.value) {
            (0 until FieldConstants.COLUMN_COUNT.value)
                .filter {
                    !ephemeraCellsOnly || field[i][it] ==
                            CellConstants.EPHEMERAL.value
                }
                .forEach { field[i][it] = CellConstants.EMPTY.value }
        }
    }

    private fun persistCellData() {
        for (i in 0 until field.size) {
            for (j in 0 until field[i].size) {
                var status = getCellStatus(i, j)
                if (status == CellConstants.EPHEMERAL.value) {
                    status = currentBlock?.staticValue
                    setCellStatus(i, j, status)
                }
            }
        }
    }

    private fun assessField() {
        for (i in 0 until field.size) {
            var emptyCells = 0;
            for (j in 0 until field[i].size) {
                val status = getCellStatus(i, j)
                val isEmpty = CellConstants.EMPTY.value == status
                if (isEmpty)
                    emptyCells++
            }
            if (emptyCells == 0)
                shiftRows(i)
        }
    }

    /* Как можно заметить метод translateBlock() не реализуется.
    Он добавляется к модели AppModel вместе с методами
    BlockAdditionPossible (), shiftRows (), startGame (),
    restartGame (), endGame () и resetModel ()
    следуюдщим образом: */
    private fun translateBlock(position: Point, frameNumder: Int) {
        synchronized(field) {
            val shape: Array<ByteArray>? = currentBlock?.getShape(frameNumder)
            if (shape != null) {
                for (i in shape.indices) {
                    for (j in 0 until shape[i].size) {
                        val y = position.y + i
                        val x = position.x + j
                        if (CellConstants.EMPTY.value != shape[i][j]) {
                            field[y][x] = shape[i][j]
                        }
                    }
                }
            }
        }
    }

    private fun blockAdditionPossible(): Boolean {
        if (!moveValid(
                currentBlock?.position as Point,
                currentBlock?.frameNumber
            )
        ) {
            return false
        }
        return true
    }

    private fun shiftRows(nToRow: Int) {
        if (nToRow > 0) {
            for (j in nToRow - 1 downTo 0) {
                for (m in 0 until field[j].size) {
                    setCellStatus(j + 1, m, getCellStatus(j, m))
                }
            }
        }
        for (j in 0 until field[0].size) {
            setCellStatus(0, j, CellConstants.EMPTY.value)
        }
    }

    fun startGame() {
        if (!isGameActive()) {
            currentState = Statuses.ACTIVE.name
            generateNextBlock()
        }
    }

    fun restartGame() {
        resetModel()
        startGame()
    }

    fun endGame() {
        score = 0
        currentState = AppModel.Statuses.OVER.name
    }

    private fun resetModel() {
        resetField(false)
        currentState = Statuses.AWAITING_START.name
        score = 0
    }
    /* В сценари, когда запрошенное перемещение является движением вниз, а
    перемещение недопустимо, это означает, что блок достиг нижней границы поля.
    В этом случае счет игрока возрастает благодаря применению метода
    boostScore(),
     и состояние всех ячеек поля сохраняются посредством метода
    persistCellData().
    Затем вызывается метод assessField() для построчного сканирования строк
    поля и проверки заполняемости находящихся в строках ячеек: */


    /* В том случае, если в строке заполнены все ячейки, строка очищается и
    сдвигается на величину, определенную с помощью метода shiftRow().
    После заершения оценки  поля создается новый блок -
    с помощью метода generateNextBlock(): */

    /* Прежде чем вновь сгенерированный блок передается полю,
    модель AppModel должна с помощью метода blockAdditionPossible(),
    убедиться в том, что поле еще не заполнено, а блок может
    перемещаться в поле: */
//    private fun blockAdditionPossible():Boolean{
//        if (!moveValid(currentBlock?.position as Point,
//                currentBlock?.frameNumber)){
//            return false
//        }
//        return true
//    }
    /* Если добавление блока невозможно, это значит, что все блоки размещены
    по верхнему краю поля. В таком случае игра завершается. Тогда текущее
    состояние игры устанавливается как Statuses.OVER,
    а currentBlock - к значению null, и наконец поле отчищается.

    С другой стороны, если перемещение было действительным с самого начала,
    блок транслирутся с учетом его новых координат
    посредством метода translateBlock(), а состояние текущега блока
    устанавливается с учетом новых его координат и значения frameNumber.

     */
}


