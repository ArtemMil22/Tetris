package com.example.mainactivity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.annotation.Dimension
import com.example.mainactivity.constants.CellConstants
import com.example.mainactivity.constants.FieldConstants
import com.example.mainactivity.models.AppModel
import com.example.mainactivity.models.Block
import android.os.Handler
import com.example.mainactivity.GameActivity

/* В этом разделе мы реализуем представление TetrisView
- пользователький интерфейс, с помощью которого пользователь играет в Tetris.
Поскольку необходимо, чтобы TetrisView принадлежал классу View,
необходимо объявить его как расширение класса View.
Класс TetrisView объявлен здесь как расширение класса View,
поскольку View является классом, который расширяется
всеми элементами представления в приложении. Так как тип View имеет
коструктор, который должен инициализироваться, мы объявили здесь для TetrisView
два вторичных конструктора, которые инициализируют два различных
конструктора класса представления, - в зависимости от того,
какой вторичный конструктор вызывается.
 */
class TetrisView : View {
    private val paint = Paint()

    /*Свойство paint является экземпляром android.graphics.Paint
    Класс Paint включает информацию о стиле и цвете, относящуюся
    к рисованию текстов, растровых изображений и геометрических
    построений.*/
    private var lastMove: Long = 0

    /*Объект lastMove используется для отслеживания промежутка
    времени в миллисекундах, в течение которого выполняется
    перемещение. */
    private var model: AppModel? = null

    /* Экземпляр model служит для хранения, экземпляра AppModel,
    с которым представление TetrisView будет взаимодействовать
    при управлении игровым процессом. */
    private var activity: GameActivity? = null

    //  activity экземпляр класса GameActivity
    private val viewHandler = ViewHandler(this)
    private var cellSize: Dimension = Dimension(0, 0)
    private var frameOffset: Dimension = Dimension(0, 0)
    /* cellSize и frameOffset - свойства, содержащие размеры ячеек
    в игре и смещения фрейма соответственно.

     Оба класса ViewHandler и Dimension не поддерживаются фреймворком
     для Android-приложений. И нам необходимо реализовать оба эти класса.*/

    constructor(context: Context, attrs: AttributeSet) :
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) :
            super(context, attrs, defStyle)

    companion object {
        private val DELAY = 500
        private val BLOCK_OFFSET = 2
        private val FRAME_OFFSET_BASE = 10
    }

    /* Класс ViewHandler воспринимает экземпрляр для представления TetrisView
    как аргумент соответсвующего конструктора. Этот класс отменяет имеющуюся в
    соответствующем суперклассе класса функцию handleМessage().
    Метод handleМessage() проверяет отправку сообщения.
    Целое значение what означает, что сообщение отправлено.
    По окончании игры вызывается функция endGame() модели AppModel
    и отображается всплявающее окно, предупредждающее игрока о завершении игры.
    Если игра находится в активном состоянии, начинается движение вниз.

     */
    private class ViewHandler(private val owner: TetrisView) : Handler() {
        override fun handleMessage(message: Message) {
            if (message.what == 0) {
                if (owner.model != null) {
                    if (owner.model!!.isGameOver()) {
                        owner.model?.endGame()
                        Toast.makeText(owner.activity, "Game over",
                            Toast.LENGTH_LONG).show();
                    }
                    if (owner.model!!.isGameActive()) {
                        owner.setGameCommandWithDelay(AppModel.Motions.DOWN)
                    }
                }
            }
        }
        fun sleep(delay: Long) {
            this.removeMessages(0)
            sendMessageDelayed(obtainMessage(0), delay)
        }
    }

    /* Метод sleep() просто удаляет любое отправленное сообщение и направляет
    новое сообщение с задержкой, величина которой указана в аргументе delay. */

    private data class Dimension(val width: Int, val height: Int)

    fun setModel(model: AppModel) {
        this.model = model
    }

    fun setActivity(gameActivity: GameActivity) {
        this.activity = gameActivity
    }
    /* Методы setModel() и setActivity() представляют сеттер функции для
    свойств model и activity экземпляра. Как следует из названия
    метод setModel() устанавливает текущую модель, исаользуемую представлением,
    а setActivity() устанавливает применение действия. */

    fun setGameCommand(move: AppModel.Motions) {
        if (null != model && (model?.currentState ==
                    AppModel.Statuses.ACTIVE.name)) {
            if (AppModel.Motions.DOWN == move) {
                model?.generateField(move.name)
                invalidate()
                return
            }
            setGameCommandWithDelay(move)
        }
    }

    /* Метод setGameCommand() устанавливает исполняемую игрой текущую команду
    перемещения. Если выполняется команда перемещения DOWN (Вниз),
    модель приложения генерирует поле для блока, выполняющего перемещение вниз.
    Метод invalidate() вызываемый в setGameCommand(), может приниматься как
    запрос на внесение изменений на экране. Метод invalidate() в конечном
    итоге приводит к вызову метод onDraw().*/
    fun setGameCommandWithDelay(move: AppModel.Motions) {
        val now = System.currentTimeMillis()
        if (now - lastMove > DELAY) {
            model?.generateField(move.name)
            invalidate()
            lastMove = now
        }
        updateScores()
        viewHandler.sleep(DELAY.toLong())
    }
//ВОЗМОЖНА ОШИБКА СВЕРХУ
    private fun updateScores() {
        activity?.tvCurrentScore?.text = "${model?.score}"
        activity?.tvHighScore?.text =
            "${activity?.appPreferences?.getHighScore()}"
    }
//




    /*Метод onDraw() унаследован от классаа View. Метод вызывается, есди представление
    отбражает свое содержимое. Необходимо предоставить специальную реализацию
    для этого представления, поэтому добавляем в класс TetrisView следующий код: */
    override fun onDraw(canvas: Canvas) {
        //(ЖЕРЕБЬЕВКА, ТИРАЖ, ВЫТЯГИВАНИЕ) тип (Canvas - Холст)
        super.onDraw(canvas)
        drawFrame(canvas)
        if (model != null) {
            for (i in 0 until FieldConstants.ROW_COUNT.value) {
                for (j in 0 until FieldConstants.COLUMN_COUNT.value) {
                    drawCell(canvas, i, j)
                }
            }
        }
        /* Метод onDraw() в TetrisView переопределяет функцию onDraw()
        в ее суперклассе, т.е. он принимает обхект canvas в качестве
        единственного аргумента и должен вызвать функцию onDraw() суперкласса.
        Это делается путем вызова метода super.onDraw() и передачи ему
        экземпляра объекта canvas в качестве аргумента.

        Следующим в представлении TetrisView вызывется метод drawFrame(),
        который рисует фрейм для представления TetrisView.

        А затем отдельные его ячейки рисуются в пределах объекта canvas
        путем применения созданных функций drawCell()*/
    }

    private fun drawFrame(canvas: Canvas) {
        paint.color = Color.LTGRAY
        canvas.drawRect(
            frameOffset.width.toFloat(),
            frameOffset.height.toFloat(),
            width - frameOffset.width.toFloat(),
            height - frameOffset.height.toFloat(),
            paint
        )
    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int) {
        val cellStatus = model?.getCellStatus(row, col)
        if (CellConstants.EMPTY.value != cellStatus) {
            val color = if (CellConstants.EPHEMERAL.value == cellStatus) {
                model?.currentBlock?.color
            } else {
                Block.getColor(cellStatus as Byte)
            }
            drawCell(canvas, col, row, color as Int)
        }
    }

    private fun drawCell(canvas: Canvas, x: Int, y: Int, rgbColor: Int) {
        paint.color = rgbColor
        val top: Float =
            (frameOffset.height + y * cellSize.height + BLOCK_OFFSET).toFloat()
        val left: Float =
            (frameOffset.width + x * cellSize.width + BLOCK_OFFSET).toFloat()
        val bottom: Float =
            (frameOffset.height + (y + 1) * cellSize.height - BLOCK_OFFSET).toFloat()
        val right: Float =
            (frameOffset.width + (x + 1) * cellSize.width - BLOCK_OFFSET).toFloat()
        val rectangle = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rectangle, 4F, 4F, paint)
    }

    override fun onSizeChanged(width: Int, height: Int, previousWidth: Int, previousHeight: Int) {
        super.onSizeChanged(width, height, previousWidth, previousHeight)
        val cellWidth = (width - 2 * FRAME_OFFSET_BASE) /
                FieldConstants.COLUMN_COUNT.value
        val cellHeight = (height - 2 * FRAME_OFFSET_BASE) /
                FieldConstants.ROW_COUNT.value
        val n = Math.min(cellWidth, cellHeight)
        this.cellSize = Dimension(n, n)
        val offsetX = (width - FieldConstants.COLUMN_COUNT.value * n) / 2
        val offsetY = (height - FieldConstants.ROW_COUNT.value * n) / 2
        this.frameOffset = Dimension(offsetX, offsetY)
    }
}