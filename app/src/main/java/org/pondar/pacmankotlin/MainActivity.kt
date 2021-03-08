package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    //reference to the game class.
    private var game: Game? = null

    private var myTimer: Timer = Timer()
    private var gameTimer: Timer = Timer()
    private var counter : Int = 60


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        game = Game(this,pointsView)

        // intialize the game view class and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        //make a new timer
        game?.running = true //should the game be running?
        //We will call the timer 5 times each second
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }
        }, 0, 17) //0 indicates we start now, 200
        //is the number of miliseconds between each call

        //timer
        gameTimer.schedule(object : TimerTask() {
            override fun run() {
                timerValue()
            }
        },0,1000)


        // movement buttons
        moveRight.setOnClickListener {
            game?.movePacmanRight(10) }
        moveLeft.setOnClickListener {
            game?.movePacmanLeft(10) }
        moveDown.setOnClickListener {
            game?.movePacmanDown(10) }
        moveUp.setOnClickListener {
            game?.movePacmanUp(10) }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun timerValue(){
        this.runOnUiThread(timerSecond)
    }

    private  val timerSecond = Runnable {
        timerView.text = "${getString(R.string.timer_value)} $counter secs"
        if (game?.running!!){
            if (game?.direction == 1) {
                counter--
                timerView.text = "${getString(R.string.timer_value)} $counter secs"
            }
            if (game?.direction == 2){
                counter--
                timerView.text = "${getString(R.string.timer_value)} $counter secs"
            }
            if (game?.direction == 3){
                counter--
                timerView.text = "${getString(R.string.timer_value)} $counter secs"
            }
            if (game?.direction == 4){
                counter--
                timerView.text = "${getString(R.string.timer_value)} $counter secs"
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
