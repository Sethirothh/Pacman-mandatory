package org.pondar.pacmankotlin

//Here you need to fill out what should be in a GoldCoin and what should the constructor be
class GoldCoin(x: Int, y: Int) {
    var taken = false
    var coinx: Int = x
    var coiny: Int = y
    override fun toString(): String {
        return "GoldCoin(taken=$taken, coinx=$coinx, coiny=$coiny)"
    }
}