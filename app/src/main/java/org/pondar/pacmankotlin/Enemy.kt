package org.pondar.pacmankotlin

class Enemy(x: Int, y: Int) {
    var alive = true
    var enemyx: Int = x
    var enemyy: Int = y
    override fun toString(): String {
        return "GoldCoin(taken=$alive, coinx=$enemyx, coiny=$enemyy)"
    }
}