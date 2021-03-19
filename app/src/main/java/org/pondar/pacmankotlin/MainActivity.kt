package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnClickListener {

    //reference to the game class.
    private var game: Game? = null

    private var myTimer: Timer = Timer()
    private var gameTimer: Timer = Timer()
    private var counter : Int = 60
    private var running = false
    private var direction = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener(this)
        stopButton.setOnClickListener(this)
        resetButton.setOnClickListener(this)

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
        //We will call the timer 5 times each second
        gameTimer.schedule(object : TimerTask() {
            override fun run() {
                gameTimerMethod()
            }
        }, 0, 1000) //0 indicates we start now, 200
        //is the number of miliseconds between each call
        // movement buttons
        moveRight.setOnClickListener {
            direction = 1
//            game?.movePacmanRight(10)
        }
        moveLeft.setOnClickListener {
            direction = 4
//            game?.movePacmanLeft(10)
        }
        moveDown.setOnClickListener {
            direction = 2
//            game?.movePacmanDown(10)
        }
        moveUp.setOnClickListener {
            direction = 3
//            game?.movePacmanUp(10)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    private fun gameTimerMethod() {
        this.runOnUiThread(gameTimerTick)
    }
    private fun timerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //we could do updates here TO GAME LOGIC,
        // but not updates TO ACTUAL UI

        // gameView.move(20)  // BIG NO NO TO DO THIS - WILL CRASH ON OLDER DEVICES!!!!


        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(gamerTick)

    }
    private val gameTimerTick = Runnable {
        if (running) {
            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            if (counter > 0) {
                counter--
                timerView.text = getString(R.string.timer_value,counter)

                if(game?.isGameWon() == true){
                    running = false
                    val toast = Toast.makeText(this, "Niveau Gennemført!", Toast.LENGTH_SHORT)
                    //                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()

                }
                if(game?.isGameLost() == true){
                    running = false
                    val toast = Toast.makeText(this, "Du forgiftet, så du taber!", Toast.LENGTH_SHORT)
                    //                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()

                }

            } else if (game?.running == false) {
running = false
                val toast = Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()

            }
        } else {
            counter = 60
        }
    }
    private val gamerTick = Runnable {
        if (running) {
            when(direction) {
                1 -> game?.movePacmanRight(8)
                2 -> game?.movePacmanDown(8)
                3 -> game?.movePacmanUp(8)
                4 -> game?.movePacmanLeft(8)
            }
            when(direction) {
                1 -> game?.moveGhostUp(5)
                2 -> game?.moveGhostLeft(5)
                3 -> game?.moveGhostRight(5)
                4 -> game?.moveGhostDown(5)
            }
            if (counter <= 0){
                game?.direction = 0
                game?.running = false
            }

        }
    }

    private fun gameIsOver(gameString: CharSequence) {
        running = false
        if (running == false){
            val toast = Toast.makeText(this, gameString, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
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
    override fun onClick(v: View) {
        //game?.initializeGoldcoins()
        if (v.id == R.id.startButton) {
            running = true
        } else if (v.id == R.id.stopButton) {
            running = false
        } else if (v.id == R.id.resetButton) {
            if (game?.isGameWon() == true) {
                game?.initializeEnemies()
                //clear coin array
                game?.coins?.clear()
                //reset pacman
                game?.pacx = 450
                game?.pacy = 1000
                //spawn new coins
                game?.initializeGoldcoins()
                //reset direction / stop movement
                game?.direction = 0
                //set running to true
                game?.running = true
                //position each ghost a random place
                for (ghost in game?.ghosts!!){
                    ghost.enemyx = Random().nextInt(900)
                    ghost.enemyy = Random().nextInt(400)
                }
                //add 30 seconds
                counter += 30
                timerView.text = getString(R.string.timer_value,counter)
                //redraw screen
                gameView?.invalidate()
            }
            else {
                counter = 0
                game?.newGame() //you should call the newGame method instead of this
                running = false
                timerView.text = getString(R.string.timer_value,counter)

            }
        }
    }
}
