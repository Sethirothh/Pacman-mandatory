package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import java.util.*


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView) {

        private var pointsView: TextView = view
        private var points : Int = 0

        //bitmap of the pacman
        var pacBitmap: Bitmap
        var ghostBitmap: Bitmap
        var goldBitmap: Bitmap
        var pacx: Int = 0
        var pacy: Int = 0
        var running = false
        var counter : Int = 60

        var direction: Int = 0

        //did we initialize the coins?
        var coinsInitialized = false
var ghostsInitialized = false

        //the list of goldcoins - initially empty
        var coins = ArrayList<GoldCoin>()

        //the list of ghosts
        var ghosts = ArrayList<Enemy>(1)

        //a reference to the gameview
        private var gameView: GameView? = null
        private var h: Int = 0
        private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
    }
    init {
        ghostBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ghost)
    }
    init {
        goldBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins()
    {
        //DO Stuff to initialize the array list with some coins.
        for (i in 0..4){
            var coinxx = Random().nextInt(950)
            var coinyy = Random().nextInt(1000)
            var coin = GoldCoin(coinxx, coinyy, false)
            coins.add(coin)
        }
        coinsInitialized = true
    }

    fun initializeEnemies()
    {
        var enemyx = Random().nextInt(1000)
        var enemyy = Random().nextInt(950)
        val ghost = Enemy(true, enemyx , enemyy )
        ghosts.add(ghost)
        ghostsInitialized = true
    }



    fun newGame() {
        pacx = 50
        coins.clear()
        ghosts.clear()
        counter = 60
        pacy = 400 //just some starting coordinates - you can change this.
        coins.clear()
        initializeGoldcoins()
        initializeEnemies()
        //reset the points
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"

        gameView?.invalidate() //redraw screen
        direction = 0
        running = true
    }



    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }
    fun movePacmanLeft(pixels: Int) {
        //still within our boundaries?
        if (pacx - pixels > 0) {
            pacx -= pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }
    fun movePacmanUp(pixels: Int) {
        //still within our boundaries?
        if (pacy - pixels > 0) {
            pacy -= pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }
    fun movePacmanDown(pixels: Int) {
        //still within our boundaries?
        if (pacy + pixels + pacBitmap.height < h) {
            pacy += pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun moveGhostRight(pixels: Int) {
        for (ghost in ghosts){
            if (ghost.enemyx + pixels + ghostBitmap.width < w){
                ghost.enemyx += pixels
                gameView!!.invalidate()
            }
        }
    }
    fun moveGhostLeft(pixels: Int) {
        for (ghost in ghosts){
            if (ghost.enemyx - pixels > 0){
                ghost.enemyx -= pixels
                gameView!!.invalidate()
            }
        }
    }
    fun moveGhostUp(pixels: Int) {
        for (ghost in ghosts){
            if (ghost.enemyy - pixels > 0){
                ghost.enemyy -= pixels
                gameView!!.invalidate()
            }
        }
    }
    fun moveGhostDown(pixels: Int) {
        for (ghost in ghosts){
            if (ghost.enemyy + pixels + ghostBitmap.height < h){
                ghost.enemyy += pixels
                gameView!!.invalidate()
            }
        }
    }


    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {
        //Pacman
        val h1 = pacBitmap.height
        val w1 = pacBitmap.width
        val x1 = pacx
        val y1 = pacy

        //Collision check for ghosts
        for (ghost in ghosts){
            //Ghost
            val h3 = ghostBitmap.height
            val w3 = ghostBitmap.width
            val x3 = ghost.enemyx
            val y3 = ghost.enemyy
            //Ghost collision
            var ghostCollisionX = false
            var ghostCollisionY = false
            //Check which position is lesser on x
            if (x1 < x3){
                //check if there is gap on x axis
                if (x1 + w1 > x3){
                    ghostCollisionX = true
                }
            }
            else{
                //check if there is a gap on x axis
                if (x3 + w3 > x1){
                    ghostCollisionX = true
                }
            }

            //check which position is lesser on y
            if (y1 < y3){
                //check if there is a gab on y axis
                if (y1 + h1 > y3){
                    ghostCollisionY = true
                }
            }
            else{
                //check if there is a gab on y axis
                if (y3 + h3 > y1){
                    ghostCollisionY = true
                }
            }
            if (ghostCollisionX && ghostCollisionY){
                if (isDead()){
                    running = false
                    direction = 0
                    Log.d("lost", "Game Over")
                }
            }
        }



        for (coin in coins){
            //Coin
            val h2 = goldBitmap.height
            val w2 = goldBitmap.width
            val x2 = coin.coinx
            val y2 = coin.coiny
            //Coin collision
            var coinCollisionX = false
            var coinCollisionY = false
            //Check which position is lesser on x
            if (x1 < x2){
                //check if there is gap on x axis
                if (x1 + w1 > x2){
                    coinCollisionX = true
                }
            }
            else{
                //check if there is a gap on x axis
                if (x2 + w2 > x1){
                    coinCollisionX = true
                }
            }

            //check which position is lesser on y
            if (y1 < y2){
                //check if there is a gab on y axis
                if (y1 + h1 > y2){
                    coinCollisionY = true
                }
            }
            else{
                //check if there is a gab on y axis
                if (y2 + h2 > y1){
                    coinCollisionY = true
                }
            }
            if (coinCollisionX && coinCollisionY){
                if (coin.taken == true){
                    continue
                }
                else{
                    //update score and check if game is won
                    coin.taken = true
                    points += 5
                    pointsView.text = "${context.resources.getString(R.string.points)} $points"
                    if (points == 25){
                        running = false
                        direction = 0
                        Log.d("win", "Du vandt!")
                    }
                }
            }
        }
    }


    fun isGameWon(): Boolean{
        for (coin in coins){
            if (!coin.taken){
                return false
            }
        }
        return true
    }
    fun isDead(): Boolean{
        for (ghost in ghosts){
            if (!ghost.alive){
                return false
            }
        }
        return true
    }
}