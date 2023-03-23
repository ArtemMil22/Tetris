package com.example.mainactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import com.example.mainactivity.storage.AppPreferences
import com.google.android.material.snackbar.Snackbar

class activity_main : AppCompatActivity() {

    var tvHighScore: TextView? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Bundle - ПАКЕТ, СВЕРТОК , СВЯЗКА, УЗЕЛ
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val textView:TextView = TextView(this)
//        val linearLayout: LinearLayout = LinearLayout(this)

        supportActionBar?.hide()
        // используем переменную AS - поддержка деятельности полосы . скрыть

        val bthNewGame = findViewById<Button>(R.id.btn_new_game)
        val bthResetScore = findViewById<Button>(R.id.bth_reset_score)
        val bthExit = findViewById<Button>(R.id.btn_exit)
        tvHighScore = findViewById<TextView>(R.id.tv_high_score2)

        var preferences1 = AppPreferences(this)
        tvHighScore?.text = "${preferences1.getHighScore()}"

//        var tvHighScore2 = findViewById<TextView>(R.id.tv_high_score)

//        tvHighScore?.text = tvHighScore2.text


        bthNewGame.setOnClickListener(this::onBthNewGameClick)

        bthResetScore.setOnClickListener(this::onBtnResetScoreClick)
        // установи на щелчок слушителя
        bthExit.setOnClickListener(this::onBthExitClick )


    }

    private fun onBthNewGameClick(view: View) {
        val intent = Intent(this, GameActivity::class.java)
    /*  Создается новый экземпляр класса Intent
      и передается конструктору текущий контекст и требуемый класс действия
      this, передается в качестве первого аргумента,
      используется для ссылки на текущий экземпрляр, в котором вызывается,
      фактически передается текущее действие - MainActivity
      Все действия являются расширениями абстрактного класса Context.
      Все действия находятся в их собственных правовых контекстах. */
        startActivity(intent)
        /* startActivity(intent) - вызывается для запуска действия, от которого
        не ожидается каких-либо результатов. Если намерение передается в качестве
        единственного аргумента, оно начинает действие, от которого не ожидается
        результата (?только переход на другое активити?)*/
    }


    private fun onBtnResetScoreClick(view: View) {
        val preferences = AppPreferences(this)
        preferences.clearHighScore()
            /* AppPreferences(это класс, с аргументом ctx в контрукторе с типои Context)
            имеющий в теле переменную типа SharedPreferences, которая задействована
            в функциях, для получения сброса очков, записи текущих очков и записи
            наивысших очков.
            AppPreferences- это созданный нами класс для хранения, организации доступа,
            модификазии данных и сохранения данных в наборе пар ключ-значение.
            В него мы помещаем this, это полученное
            значение функции onBtnResetScoreClick от щелчка по кнопке.
            После вызываем функцию класса AppPreferences, сбросить значение очков.
             */
        Snackbar
            .make(view, "Score successfully reset", Snackbar.LENGTH_SHORT)
            .show()
        tvHighScore?.text = "High score: ${preferences.getHighScore()}"
    }

    /* Мы используем класс Snackbar(Закусочная), он похож на Toast(Всплывающее окно),
    есть еще ToolBar.
    используется для отображения короткого сообщения внизу экрана (bar),
    сообщение можно закрыть просто проведя по нему пальцем.
    make() - принимает три аргумента,
    1-корневой пакет активности (view);
    2-сообщение которое хотим отобразить;
    3-период времени в течение которого должна отображаться Snackbar.
     еще применяемые к Snackbar .setAction()"добавляем кнопку"
     и .setActionTextColor() "меняем цвет"
     И в конце для tvHighScore? , мы возвращаем перезаписанное значение, с помощью
     getHighScore()
     */
    private fun onBthExitClick(view: View) {
        System.exit(0)
        // прекращает дальнейшее выполнение программы, если аргумент 0
    }
}