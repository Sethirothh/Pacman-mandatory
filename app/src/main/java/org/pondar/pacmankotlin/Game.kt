package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
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

        var direction: Int = 0

        //did we initialize the coins?
        var coinsInitialized = false


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
            val coin = GoldCoin(0, 0)
            coin.coinx = Random().nextInt(950)
            coin.coiny = Random().nextInt(1000)
            coins.add(coin)
        }
        coinsInitialized = true
    }


    fun newGame() {
        pacx = 50
        pacy = 400 //just some starting coordinates - you can change this.
        coins.clear()
        initializeGoldcoins()

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

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {

    }


}