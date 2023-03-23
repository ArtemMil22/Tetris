package com.example.mainactivity.models;

import android.graphics.Color;
import android.graphics.Point;

import androidx.annotation.NonNull;

import com.example.mainactivity.constants.FieldConstants;

import java.util.Random;

public class Block {
    /* Программно моделируем сам блок, после того как мы смоделировали
    фрейм блока и его форму в Shape
    Наш процесс моделирования будет заключаться в добавлении переменные
    экземпляра, представляющие характеристики блока */

    // добавляем 4 переменные экземпляра
    private int shapeIndex;
    // shapeIndex - Переменная включает индекс формы блока
    private int frameNumber;
    //frameNumber - Переменная отслеживает кол-во фреймов, относящихся к форме блока
    private BlockColor color;
    // color - содержит цветовые характеристики блока
    private Point position;
    // position - используется для отслеживания текущей пространственной позиции блока


    /* Создаем контсруктор для класса, который инициализирует созданные нами
    переменные экземпляра, указывая их начальные состояния. */
    private Block(int shapeIndex, BlockColor blockColor) {
    /* Новый экземпряр? Block создается с двумя переменными, ему в качестве
    аргументов значениями, и позиция блока устанавливается вдоль оси Х.
    Метод createBlock() возвращает созданный и инициализированный блок.
     */
        this.frameNumber = 0;
        this.shapeIndex = shapeIndex;
        this.color = blockColor;
        this.position = new Point(
                FieldConstants.COLUMN_COUNT.getValue() / 2,
                0
        );
        /* this.position - здесь задается положение переменной экземпляра
        позиции для текущего экземпляра блока.

         */
    }
//AppModel. было

    public static Block createBlock() {
    /*Все предыдущие определения конструктора объявлены в частном доступе
     Но Нам нужно, чтобы другие классы имели возможности для создания экземпряра
     блока, и именно для этого нам следует определить статический метод,
     который это разрешает, это метод createBlock().
     Метод createBlock случайным образом выбирает индекс для формы тетрамино
    в классе enum Shape и BlockColor, а затем присваивает два случайно выбранных
    значения элементам shapeIndex и blockColor.*/
        Random random = new Random();
        int shapeIndex = random.nextInt(Shape.values().length);
        BlockColor blockColor = BlockColor.values()
                [random.nextInt(BlockColor.values().length)];

        Block block = new Block(shapeIndex, blockColor);
        block.position.x = block.position.x - Shape.values()
                [shapeIndex].getStartPosition();
        return block;
        /* Метод createBlock() возвращает созданный и инициализированный блок.*/
    }

    public enum BlockColor {
        /* Шаблон enum BlockColor добаляется внутри класса Block
        Данный класс перечисления формирует постоянный набор экземпляров BlockColor,
        каждый из которых обладает свойствами rgbValue и  byteValue.
         rgbValue - это свойство является целым числом, которое однозначно
         определяет цвет RGB, указанный с помощью метода Color.rgb()
         Сам класс Color предоставляется платформой Android, где
         rgb() - метод класса, определенный внутри класса Color.
         Пять вызовов Color.rgb() определяют соответственно цвета: розовый,
         зеленый, оранжевый, желтый, голубой.

         */
        PINK(Color.rgb(255, 105, 180), (byte) 2),
        GREEN(Color.rgb(0, 128, 0), (byte) 3),
        ORANGE(Color.rgb(255, 140, 0), (byte) 4),
        YELLOW(Color.rgb(255, 255, 0), (byte) 5),
        CYAN(Color.rgb(0, 255, 255), (byte) 6);

        BlockColor(int rgbValue, byte value) {
            this.rgbValue = rgbValue;
            this.byteValue = value;
        }

        private final int rgbValue;
        private final byte byteValue;
    }

    /* Внизу кода я добавляю в класс Block несколько методов геттеров и
    сеттеров(получения и установки(по Java наверное))
    Эти методы позволят получать доступ к важнейшим свойствам экземпляров
    блока.
     */

    public static int getColor(byte value) {
        for (BlockColor colour : BlockColor.values()) {
            if (value == colour.byteValue) {
                return colour.rgbValue;
            }
        }
        return -1;
    }

    public final void setState(int frame, Point position) {
        this.frameNumber = frame;
        this.position = position;
    }

    @NonNull
    /* Анотация обозначает, что возвращаемо поле, параметр или метод
    не могут иметь значение null, позволяя указать на тот факт, что
    метод getShape() не может возвращать нулевое значение. */
    public final byte[][] getShape(int frameNumber) {
        return Shape.values()[shapeIndex].getFrame(frameNumber).as2dByteArray();
    }

    public Point getPosition() {
        return this.position;
    }

    public final int getFrameCount() {

        return Shape.values()[shapeIndex].getFrameCount();
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public int getColor() {
        return color.rgbValue;
    }

    public byte getStaticValue() {
        return color.byteValue;
    }
}
